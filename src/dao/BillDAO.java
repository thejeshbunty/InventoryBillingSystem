
// dao/BillDAO.java
package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Bill;
import util.DBConnection;

public class BillDAO {

    // Add a new bill, returns generated bill ID
    public int addBill(Bill bill) {
        String sql = "INSERT INTO bills (user_id, total, bill_date) VALUES (?, ?, ?) RETURNING bill_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bill.getUserId());
            ps.setDouble(2, bill.getTotal());
            ps.setDate(3, Date.valueOf(LocalDate.now()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("bill_id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Find bill by ID ✅
    public Optional<Bill> findById(int billId) {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRowToBill(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Find all bills
    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY bill_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) bills.add(mapRowToBill(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }
    
    // Find bills by user ID
    public List<Bill> findByUserId(int userId) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE user_id = ? ORDER BY bill_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                bills.add(mapRowToBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    // Find bills by date range
    public List<Bill> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE bill_date BETWEEN ? AND ? ORDER BY bill_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                bills.add(mapRowToBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    private Bill mapRowToBill(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("bill_id"));
        bill.setUserId(rs.getInt("user_id"));
        bill.setTotal(rs.getDouble("total"));
        bill.setBillDate(rs.getDate("bill_date").toLocalDate());
        return bill;
    }
}
