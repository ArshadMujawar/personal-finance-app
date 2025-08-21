package com.personalfinance.app.service;

import com.personalfinance.app.dto.AuthRequest;
import com.personalfinance.app.dto.AuthResponse;
import com.personalfinance.app.model.User;
import com.personalfinance.app.repository.UserRepository;
import com.personalfinance.app.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthResponse register(AuthRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse("Username already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER"); // Default role
        
        // Save user to database
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole());
        
        return new AuthResponse(token, savedUser);
    }
    
    public AuthResponse login(AuthRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        
        // Check if user exists and password matches
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid username or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        
        return new AuthResponse(token, user);
    }
} 