package com.society.service;

import com.society.entity.User;
import com.society.enums.Role;
import com.society.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;

    // check if user has admin privileges
    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getRole().equals(Role.ADMIN);

    }

    // Check if user is resident
    public boolean isResident(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getRole().equals(Role.RESIDENT);
    }

    // CHECK USER IS staff
    public boolean isStaff(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getRole().equals(Role.STAFF);

    }

    // check if user can generae bill
    public boolean canGenerateBill(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return isAdmin(userId);

    }

    // check if user can view all bills(only admin)
    public boolean canViewBill(Long userId) {
        return isAdmin(userId);
    }
    //     // Check if user can view specific user's bills (Admin or own bills)

    public boolean canViewUserBill(Long requesterId, Long targetUserId) {
        return isAdmin(requesterId) || requesterId.equals(targetUserId);
    }

    // Check if user can mark bills as paid (Admin or the bill owner)
    public boolean canMarkBillAsPaid(Long userId, Long billOwnerId) {
        return isAdmin(userId) || userId.equals(billOwnerId);
    }

    // check if user can raise complaint (Resident only)

    public boolean canRiseComplaints(Long userId) {
        return isResident(userId);
    }

    // check if user can update complaint status (Admin and staff)
    public boolean canUpdateComplaintStatus(Long userId) {
        return isAdmin(userId) || isStaff(userId);
    }

    // Check if user can post notices (Admin only)
    public boolean canPostNotices(Long userId) {
        return isAdmin(userId);
    }

    // Check if user can manage visitors (Admin or Staff)
    public boolean canManageVisitors(Long userId) {
        return isAdmin(userId) || isStaff(userId);
    }

    // get user role as string
    public String getUserRole(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getRole().toString() : "UNKNOW";
    }

    // Validate user exists and is active
    public boolean isValidUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getIsActive();
    }
}