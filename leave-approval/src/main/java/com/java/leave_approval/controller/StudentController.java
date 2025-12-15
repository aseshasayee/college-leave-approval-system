package com.java.leave_approval.controller;

import com.java.leave_approval.model.*;
import com.java.leave_approval.dto.*;
import com.java.leave_approval.service.LeaveRequestService;
import com.java.leave_approval.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private LeaveRequestService leaveRequestService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Submit leave request with image upload
     * POST /api/student/{studentId}/leave-request/upload
     */
    @PostMapping("/{studentId}/leave-request/upload")
    public ResponseEntity<?> submitLeaveRequestWithImage(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") LeaveType type) {
        try {
            LeaveRequest request = leaveRequestService.submitLeaveRequest(studentId, type, file);
            return ResponseEntity.ok(leaveRequestService.getRequestStatus(request.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Submit leave request with text message
     * POST /api/student/{studentId}/leave-request/message
     */
    @PostMapping("/{studentId}/leave-request/message")
    public ResponseEntity<?> submitLeaveRequestWithMessage(
            @PathVariable Long studentId,
            @RequestBody LeaveRequestDTO requestDTO) {
        try {
            LeaveRequest request = leaveRequestService.submitLeaveRequestWithMessage(
                studentId, requestDTO.getType(), requestDTO.getMessage());
            return ResponseEntity.ok(leaveRequestService.getRequestStatus(request.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get all leave requests for a student
     * GET /api/student/{studentId}/leave-requests
     */
    @GetMapping("/{studentId}/leave-requests")
    public ResponseEntity<?> getStudentRequests(@PathVariable Long studentId) {
        try {
            List<LeaveRequestStatusDTO> requests = leaveRequestService.getStudentRequests(studentId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get real-time status of a specific request
     * GET /api/student/request/{requestId}/status
     */
    @GetMapping("/request/{requestId}/status")
    public ResponseEntity<?> getRequestStatus(@PathVariable Long requestId) {
        try {
            LeaveRequestStatusDTO status = leaveRequestService.getRequestStatus(requestId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Resolve a query (student responds via FA)
     * POST /api/student/query/{queryId}/resolve
     */
    @PostMapping("/query/{queryId}/resolve")
    public ResponseEntity<?> resolveQuery(@PathVariable Long queryId) {
        try {
            leaveRequestService.resolveQuery(queryId);
            return ResponseEntity.ok("Query resolved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
