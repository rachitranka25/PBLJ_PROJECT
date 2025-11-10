package com.auction.controller;

import com.auction.dto.LoginRequest;
import com.auction.dto.RegisterRequest;
import com.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSession(@PathVariable String sessionId) {
        var user = userService.getUserBySession(sessionId);
        if (user != null) {
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "user", userService.convertToDTO(user)
            ));
        }
        return ResponseEntity.ok(Map.of("valid", false));
    }
}




