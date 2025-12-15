package com.java.leave_approval.service;

import com.java.leave_approval.model.*;
import com.java.leave_approval.repository.*;
import com.java.leave_approval.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {
    
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ApprovalStepRepository approvalStepRepository;
    
    @Autowired
    private DocumentSignatureRepository documentSignatureRepository;
    
    @Autowired
    private QueryRepository queryRepository;
    
    @Autowired
    private DigitalSignatureService digitalSignatureService;
    
    @Autowired
    private SupabaseStorageService supabaseStorageService;
    
    /**
     * Submit a new leave request with image upload
     */
    @Transactional
    public LeaveRequest submitLeaveRequest(Long studentId, LeaveType type, MultipartFile file) throws Exception {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Upload file to Supabase Storage
        String documentPath = supabaseStorageService.uploadFile(file);
        
        // Create leave request
        LeaveRequest request = new LeaveRequest(student, type, documentPath);
        request = leaveRequestRepository.save(request);
        
        // Create approval chain automatically
        createApprovalChain(request, student);
        
        return request;
    }
    
    /**
     * Submit a new leave request with text message (no image)
     */
    @Transactional
    public LeaveRequest submitLeaveRequestWithMessage(Long studentId, LeaveType type, String message) throws Exception {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Upload message to Supabase Storage
        String documentPath = supabaseStorageService.uploadTextMessage(message, studentId);
        
        // Create leave request
        LeaveRequest request = new LeaveRequest(student, type, documentPath);
        request = leaveRequestRepository.save(request);
        
        // Create approval chain
        createApprovalChain(request, student);
        
        return request;
    }
    
    /**
     * Create approval chain: FA -> Year Coordinator -> HOD
     */
    private void createApprovalChain(LeaveRequest request, Student student) {
        // Get authorities for this student's department and year
        // For now, using simple role-based selection
        // In production, you'd query based on department, year, section
        
        List<User> facultyAdvisors = userRepository.findByRole(UserRole.FACULTY_ADVISOR);
        List<User> yearCoordinators = userRepository.findByRole(UserRole.YEAR_COORDINATOR);
        List<User> hods = userRepository.findByRole(UserRole.HOD);
        
        if (!facultyAdvisors.isEmpty()) {
            ApprovalStep step1 = new ApprovalStep(request, facultyAdvisors.get(0), UserRole.FACULTY_ADVISOR, 1);
            approvalStepRepository.save(step1);
        }
        
        if (!yearCoordinators.isEmpty()) {
            ApprovalStep step2 = new ApprovalStep(request, yearCoordinators.get(0), UserRole.YEAR_COORDINATOR, 2);
            approvalStepRepository.save(step2);
        }
        
        if (!hods.isEmpty()) {
            ApprovalStep step3 = new ApprovalStep(request, hods.get(0), UserRole.HOD, 3);
            approvalStepRepository.save(step3);
        }
    }
    
    /**
     * Get all pending requests for a specific approver
     */
    public List<LeaveRequest> getPendingRequestsForApprover(Long approverId) {
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new RuntimeException("Approver not found"));
        
        List<ApprovalStep> pendingSteps = approvalStepRepository
            .findByApproverAndDecision(approver, ApprovalDecision.PENDING);
        
        return pendingSteps.stream()
            .map(ApprovalStep::getRequest)
            .collect(Collectors.toList());
    }
    
    /**
     * Process approval/rejection by authority
     */
    @Transactional
    public void processApproval(Long requestId, Long approverId, ApprovalActionDTO action) throws Exception {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new RuntimeException("Approver not found"));
        
        // Find the current approval step for this approver
        List<ApprovalStep> steps = approvalStepRepository.findByRequestOrderByStepOrder(request);
        ApprovalStep currentStep = steps.stream()
            .filter(s -> s.getApprover().getId().equals(approverId) && 
                        s.getDecision() == ApprovalDecision.PENDING)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No pending approval step found for this approver"));
        
        // Update decision
        currentStep.setDecision(action.getDecision());
        currentStep.setRemarks(action.getRemarks());
        currentStep.setActionTime(LocalDateTime.now());
        approvalStepRepository.save(currentStep);
        
        // If approved, generate digital signature
        if (action.getDecision() == ApprovalDecision.APPROVED) {
            generateAndStoreSignature(currentStep, request);
            
            // Check if this is the final approval (HOD)
            if (currentStep.getApproverRole() == UserRole.HOD) {
                request.setStatus(ApprovalDecision.APPROVED);
                leaveRequestRepository.save(request);
            }
        } else if (action.getDecision() == ApprovalDecision.REJECTED) {
            // If rejected, update request status
            request.setStatus(ApprovalDecision.REJECTED);
            leaveRequestRepository.save(request);
        }
    }
    
    /**
     * Raise a query on a request
     */
    @Transactional
    public void raiseQuery(Long requestId, Long approverId, String message) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new RuntimeException("Approver not found"));
        
        Query query = new Query(request, approver, message);
        queryRepository.save(query);
        
        // Update request status to QUERY
        request.setStatus(ApprovalDecision.QUERY);
        leaveRequestRepository.save(request);
    }
    
    /**
     * Resolve a query
     */
    @Transactional
    public void resolveQuery(Long queryId) {
        Query query = queryRepository.findById(queryId)
            .orElseThrow(() -> new RuntimeException("Query not found"));
        
        query.setResolved(true);
        queryRepository.save(query);
        
        // Update request status back to PENDING
        LeaveRequest request = query.getRequest();
        request.setStatus(ApprovalDecision.PENDING);
        leaveRequestRepository.save(request);
    }
    
    /**
     * Get detailed status of a leave request
     */
    public LeaveRequestStatusDTO getRequestStatus(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        LeaveRequestStatusDTO statusDTO = new LeaveRequestStatusDTO();
        statusDTO.setRequestId(request.getId());
        statusDTO.setType(request.getType());
        statusDTO.setStatus(request.getStatus());
        statusDTO.setDocumentPath(request.getDocumentPath());
        statusDTO.setCreatedAt(request.getCreatedAt());
        
        // Get approval steps
        List<ApprovalStep> steps = approvalStepRepository.findByRequestOrderByStepOrder(request);
        List<LeaveRequestStatusDTO.ApprovalStepDTO> stepDTOs = steps.stream()
            .map(step -> new LeaveRequestStatusDTO.ApprovalStepDTO(
                step.getApprover().getName(),
                step.getApproverRole().name(),
                step.getDecision(),
                step.getRemarks(),
                step.getActionTime(),
                step.getStepOrder()
            ))
            .collect(Collectors.toList());
        statusDTO.setApprovalSteps(stepDTOs);
        
        // Get queries
        List<Query> queries = queryRepository.findByRequest(request);
        List<LeaveRequestStatusDTO.QueryInfoDTO> queryDTOs = queries.stream()
            .map(query -> new LeaveRequestStatusDTO.QueryInfoDTO(
                query.getId(),
                query.getRaisedBy().getName(),
                query.getMessage(),
                query.getResolved(),
                query.getCreatedAt()
            ))
            .collect(Collectors.toList());
        statusDTO.setQueries(queryDTOs);
        
        return statusDTO;
    }
    
    /**
     * Get all requests for a student
     */
    public List<LeaveRequestStatusDTO> getStudentRequests(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        List<LeaveRequest> requests = leaveRequestRepository.findByStudentOrderByCreatedAtDesc(student);
        
        return requests.stream()
            .map(request -> getRequestStatus(request.getId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Generate and store digital signature for approved step
     */
    private void generateAndStoreSignature(ApprovalStep step, LeaveRequest request) throws Exception {
        // Download document content from Supabase
        byte[] documentContent = supabaseStorageService.downloadFile(request.getDocumentPath());
        
        // Generate hash
        byte[] documentHash = digitalSignatureService.generateDocumentHash(documentContent);
        
        // Load approver's private key (in production, use secure key management)
        // For now, we'll use a demo key path
        String keyPath = "keys/user_" + step.getApprover().getId() + "_private.key";
        PrivateKey privateKey;
        
        try {
            privateKey = digitalSignatureService.loadPrivateKey(keyPath);
        } catch (Exception e) {
            // If key doesn't exist, generate new key pair
            var keyPair = digitalSignatureService.generateKeyPair();
            Files.createDirectories(Paths.get("keys"));
            digitalSignatureService.storePrivateKey(keyPair.getPrivate(), keyPath);
            digitalSignatureService.storePublicKey(keyPair.getPublic(), 
                "keys/user_" + step.getApprover().getId() + "_public.key");
            privateKey = keyPair.getPrivate();
        }
        
        // Sign the document hash
        byte[] signature = digitalSignatureService.signDocument(documentHash, privateKey);
        
        // Store signature
        DocumentSignature docSignature = new DocumentSignature(step, step.getApprover(), signature);
        documentSignatureRepository.save(docSignature);
    }
}
