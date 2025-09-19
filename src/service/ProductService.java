package service;

import dao.ProductDAO;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import model.Product;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Optional<Product> getProductById(int id) {
        return productDAO.findById(id);
    }

    public boolean addProduct(Product p) {
        return productDAO.add(p);
    }

    public boolean updateProduct(Product p) {
        return productDAO.update(p);
    }

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    public boolean decrementStock(int productId, int qty) {
        return productDAO.decrementStock(productId, qty);
    }

    public List<Product> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }

    public void exportLowStockCSV(String path) {
        List<Product> lowStock = getLowStockProducts();
        if (lowStock.isEmpty()) {
            System.out.println("No low stock products to export.");
            return;
        }

        try (FileWriter writer = new FileWriter(path)) {
            writer.append("Product ID,Name,Price,Quantity,LowStockThreshold\n");
            for (Product p : lowStock) {
                writer.append(p.getProductId() + "," + p.getProductName() + "," +
                              p.getPrice() + "," + p.getQuantity() + "," +
                              p.getLowStockThreshold() + "\n");
            }
            System.out.println("Low stock CSV exported to: " + path);
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }
}