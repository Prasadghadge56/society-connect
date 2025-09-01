package com.society.controller;

import com.society.entity.Complaint;
import com.society.entity.User;
import com.society.repository.ComplaintRepository;
import com.society.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Create a complaint
    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createComplaint(@PathVariable Long userId, @RequestBody Complaint complaint) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        complaint.setUser(user);
        complaint.setStatus("PENDING");
        return ResponseEntity.ok(complaintRepository.save(complaint));
    }

    // 2. Get complaint by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getComplaintById(@PathVariable Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        return ResponseEntity.ok(complaint);
    }

    // 3. Get complaints by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getComplaintsByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Complaint> complaints = complaintRepository.findByUser(user);
        return ResponseEntity.ok(complaints);
    }

    // 4. Get all complaints (Admin use)
    @GetMapping
    public ResponseEntity<?> getAllComplaints() {
        return ResponseEntity.ok(complaintRepository.findAll());
    }

    // 5. Update complaint status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long id, @RequestParam String status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status.toUpperCase()); // e.g., PENDING, IN_PROGRESS, RESOLVED
        return ResponseEntity.ok(complaintRepository.save(complaint));
    }

    // 6. Delete complaint
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComplaint(@PathVariable Long id) {
        complaintRepository.deleteById(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }

    // 7. Filter complaints by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getComplaintsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(complaintRepository.findByStatus(status.toUpperCase()));
    }
}
