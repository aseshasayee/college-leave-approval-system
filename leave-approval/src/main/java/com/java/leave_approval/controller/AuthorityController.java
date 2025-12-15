package com.java.leave_approval.controller;

import com.java.leave_approval.model.*;
import com.java.leave_approval.dto.*;
import com.java.leave_approval.service.LeaveRequestService;
import com.java.leave_approval.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authority")
@CrossOrigin(origins = "*")
public class AuthorityController {
    
    @Autowired
    private LeaveRequestService leaveRequestService;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all pending requests for an authority
     * GET /api/authority/{approverId}/pending-requests
     */
    @GetMapping("/{approverId}/pending-requests")
    public ResponseEntity<?> getPendingRequests(@PathVariable Long approverId) {
        try {
            List<LeaveRequest> requests = leaveRequestService.getPendingRequestsForApprover(approverId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get detailed status of a request
     * GET /api/authority/request/{requestId}/details
     */
    @GetMapping("/request/{requestId}/details")
    public ResponseEntity<?> getRequestDetails(@PathVariable Long requestId) {
        try {
            LeaveRequestStatusDTO status = leaveRequestService.getRequestStatus(requestId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Approve or reject a request
     * POST /api/authority/{approverId}/request/{requestId}/approve
     */
    @PostMapping("/{approverId}/request/{requestId}/approve")
    public ResponseEntity<?> processApproval(
            @PathVariable Long approverId,
            @PathVariable Long requestId,
            @RequestBody ApprovalActionDTO action) {
        try {
            leaveRequestService.processApproval(requestId, approverId, action);
            return ResponseEntity.ok("Request processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Raise a query on a request
     * POST /api/authority/{approverId}/request/{requestId}/query
     */
    @PostMapping("/{approverId}/request/{requestId}/query")
    public ResponseEntity<?> raiseQuery(
            @PathVariable Long approverId,
            @PathVariable Long requestId,
            @RequestBody QueryDTO queryDTO) {
        try {
            leaveRequestService.raiseQuery(requestId, approverId, queryDTO.getMessage());
            return ResponseEntity.ok("Query raised successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
