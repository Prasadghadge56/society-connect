package com.society.service;

import com.society.dto.PaymentStats;
import com.society.entity.MaintenanceBill;
import com.society.entity.User;
import com.society.enums.Role;
import com.society.repository.MaintenanceBillRepository;
import com.society.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceBillRepository maintenanceBillRepository;

    @Autowired
    private UserRepository userRepository;

    // generate bills for all users for specific month
    public List<MaintenanceBill> generateMonthlyBills(String month, Double amount, String description) {
        List<User> residents = userRepository.findAll().stream()
                .filter(user -> "RESIDENT".equals(user.getRole())).toList();

        List<MaintenanceBill> bills = new ArrayList<>();

        for (User resident : residents) {
            // check if bill already exists for this month
            List<MaintenanceBill> existing = maintenanceBillRepository.findByUserAndMonth(resident, month);
            if (existing.isEmpty()) {
                MaintenanceBill bill = new MaintenanceBill(
                        resident, amount, month, "UNPAID", LocalDate.now().plusDays(15), description
                );
                bills.add(maintenanceBillRepository.save(bill));
            }
        }
        return bills;
    }

    // get bills for specific user
    public List<MaintenanceBill> getBillsByUserId(Long userId) {
        return maintenanceBillRepository.findByUserId(userId);
    }

    // Mark bill as paid with payment details (KEY FEATURE FOR YOUR REQUIREMENT)
    public MaintenanceBill markBillAsPaid(Long billId, String paymentMethod, String transactionId, String remarks) {
        MaintenanceBill bill = maintenanceBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setStatus("PAID");
        bill.setPaidDate(LocalDate.now());
        bill.setPaymentMethod(paymentMethod);
        bill.setTransactionId(transactionId);
        bill.setRemarks(remarks);

        return maintenanceBillRepository.save(bill);
    }

    // get all bills
    public List<MaintenanceBill> getAllBills() {
        return maintenanceBillRepository.findAll();
    }

    // get bills by status
    public List<MaintenanceBill> getBillsByStatus(String status) {
        return maintenanceBillRepository.findByStatus(status);
    }

    // get bills for current month
    public List<MaintenanceBill> getCurrentMonthBills() {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return maintenanceBillRepository.findByMonth(currentMonth);
    }

    // Get payment statistics
    public PaymentStats getPaymentStats() {
        long totalBills = maintenanceBillRepository.count();
        long paidBills = maintenanceBillRepository.countByStatus("PAID");
        long unpaidBills = maintenanceBillRepository.countByStatus("UNPAID");
        long overdueBills = maintenanceBillRepository.countByStatus("OVERDUE");

        return new PaymentStats(totalBills, paidBills, unpaidBills, overdueBills);
    }

    // Get bill by ID (useful for payment)
    public MaintenanceBill getBillById(Long billId) {
        return maintenanceBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }
    // NEW METHOD: Generate bill for single resident (Main feature)
    public MaintenanceBill generateBillForSingleResident(Long userId, String month, Double amount, String description) {
        System.out.println("DEBUG: Starting generateBillForSingleResident - UserId: " + userId + ", Month: " + month);

        // Step 1: Get the user and validate they exist
        User resident = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        System.out.println("DEBUG: Found user - Name: " + resident.getName() + ", Role: " + resident.getRole());

        // Step 2: Validate user is a resident
        if (!Role.RESIDENT.equals(resident.getRole())) {
            throw new RuntimeException("Bills can only be generated for residents. User " + resident.getName() + " has role: " + resident.getRole());
        }

        // Step 3: Validate user is active
        if (!resident.getIsActive()) {
            throw new RuntimeException("Cannot generate bill for inactive user: " + resident.getName());
        }

        // Step 4: Check if bill already exists for this user and month
        boolean billExists = maintenanceBillRepository.existsByUserIdAndMonth(userId, month);
        if (billExists) {
            throw new RuntimeException("Bill already exists for user " + resident.getName() + " for month " + month);
        }

        // Step 6: Validate amount
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        // Step 7: Create new bill
        LocalDate dueDate = LocalDate.now().plusDays(15); // 15 days from today
        MaintenanceBill bill = new MaintenanceBill(
                resident, amount, month, "UNPAID", dueDate, description
        );

        // Step 8: Save and return
        MaintenanceBill savedBill = maintenanceBillRepository.save(bill);
        System.out.println("DEBUG: Bill created successfully - ID: " + savedBill.getId());

        return savedBill;
    }
}
