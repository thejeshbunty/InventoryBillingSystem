// service/ReportService.java
package service;

import dao.BillDAO;
import dao.UserDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import model.Bill;
import model.User;

public class ReportService {
    private BillDAO billDAO = new BillDAO();
    private UserDAO userDAO = new UserDAO();

    // Generate billing report for a specific user
    public void generateBillingReport(int userId) {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("User not found.");
            return;
        }
        User user = userOpt.get();
        List<Bill> bills = billDAO.findByUserId(userId);

        System.out.println("\n--- Billing Report for User " + user.getUserId() + " (" + user.getUsername() + ") ---");
        if (bills.isEmpty()) {
            System.out.println("No bills found.");
        } else {
            for (Bill bill : bills) {
                System.out.println("Bill ID: " + bill.getBillId() + ", Date: " + bill.getBillDate()
                        + ", Total: " + bill.getTotal());
            }
        }
    }

    // Generate billing report within a date range
    public void generateBillingReport(LocalDate startDate, LocalDate endDate) {
        List<Bill> bills = billDAO.findByDateRange(startDate, endDate);

        System.out.println("\n--- Billing Report from " + startDate + " to " + endDate + " ---");
        if (bills.isEmpty()) {
            System.out.println("No bills found in this range.");
        } else {
            for (Bill bill : bills) {
                Optional<User> userOpt = userDAO.findById(bill.getUserId());
                String username = userOpt.map(User::getUsername).orElse("Unknown");
                System.out.println("Bill ID: " + bill.getBillId() + ", User: " + username
                        + ", Date: " + bill.getBillDate() + ", Total: " + bill.getTotal());
            }
        }
    }

    // Get all bills (for admin view)
    public List<Bill> getAllBills() {
        return billDAO.findAll();
    }

    // View specific bill details by bill ID
    public void viewBillDetails(int billId) {
        Optional<Bill> billOpt = billDAO.findById(billId); // ✅ uses findById(int)
        if (billOpt.isEmpty()) {
            System.out.println("Bill not found.");
            return;
        }

        Bill bill = billOpt.get();
        System.out.println("\n--- Bill Details ---");
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("User ID: " + bill.getUserId());
        System.out.println("Date: " + bill.getBillDate());
        System.out.println("Total: " + bill.getTotal());
    }
}