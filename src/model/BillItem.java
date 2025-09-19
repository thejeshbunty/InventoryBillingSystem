package model;

public class BillItem {
    private int itemId;
    private int billId;
    private int productId;
    private int quantity;
    private double itemPrice;

    public BillItem() {}

    public BillItem(int itemId, int billId, int productId, int quantity, double itemPrice) {
        this.itemId = itemId;
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getItemPrice() { return itemPrice; }
    public void setItemPrice(double itemPrice) { this.itemPrice = itemPrice; }
}