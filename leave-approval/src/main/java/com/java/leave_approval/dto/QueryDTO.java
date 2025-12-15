package com.java.leave_approval.dto;

public class QueryDTO {
    private String message;
    
    public QueryDTO() {}
    
    public QueryDTO(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
