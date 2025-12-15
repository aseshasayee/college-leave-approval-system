package com.java.leave_approval.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type;
    
    @Column(name = "document_path", nullable = false, columnDefinition = "TEXT")
    private String documentPath;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalDecision status = ApprovalDecision.PENDING;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public LeaveRequest() {}
    
    public LeaveRequest(Student student, LeaveType type, String documentPath) {
        this.student = student;
        this.type = type;
        this.documentPath = documentPath;
        this.status = ApprovalDecision.PENDING;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public LeaveType getType() {
        return type;
    }
    
    public void setType(LeaveType type) {
        this.type = type;
    }
    
    public String getDocumentPath() {
        return documentPath;
    }
    
    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
    
    public ApprovalDecision getStatus() {
        return status;
    }
    
    public void setStatus(ApprovalDecision status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", type=" + type +
                ", documentPath='" + documentPath + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
