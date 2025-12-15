package com.java.leave_approval.repository;

import com.java.leave_approval.model.Student;
import com.java.leave_approval.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByRollNo(String rollNo);
    boolean existsByRollNo(String rollNo);
}
