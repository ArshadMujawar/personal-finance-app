package com.personalfinance.app.config.controller;

import com.personalfinance.app.dto.AuthRequest;
import com.personalfinance.app.dto.AuthResponse;
import com.personalfinance.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        AuthResponse response = authService.register(request);
        
        if (response.getMessage() != null) {
            // Error occurred
            return ResponseEntity.badRequest().body(response);
        }
        
        // Success
        return ResponseEntity.status(201).body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        
        if (response.getMessage() != null) {
            // Error occurred
            return ResponseEntity.status(401).body(response);
        }
        
        // Success
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth API is working!");
    }
} 