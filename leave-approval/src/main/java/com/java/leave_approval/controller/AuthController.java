package com.java.leave_approval.controller;

import com.java.leave_approval.dto.*;
import com.java.leave_approval.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Register a new user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserResponseDTO response = userService.registerUser(registrationDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Login user (simple email-based for prototype)
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        try {
            UserResponseDTO response = userService.loginUser(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Get user details by ID
     * GET /api/auth/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserResponseDTO response = userService.getUserById(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
