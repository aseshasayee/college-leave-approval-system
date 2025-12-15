package com.java.leave_approval.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "queries")
public class Query {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private LeaveRequest request;
    
    @ManyToOne
    @JoinColumn(name = "raised_by", nullable = false)
    private User raisedBy;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(nullable = false)
    private Boolean resolved = false;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public Query() {}
    
    public Query(LeaveRequest request, User raisedBy, String message) {
        this.request = request;
        this.raisedBy = raisedBy;
        this.message = message;
        this.resolved = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LeaveRequest getRequest() {
        return request;
    }
    
    public void setRequest(LeaveRequest request) {
        this.request = request;
    }
    
    public User getRaisedBy() {
        return raisedBy;
    }
    
    public void setRaisedBy(User raisedBy) {
        this.raisedBy = raisedBy;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean getResolved() {
        return resolved;
    }
    
    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Query{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", resolved=" + resolved +
                ", createdAt=" + createdAt +
                '}';
    }
}
