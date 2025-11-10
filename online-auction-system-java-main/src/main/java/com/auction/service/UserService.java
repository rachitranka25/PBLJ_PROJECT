package com.auction.service;

import com.auction.dto.LoginRequest;
import com.auction.dto.RegisterRequest;
import com.auction.dto.UserDTO;
import com.auction.model.User;
import com.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Simple in-memory session management (for demo purposes)
    private Map<String, User> sessions = new HashMap<>();

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        if (userRepository.existsByUsername(request.getUsername())) {
            response.put("success", false);
            response.put("message", "Username already exists");
            return response;
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return response;
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // In production, hash this!
        user.setFullName(request.getFullName());
        
        user = userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "Registration successful");
        response.put("user", convertToDTO(user));
        return response;
    }

    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return response;
        }
        
        User user = userOpt.get();
        String sessionId = generateSessionId();
        sessions.put(sessionId, user);
        
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("sessionId", sessionId);
        response.put("user", convertToDTO(user));
        return response;
    }

    public User getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    public UserDTO convertToDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFullName());
    }

    public User getUserById(Long id) {
        if (id == null) return null;
        return userRepository.findById(id).orElse(null);
    }

    private String generateSessionId() {
        return "session_" + System.currentTimeMillis() + "_" + Math.random();
    }
}

