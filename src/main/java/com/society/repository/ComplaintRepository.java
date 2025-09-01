package com.society.repository;

import com.society.entity.Complaint;
import com.society.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findComplaintByUser(User user);
    List<Complaint> findByStatus(String status);
    List<Complaint> findByUser(User user);
    List<Complaint> findByUserId(Long userId);

}
