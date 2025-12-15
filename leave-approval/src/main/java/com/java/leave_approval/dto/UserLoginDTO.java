package com.java.leave_approval.dto;

public class UserLoginDTO {
    private String email;
    
    public UserLoginDTO() {}
    
    public UserLoginDTO(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
