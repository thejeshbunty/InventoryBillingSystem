package service;

import dao.BillItemDAO;
import java.util.List;
import model.BillItem;

public class BillItemService {
   
    private BillItemDAO billItemDAO = new BillItemDAO();

    // Save all items for a bill
    public void saveBillItems(int billId, List<BillItem> items) {
        for (BillItem item : items) {
            item.setBillId(billId);
            billItemDAO.saveBillItem(item); // was save()
        }
    }

    public List<BillItem> getItemsByBillId(int billId) {
        return billItemDAO.findByBillId(billId);
    }

    public void deleteItemsByBillId(int billId) {
        billItemDAO.deleteByBillId(billId);
    }

    // Inside BillingService.java

// ... (other imports and code) ...

    public List<BillItem> getBillItemsByBillId(int billId) {
    // This will call a new method in your DAO layer
        return billItemDAO.findByBillId(billId);
    }
}
