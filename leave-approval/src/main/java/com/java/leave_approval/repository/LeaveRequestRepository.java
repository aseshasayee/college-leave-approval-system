package com.java.leave_approval.repository;

import com.java.leave_approval.model.LeaveRequest;
import com.java.leave_approval.model.Student;
import com.java.leave_approval.model.ApprovalDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStudent(Student student);
    List<LeaveRequest> findByStudentOrderByCreatedAtDesc(Student student);
    List<LeaveRequest> findByStatus(ApprovalDecision status);
}
