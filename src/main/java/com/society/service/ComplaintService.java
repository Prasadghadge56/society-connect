package com.society.service;

import com.society.entity.Complaint;
import com.society.entity.User;
import com.society.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    // 1. File complaint (with image already set in controller)
    public Complaint fileComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // 2. Get all complaints
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // 3. Get complaint by Id
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    // 4. Get complaints by user
    public List<Complaint> getComplaintsByUser(Long userId) {
        return complaintRepository.findByUserId(userId);
    }

    // 5. Update complaint status
    public Complaint updateComplaintStatus(Long id, String status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }

    // 6. Delete complaint
    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    // 7. Get complaints by status
    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintRepository.findByStatus(status.toUpperCase());
    }
}
