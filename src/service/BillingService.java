// service/BillingService.java
package service;

import dao.BillDAO;
import dao.BillItemDAO;
import dao.ProductDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Bill;
import model.BillItem;
import model.Product;

public class BillingService {
    private BillDAO billDAO = new BillDAO();
    private BillItemDAO billItemDAO = new BillItemDAO();
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Creates a new bill and saves its items.
     * Decrements product stock for each item.
     *
     * @param userId
     * @param items
     * @return The ID of the newly created bill.
     */
    public int createBill(int userId, List<BillItem> items) {
        double total = items.stream().mapToDouble(BillItem::getItemPrice).sum();
        Bill bill = new Bill(0, userId, total, LocalDate.now());
        int billId = billDAO.addBill(bill);

        if (billId > 0) {
            for (BillItem item : items) {
                item.setBillId(billId);
                boolean saved = billItemDAO.addBillItem(item); // returns true if inserted
                if (saved) {
                    productDAO.decrementStock(item.getProductId(), item.getQuantity());
                }
            }
        }
        return billId;
    }

    public List<Product> getLowStockAfterBill(List<BillItem> items) {
        List<Product> lowStockProducts = new ArrayList<>();
        for (BillItem item : items) {
            productDAO.getProductById(item.getProductId()).ifPresent(p -> {
                if (p.getQuantity() <= p.getLowStockThreshold()) {
                    lowStockProducts.add(p);
                }
            });
        }
        return lowStockProducts;
    }
}