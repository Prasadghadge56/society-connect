package com.society.controller;

import com.society.entity.User;
import com.society.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register User
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok(Map.of("message", "User registered", "user", savedUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials){
        try {
            String username = credentials.get("username"); // or "email" if your entity uses email
            String password = credentials.get("password");

            User user = userService.login(username, password);
            return ResponseEntity.ok(Map.of("message", "Login successful", "user", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id){
        try {
            User user = userService.getprofile(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // fixed typo
        }
    }

    // ✅ Get all users
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Get Admin user
    @GetMapping("/admins")
    public ResponseEntity<?> getAdminUsers() {
        try {
            return ResponseEntity.ok(userService.getAdminUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
