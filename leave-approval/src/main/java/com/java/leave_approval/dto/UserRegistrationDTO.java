package com.java.leave_approval.dto;

import com.java.leave_approval.model.UserRole;

public class UserRegistrationDTO {
    private String name;
    private String email;
    private UserRole role;
    
    // Student-specific fields (only if role is STUDENT)
    private String rollNo;
    private String department;
    private Integer year;
    private String section;
    private Integer semester;
    
    public UserRegistrationDTO() {}
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String getRollNo() {
        return rollNo;
    }
    
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getSection() {
        return section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    
    public Integer getSemester() {
        return semester;
    }
    
    public void setSemester(Integer semester) {
        this.semester = semester;
    }
}
