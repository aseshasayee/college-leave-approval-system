package com.java.leave_approval.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_steps")
public class ApprovalStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private LeaveRequest request;
    
    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "approver_role", nullable = false)
    private UserRole approverRole;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalDecision decision = ApprovalDecision.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;
    
    @Column(name = "action_time")
    private LocalDateTime actionTime;
    
    @PrePersist
    protected void onCreate() {
        actionTime = LocalDateTime.now();
    }
    
    // Constructors
    public ApprovalStep() {}
    
    public ApprovalStep(LeaveRequest request, User approver, UserRole approverRole, Integer stepOrder) {
        this.request = request;
        this.approver = approver;
        this.approverRole = approverRole;
        this.stepOrder = stepOrder;
        this.decision = ApprovalDecision.PENDING;
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
    
    public User getApprover() {
        return approver;
    }
    
    public void setApprover(User approver) {
        this.approver = approver;
    }
    
    public UserRole getApproverRole() {
        return approverRole;
    }
    
    public void setApproverRole(UserRole approverRole) {
        this.approverRole = approverRole;
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
    
    public Integer getStepOrder() {
        return stepOrder;
    }
    
    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }
    
    public LocalDateTime getActionTime() {
        return actionTime;
    }
    
    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }
    
    @Override
    public String toString() {
        return "ApprovalStep{" +
                "id=" + id +
                ", approverRole=" + approverRole +
                ", decision=" + decision +
                ", remarks='" + remarks + '\'' +
                ", stepOrder=" + stepOrder +
                ", actionTime=" + actionTime +
                '}';
    }
}
