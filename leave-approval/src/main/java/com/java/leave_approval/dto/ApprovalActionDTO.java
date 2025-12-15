package com.java.leave_approval.dto;

import com.java.leave_approval.model.ApprovalDecision;

public class ApprovalActionDTO {
    private ApprovalDecision decision;
    private String remarks;
    
    public ApprovalActionDTO() {}
    
    public ApprovalActionDTO(ApprovalDecision decision, String remarks) {
        this.decision = decision;
        this.remarks = remarks;
    }
    
    public ApprovalDecision getDecision() {
        return decision;
    }
    
    public void setDecision(ApprovalDecision decision) {
        this.decision = decision;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
