package com.java.leave_approval.dto;

import com.java.leave_approval.model.LeaveType;

public class LeaveRequestDTO {
    private LeaveType type;
    private String message; // Optional: if student types instead of uploading
    
    public LeaveRequestDTO() {}
    
    public LeaveRequestDTO(LeaveType type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public LeaveType getType() {
        return type;
    }
    
    public void setType(LeaveType type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
