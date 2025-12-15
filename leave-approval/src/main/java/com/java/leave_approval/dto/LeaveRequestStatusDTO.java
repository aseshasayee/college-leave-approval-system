package com.java.leave_approval.dto;

import com.java.leave_approval.model.LeaveType;
import com.java.leave_approval.model.ApprovalDecision;
import java.time.LocalDateTime;
import java.util.List;

public class LeaveRequestStatusDTO {
    private Long requestId;
    private LeaveType type;
    private ApprovalDecision status;
    private String documentPath;
    private LocalDateTime createdAt;
    private List<ApprovalStepDTO> approvalSteps;
    private List<QueryInfoDTO> queries;
    
    public static class ApprovalStepDTO {
        private String approverName;
        private String approverRole;
        private ApprovalDecision decision;
        private String remarks;
        private LocalDateTime actionTime;
        private Integer stepOrder;
        
        public ApprovalStepDTO() {}
        
        public ApprovalStepDTO(String approverName, String approverRole, ApprovalDecision decision, 
                              String remarks, LocalDateTime actionTime, Integer stepOrder) {
            this.approverName = approverName;
            this.approverRole = approverRole;
            this.decision = decision;
            this.remarks = remarks;
            this.actionTime = actionTime;
            this.stepOrder = stepOrder;
        }
        
        // Getters and Setters
        public String getApproverName() { return approverName; }
        public void setApproverName(String approverName) { this.approverName = approverName; }
        
        public String getApproverRole() { return approverRole; }
        public void setApproverRole(String approverRole) { this.approverRole = approverRole; }
        
        public ApprovalDecision getDecision() { return decision; }
        public void setDecision(ApprovalDecision decision) { this.decision = decision; }
        
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
        
        public LocalDateTime getActionTime() { return actionTime; }
        public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }
        
        public Integer getStepOrder() { return stepOrder; }
        public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    }
    
    public static class QueryInfoDTO {
        private Long queryId;
        private String raisedByName;
        private String message;
        private Boolean resolved;
        private LocalDateTime createdAt;
        
        public QueryInfoDTO() {}
        
        public QueryInfoDTO(Long queryId, String raisedByName, String message, 
                           Boolean resolved, LocalDateTime createdAt) {
            this.queryId = queryId;
            this.raisedByName = raisedByName;
            this.message = message;
            this.resolved = resolved;
            this.createdAt = createdAt;
        }
        
        // Getters and Setters
        public Long getQueryId() { return queryId; }
        public void setQueryId(Long queryId) { this.queryId = queryId; }
        
        public String getRaisedByName() { return raisedByName; }
        public void setRaisedByName(String raisedByName) { this.raisedByName = raisedByName; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Boolean getResolved() { return resolved; }
        public void setResolved(Boolean resolved) { this.resolved = resolved; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
    
    // Constructors
    public LeaveRequestStatusDTO() {}
    
    // Getters and Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    
    public LeaveType getType() { return type; }
    public void setType(LeaveType type) { this.type = type; }
    
    public ApprovalDecision getStatus() { return status; }
    public void setStatus(ApprovalDecision status) { this.status = status; }
    
    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<ApprovalStepDTO> getApprovalSteps() { return approvalSteps; }
    public void setApprovalSteps(List<ApprovalStepDTO> approvalSteps) { this.approvalSteps = approvalSteps; }
    
    public List<QueryInfoDTO> getQueries() { return queries; }
    public void setQueries(List<QueryInfoDTO> queries) { this.queries = queries; }
}
