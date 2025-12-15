package com.java.leave_approval.service;

import com.java.leave_approval.model.*;
import com.java.leave_approval.repository.*;
import com.java.leave_approval.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Register a new user (any role)
     */
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        // Check if email already exists
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Validate email domain (institutional email only)
        if (!registrationDTO.getEmail().endsWith("@srmist.edu.in") || !registrationDTO.getEmail().endsWith("@gmail.com")) {
            throw new RuntimeException("Only institutional email (@srmist.edu.in) is allowed");
        }
        
        // Create user
        User user = new User(
            registrationDTO.getName(),
            registrationDTO.getEmail(),
            registrationDTO.getRole()
        );
        user = userRepository.save(user);
        
        // If student, create student record
        Student student = null;
        if (registrationDTO.getRole() == UserRole.STUDENT) {
            // Validate student-specific fields
            if (registrationDTO.getRollNo() == null || registrationDTO.getDepartment() == null) {
                throw new RuntimeException("Roll number and department are required for students");
            }
            
            // Check if roll number already exists
            if (studentRepository.existsByRollNo(registrationDTO.getRollNo())) {
                throw new RuntimeException("Roll number already exists");
            }
            
            student = new Student(
                user,
                registrationDTO.getRollNo(),
                registrationDTO.getDepartment(),
                registrationDTO.getYear(),
                registrationDTO.getSection(),
                registrationDTO.getSemester()
            );
            student = studentRepository.save(student);
        }
        
        // Build response
        return buildUserResponse(user, student);
    }
    
    /**
     * Login user (simple email-based for prototype)
     */
    public UserResponseDTO loginUser(UserLoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        Student student = null;
        
        // If student, get student record
        if (user.getRole() == UserRole.STUDENT) {
            student = studentRepository.findByUser(user)
                .orElse(null);
        }
        
        return buildUserResponse(user, student);
    }
    
    /**
     * Get user by ID
     */
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Student student = null;
        if (user.getRole() == UserRole.STUDENT) {
            student = studentRepository.findByUser(user).orElse(null);
        }
        
        return buildUserResponse(user, student);
    }
    
    /**
     * Build user response DTO
     */
    private UserResponseDTO buildUserResponse(User user, Student student) {
        UserResponseDTO response = new UserResponseDTO();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        
        if (student != null) {
            response.setStudentId(student.getId());
            response.setRollNo(student.getRollNo());
            response.setDepartment(student.getDepartment());
            response.setYear(student.getYear());
            response.setSection(student.getSection());
            response.setSemester(student.getSemester());
        }
        
        return response;
    }
}
