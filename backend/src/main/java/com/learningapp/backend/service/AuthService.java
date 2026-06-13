package com.learningapp.backend.service;

import com.learningapp.backend.dto.AuthResponse;
import com.learningapp.backend.dto.LoginRequest;
import com.learningapp.backend.dto.RegisterRequest;
import com.learningapp.backend.model.User;
import com.learningapp.backend.repository.UserRepository;
import com.learningapp.backend.security.JwtUtil;
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

    // REGISTER
    public AuthResponse register(RegisterRequest request) {

        // Check 1: Username already exists?
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        // Check 2: Email already exists?
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        // Naya user banao
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Password encrypt karo
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        // Database mein save karo
        userRepository.save(user);

        return new AuthResponse(null, user.getUsername(), "Registration successful!");
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {

        // User dhundo
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Password match karo
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Token generate karo
        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token, user.getUsername(), "Login successful!");
    }
}