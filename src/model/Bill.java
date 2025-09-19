package model;

import java.time.LocalDate;

public class Bill {
    private int billId;
    private int userId;
    private double total;
    private LocalDate billDate;

    public Bill() {}

    public Bill(int billId, int userId, double total, LocalDate billDate) {
        this.billId = billId;
        this.userId = userId;
        this.total = total;
        this.billDate = billDate;
    }

    // Getters and setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
}
