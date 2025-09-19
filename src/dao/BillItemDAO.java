
// dao/BillItemDAO.java
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.BillItem;
import util.DBConnection;

public class BillItemDAO {

    // Save a new bill item ✅ returns boolean
    public boolean addBillItem(BillItem item) {
        String sql = "INSERT INTO bill_items (bill_id, product_id, quantity, item_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, item.getBillId());
            pstmt.setInt(2, item.getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getItemPrice());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // New method added to resolve the compilation error
    public boolean saveBillItem(BillItem item) {
        return addBillItem(item);
    }

    // Get all bill items for a specific bill
    public List<BillItem> findByBillId(int billId) {
        List<BillItem> items = new ArrayList<>();
        String sql = "SELECT * FROM bill_items WHERE bill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BillItem item = new BillItem();
                item.setItemId(rs.getInt("item_id"));
                item.setBillId(rs.getInt("bill_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setItemPrice(rs.getDouble("item_price"));
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // Delete bill items by bill id
    public boolean deleteByBillId(int billId) {
        String sql = "DELETE FROM bill_items WHERE bill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
