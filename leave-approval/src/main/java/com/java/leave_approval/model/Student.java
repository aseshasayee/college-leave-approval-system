package com.java.leave_approval.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(name = "roll_no", nullable = false, unique = true, length = 20)
    private String rollNo;
    
    @Column(nullable = false, length = 50)
    private String department;
    
    @Column(nullable = false)
    private Integer year;
    
    @Column(nullable = false, length = 10)
    private String section;
    
    @Column(nullable = false)
    private Integer semester;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public Student() {}
    
    public Student(User user, String rollNo, String department, Integer year, String section, Integer semester) {
        this.user = user;
        this.rollNo = rollNo;
        this.department = department;
        this.year = year;
        this.section = section;
        this.semester = semester;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", rollNo='" + rollNo + '\'' +
                ", department='" + department + '\'' +
                ", year=" + year +
                ", section='" + section + '\'' +
                ", semester=" + semester +
                ", createdAt=" + createdAt +
                '}';
    }
}
