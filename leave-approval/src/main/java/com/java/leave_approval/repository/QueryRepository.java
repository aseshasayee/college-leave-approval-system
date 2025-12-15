package com.java.leave_approval.repository;

import com.java.leave_approval.model.Query;
import com.java.leave_approval.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
    List<Query> findByRequest(LeaveRequest request);
    List<Query> findByRequestAndResolved(LeaveRequest request, Boolean resolved);
}
