package com.society.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_bills")
public class MaintenanceBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private Double amount;
    private String month;
    private String status; // "PAID", "UNPAID", "OVERDUE"
    private String paymentMethod; // "CASH", "ONLINE", "CHEQUE"
    private String transactionId;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String description;
    private String remarks; // Admin can add remarks when marking as paid

    // Constructors
    public MaintenanceBill() {}

    public MaintenanceBill(User user, Double amount, String month, String status, LocalDate dueDate, String description) {
        this.user = user;
        this.amount = amount;
        this.month = month;
        this.status = status;
        this.dueDate = dueDate;
        this.description = description;
    }

    // All getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
 