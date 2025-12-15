package com.java.leave_approval.repository;

import com.java.leave_approval.model.ApprovalStep;
import com.java.leave_approval.model.LeaveRequest;
import com.java.leave_approval.model.User;
import com.java.leave_approval.model.ApprovalDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
    List<ApprovalStep> findByRequestOrderByStepOrder(LeaveRequest request);
    List<ApprovalStep> findByApproverAndDecision(User approver, ApprovalDecision decision);
    Optional<ApprovalStep> findByRequestAndStepOrder(LeaveRequest request, Integer stepOrder);
}
