package com.java.leave_approval.repository;

import com.java.leave_approval.model.DocumentSignature;
import com.java.leave_approval.model.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentSignatureRepository extends JpaRepository<DocumentSignature, Long> {
    Optional<DocumentSignature> findByApprovalStep(ApprovalStep approvalStep);
}
