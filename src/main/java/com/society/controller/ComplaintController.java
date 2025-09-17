package com.society.controller;

import com.society.entity.Complaint;
import com.society.entity.User;
import com.society.repository.UserRepository;
import com.society.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper; // ✅ to parse JSON string into Complaint

    // 1. Create a complaint with image upload
    @PostMapping(value = "/create/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<?> createComplaint(
            @PathVariable Long userId,
            @RequestPart("complaint") String complaintJson, // JSON as String
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Convert JSON string into Complaint object
        Complaint complaint = objectMapper.readValue(complaintJson, Complaint.class);
        complaint.setUser(user);
        complaint.setStatus("PENDING");

        if (image != null && !image.isEmpty()) {
            complaint.setImage(image.getBytes());
        }

        return ResponseEntity.ok(complaintService.fileComplaint(complaint));
    }

    // 2. Get complaint by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getComplaintById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    // 3. Get complaints by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getComplaintsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(complaintService.getComplaintsByUser(userId));
    }

    // 4. Get all complaints (Admin use)
    @GetMapping
    public ResponseEntity<?> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    // 5. Update complaint status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(complaintService.updateComplaintStatus(id, status));
    }

    // 6. Delete complaint
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }

    // 7. ✅ Filter complaints by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getComplaintsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(complaintService.getComplaintsByStatus(status));
    }

    // 8. ✅ Fetch complaint image by complaint ID
    @GetMapping("/{id}/image")
    public ResponseEntity<?> getComplaintImage(@PathVariable Long id) {
        Complaint complaint = complaintService.getComplaintById(id);

        if (complaint.getImage() == null) {
            return ResponseEntity.badRequest().body("No image found for this complaint");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=image_" + id + ".jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(complaint.getImage());
    }
}
