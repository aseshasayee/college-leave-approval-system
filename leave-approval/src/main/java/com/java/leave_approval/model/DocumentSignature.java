package com.java.leave_approval.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_signatures")
public class DocumentSignature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "approval_step_id", nullable = false)
    private ApprovalStep approvalStep;
    
    @ManyToOne
    @JoinColumn(name = "signed_by", nullable = false)
    private User signedBy;
    
    @Column(nullable = false)
    private byte[] signature;
    
    @Column(name = "signed_at", updatable = false)
    private LocalDateTime signedAt;
    
    @PrePersist
    protected void onCreate() {
        signedAt = LocalDateTime.now();
    }
    
    // Constructors
    public DocumentSignature() {}
    
    public DocumentSignature(ApprovalStep approvalStep, User signedBy, byte[] signature) {
        this.approvalStep = approvalStep;
        this.signedBy = signedBy;
        this.signature = signature;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ApprovalStep getApprovalStep() {
        return approvalStep;
    }
    
    public void setApprovalStep(ApprovalStep approvalStep) {
        this.approvalStep = approvalStep;
    }
    
    public User getSignedBy() {
        return signedBy;
    }
    
    public void setSignedBy(User signedBy) {
        this.signedBy = signedBy;
    }
    
    public byte[] getSignature() {
        return signature;
    }
    
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    
    public LocalDateTime getSignedAt() {
        return signedAt;
    }
    
    public void setSignedAt(LocalDateTime signedAt) {
        this.signedAt = signedAt;
    }
    
    @Override
    public String toString() {
        return "DocumentSignature{" +
                "id=" + id +
                ", signedAt=" + signedAt +
                '}';
    }
}
