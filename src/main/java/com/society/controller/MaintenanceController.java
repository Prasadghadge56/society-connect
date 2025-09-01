package com.society.controller;

import com.society.entity.MaintenanceBill;
import com.society.service.AuthorizationService;
import com.society.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")  // Fixed typo: "maintence" -> "maintenance"
@CrossOrigin(origins = "*")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private AuthorizationService authService;

    // NEW ENDPOINT: Generate bill for single resident (Admin only)
    @PostMapping("/generate/single")
    public ResponseEntity<?> generateBillForSingleResident(@RequestHeader("User-Id") Long adminId,
                                                           @RequestBody Map<String, Object> request) {
        try {
            // Check if admin can generate bills
            if (!authService.canGenerateBill(adminId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only admin can generate bills")
                );
            }

            // Extract request parameters
            Long residentId = Long.valueOf(request.get("residentId").toString());
            String month = (String) request.get("month");
            Double amount = Double.valueOf(request.get("amount").toString());
            String description = (String) request.get("description");

            System.out.println("DEBUG: Generating single bill - ResidentId: " + residentId +
                    ", Month: " + month + ", Amount: " + amount);

            // Generate bill for single resident
            MaintenanceBill bill = maintenanceService.generateBillForSingleResident(
                    residentId, month, amount, description
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Bill generated successfully for resident",
                    "residentName", bill.getUser().getName(),
                    "flatNumber", bill.getUser().getFlatNumber(),
                    "bill", bill
            ));

        } catch (Exception e) {
            System.out.println("DEBUG: Error generating single bill: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    // Generate bills for all residents (Admin only)
    @PostMapping("/generate")
    public ResponseEntity<?> generateBills(@RequestHeader("User-Id") Long userId,
                                           @RequestBody Map<String, Object> request) {
        try {
            if (!authService.canGenerateBill(userId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only admin can generate bills")
                );
            }

            String month = (String) request.get("month");
            Double amount = Double.valueOf(request.get("amount").toString());
            String description = (String) request.get("description");

            List<MaintenanceBill> bills = maintenanceService.generateMonthlyBills(month, amount, description);
            return ResponseEntity.ok(Map.of(
                    "message", "Bills generated successfully",
                    "count", bills.size(),
                    "bills", bills
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get bills for a specific user (Admin or own bills)
    @GetMapping("/user/{targetUserId}")
    public ResponseEntity<?> getBillsByUser(@RequestHeader("User-Id") Long userId,
                                            @PathVariable Long targetUserId) {
        try {
            if (!authService.canViewUserBill(userId, targetUserId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied")
                );
            }

            List<MaintenanceBill> bills = maintenanceService.getBillsByUserId(targetUserId);
            return ResponseEntity.ok(Map.of(
                    "message", "Bills retrieved successfully",
                    "bills", bills
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // MAIN FEATURE: Mark bill as paid when resident gives money (Admin only)
    @PutMapping("/pay/{billId}")
    public ResponseEntity<?> markBillAsPaid(@RequestHeader("User-Id") Long userId,
                                            @PathVariable Long billId,
                                            @RequestBody Map<String, Object> paymentDetails) {
        try {
            // Get the bill to check ownership
            MaintenanceBill bill = maintenanceService.getBillById(billId);

            if (!authService.canMarkBillAsPaid(userId, bill.getUser().getId())) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied")
                );
            }

            String paymentMethod = (String) paymentDetails.get("paymentMethod"); // "CASH", "ONLINE", "CHEQUE"
            String transactionId = (String) paymentDetails.getOrDefault("transactionId", "");
            String remarks = (String) paymentDetails.getOrDefault("remarks", "");

            MaintenanceBill updatedBill = maintenanceService.markBillAsPaid(billId, paymentMethod, transactionId, remarks);

            return ResponseEntity.ok(Map.of(
                    "message", "Bill marked as paid successfully",
                    "bill", updatedBill
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get all bills (Admin only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllBills(@RequestHeader("User-Id") Long userId) {
        try {
            if (!authService.canViewBill(userId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only admin can view all bills")
                );
            }

            List<MaintenanceBill> bills = maintenanceService.getAllBills();
            return ResponseEntity.ok(Map.of(
                    "message", "All bills retrieved successfully",
                    "bills", bills
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get bills by status (Admin only)
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBillsByStatus(@RequestHeader("User-Id") Long userId,
                                              @PathVariable String status) {
        try {
            if (!authService.canViewBill(userId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only admin can view bills by status")
                );
            }

            List<MaintenanceBill> bills = maintenanceService.getBillsByStatus(status.toUpperCase());
            return ResponseEntity.ok(Map.of(
                    "message", "Bills retrieved successfully",
                    "status", status,
                    "bills", bills
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get current month bills (Admin only)
    @GetMapping("/current-month")
    public ResponseEntity<?> getCurrentMonthBills(@RequestHeader("User-Id") Long userId) {
        try {
            if (!authService.canViewBill(userId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only admin can view current month bills")
                );
            }

            List<MaintenanceBill> bills = maintenanceService.getCurrentMonthBills();
            return ResponseEntity.ok(Map.of(
                    "message", "Current month bills retrieved successfully",
                    "bills", bills
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get payment statistics (Admin only)
    @GetMapping("/stats")
    public ResponseEntity<?> getPaymentStats(@RequestHeader("User-Id") Long userId) {
        try {
            if (!authService.canViewBill(userId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only admin can view payment statistics")
                );
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Payment statistics retrieved successfully",
                    "stats", maintenanceService.getPaymentStats()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}