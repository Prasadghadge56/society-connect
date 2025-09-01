package com.society.dto;

public class PaymentStats {
    private long totalBills;
    private long paidBills;
    private long unpaidBills;
    private long overdueBills;

    public PaymentStats(long totalBills, long paidBills, long unpaidBills, long overdueBills) {
        this.totalBills = totalBills;
        this.paidBills = paidBills;
        this.unpaidBills = unpaidBills;
        this.overdueBills = overdueBills;
    }

    // Getters
    public long getTotalBills() { return totalBills; }
    public long getPaidBills() { return paidBills; }
    public long getUnpaidBills() { return unpaidBills; }
    public long getOverdueBills() { return overdueBills; }
}