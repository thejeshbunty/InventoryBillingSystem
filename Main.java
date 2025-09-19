import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.BillItem;
import model.Product;
import model.User;
import service.AuthService;
import service.BillingService;
import service.ProductService;
import service.ReportService;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final ProductService productService = new ProductService();
    private static final BillingService billingService = new BillingService();
    private static final ReportService reportService = new ReportService();
    private static User currentUser;

    private static final String RED = "\u001b[31m";
    private static final String RESET = "\u001b[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        System.out.println("=== Inventory & Billing System ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> userOpt = authService.authenticate(username, password);
        if (userOpt.isEmpty()) {
            System.out.println("Invalid login. Exiting...");
            System.exit(0);
        } else {
            currentUser = userOpt.get();
            System.out.println("Login successful! (" + currentUser.getRole() + ")");
            mainMenu();
        }
    }

    private static void mainMenu() {
        boolean isAdmin = currentUser.getRole().equalsIgnoreCase("admin");

        while (true) {
            System.out.println("\n" + CYAN + "=== Main Menu (" + currentUser.getRole().toUpperCase() + ") ===" + RESET);

            List<MenuOption> menu = new ArrayList<>();

            // Admin-only features
            if (isAdmin) {
                menu.add(new MenuOption("View All Products", Main::viewAllProducts));
                menu.add(new MenuOption("Add Product", Main::addNewProduct));
                menu.add(new MenuOption("Update Product", Main::updateProduct));
                menu.add(new MenuOption("Delete Product", Main::deleteProduct));
                menu.add(new MenuOption("View Low Stock Products", Main::viewLowStockProducts));
                menu.add(new MenuOption("Export Low Stock CSV", Main::exportLowStockCSV));
            }

            // Shared features
            menu.add(new MenuOption("Create New Bill", Main::createNewBill));
            menu.add(new MenuOption("View My Bills", Main::viewMyBills));
            menu.add(new MenuOption("View Bills by Date Range", Main::viewBillsByDateRange));
            menu.add(new MenuOption("View Specific Bill", Main::viewSpecificBill));
            menu.add(new MenuOption("Change Credentials", () -> changeCredentials(currentUser)));
            menu.add(new MenuOption("Exit", () -> System.exit(0)));

            // Display menu
            for (int i = 0; i < menu.size(); i++) {
                MenuOption option = menu.get(i);
                String prefix = (isAdmin && i < 6) ? YELLOW : ""; // Highlight admin-only features
                System.out.printf("%d. %s%s%s%n", i + 1, prefix, option.getName(), RESET);
            }

            // Get user choice
            System.out.print("Choice: ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > menu.size()) {
                    System.out.println("Enter a valid number.");
                } else {
                    menu.get(choice - 1).getAction().run();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static class MenuOption {
        private final String name;
        private final Runnable action;

        public MenuOption(String name, Runnable action) {
            this.name = name;
            this.action = action;
        }

        public String getName() { return name; }
        public Runnable getAction() { return action; }
    }

    private static void changeCredentials(User user) {
        System.out.print("Current password: ");
        String currentPwd = scanner.nextLine();
        if (!authService.verifyPassword(user, currentPwd)) {
            System.out.println("❌ Incorrect password.");
            return;
        }

        System.out.print("New username (Enter to keep): ");
        String newUsername = scanner.nextLine().trim();
        if (!newUsername.isEmpty() && authService.changeUsername(user, newUsername)) {
            user.setUsername(newUsername);
            System.out.println("✅ Username updated.");
        }

        System.out.print("New password (Enter to keep): ");
        String newPwd = scanner.nextLine().trim();
        if (!newPwd.isEmpty() && authService.changePassword(user, newPwd)) {
            System.out.println("✅ Password updated.");
        }
    }

    // --- Product Methods ---
    private static void viewAllProducts() {
        List<Product> products = productService.getAllProducts();
        System.out.println("\n--- All Products ---");
        if (products.isEmpty()) {
            System.out.println("No products.");
            return;
        }
        System.out.printf("%-5s %-20s %-10s %-10s %-15s%n", "ID", "Name", "Price", "Qty", "Status");
        for (Product p : products) {
            String status = p.getQuantity() <= p.getLowStockThreshold() ? RED + "⚠ LOW STOCK" + RESET : "";
            System.out.printf("%-5d %-20s %-10.2f %-10d %-15s%n", p.getProductId(), p.getProductName(), p.getPrice(), p.getQuantity(), status);
        }
    }

    private static void addNewProduct() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        double price = 0.0;
        int qty = 0, lowStock = 0;

        while (true) {
            try {
                System.out.print("Price: ");
                price = Double.parseDouble(scanner.nextLine());
                if (price <= 0) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Enter valid positive number.");
            }
        }

        while (true) {
            try {
                System.out.print("Qty: ");
                qty = Integer.parseInt(scanner.nextLine());
                if (qty < 0) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Enter valid non-negative number.");
            }
        }

        while (true) {
            try {
                System.out.print("Low stock threshold: ");
                lowStock = Integer.parseInt(scanner.nextLine());
                if (lowStock < 0) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Enter valid non-negative number.");
            }
        }

        productService.addProduct(new Product(0, name, price, qty, lowStock));
        System.out.println("✅ Product added.");
    }

    private static void updateProduct() {
        System.out.print("Product ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        productService.getProductById(id).ifPresentOrElse(p -> {
            System.out.print("New name (" + p.getProductName() + "): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) p.setProductName(name);

            System.out.print("New price (" + p.getPrice() + "): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) p.setPrice(Double.parseDouble(priceStr));

            System.out.print("New qty (" + p.getQuantity() + "): ");
            String qtyStr = scanner.nextLine();
            if (!qtyStr.isEmpty()) p.setQuantity(Integer.parseInt(qtyStr));

            System.out.print("New low stock (" + p.getLowStockThreshold() + "): ");
            String lowStr = scanner.nextLine();
            if (!lowStr.isEmpty()) p.setLowStockThreshold(Integer.parseInt(lowStr));

            productService.updateProduct(p);
            System.out.println("✅ Product updated.");
        }, () -> System.out.println("Product not found."));
    }

    private static void deleteProduct() {
        System.out.print("Product ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        productService.getProductById(id).ifPresentOrElse(p -> {
            System.out.print("Confirm delete? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                productService.deleteProduct(id);
                System.out.println("✅ Deleted.");
            }
        }, () -> System.out.println("Product not found."));
    }

    private static void viewLowStockProducts() {
        List<Product> products = productService.getAllProducts();
        boolean hasLow = false;
        System.out.printf("%-5s %-20s %-10s %-10s %-15s%n", "ID", "Name", "Price", "Qty", "Status");
        for (Product p : products) {
            if (p.getQuantity() <= p.getLowStockThreshold()) {
                hasLow = true;
                System.out.printf("%-5d %-20s %-10.2f %-10d %-15s%n", p.getProductId(), p.getProductName(), p.getPrice(), p.getQuantity(), RED + "⚠ LOW STOCK" + RESET);
            }
        }
        if (!hasLow) System.out.println("No low stock products.");
    }

    private static void exportLowStockCSV() {
        System.out.print("CSV file path: ");
        String path = scanner.nextLine();
        productService.exportLowStockCSV(path);
        System.out.println("✅ Exported to " + path);
    }

    // --- Billing Methods ---
    private static void createNewBill() {
        List<BillItem> items = new ArrayList<>();
        Map<Integer, Integer> qtyMap = new HashMap<>();
        System.out.println("\n--- Create New Bill ---");
        String addMore = "yes";

        while (addMore.equalsIgnoreCase("yes")) {
            int productId = -1, qty = -1;

            while (productId == -1) {
                try {
                    System.out.print("Product ID: ");
                    productId = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) { System.out.println("Enter valid number."); }
            }

            while (qty == -1) {
                try {
                    System.out.print("Quantity: ");
                    qty = Integer.parseInt(scanner.nextLine());
                    if (qty <= 0) { System.out.println("Qty must be positive."); qty = -1; }
                } catch (Exception e) { System.out.println("Enter valid number."); }
            }

            Optional<Product> opt = productService.getProductById(productId);
            if (opt.isPresent()) {
                Product p = opt.get();
                int prev = qtyMap.getOrDefault(productId, 0);
                if (p.getQuantity() < prev + qty) {
                    System.out.println("Not enough stock. Remaining: " + (p.getQuantity() - prev));
                    continue;
                }
                double price = p.getPrice() * qty;
                boolean updated = false;
                for (BillItem bi : items) {
                    if (bi.getProductId() == productId) {
                        bi.setQuantity(bi.getQuantity() + qty);
                        bi.setItemPrice(bi.getItemPrice() + price);
                        updated = true;
                        break;
                    }
                }
                if (!updated) items.add(new BillItem(0, 0, productId, qty, price));
                qtyMap.put(productId, prev + qty);

                double totalSoFar = items.stream().mapToDouble(BillItem::getItemPrice).sum();
                System.out.println("Item added. Total so far: " + totalSoFar);
                System.out.println("Remaining stock: " + (p.getQuantity() - qtyMap.get(productId)));
            } else {
                System.out.println("Product not found.");
            }

            System.out.print("Add another item? (yes/no): ");
            addMore = scanner.nextLine();
        }

        if (!items.isEmpty()) {
            System.out.print("Confirm bill creation? (yes/no): ");
            if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                System.out.println("Cancelled.");
                return;
            }
            int billId = billingService.createBill(currentUser.getUserId(), items);
            List<Product> lowStock = billingService.getLowStockAfterBill(items);
            if (!lowStock.isEmpty()) {
                System.out.println("\n" + RED + "⚠️ Low Stock Alert!" + RESET);
                for (Product p : lowStock)
                    System.out.printf(" - %s (Remaining: %d, Min: %d)%n", p.getProductName(), p.getQuantity(), p.getLowStockThreshold());
            }
            System.out.println("✅ Bill created! ID: " + billId);
        } else {
            System.out.println("No items added.");
        }
    }

    private static void viewMyBills() { reportService.generateBillingReport(currentUser.getUserId()); }

    private static void viewBillsByDateRange() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = null, end = null;
        while (start == null) {
            try { System.out.print("Start date (yyyy-MM-dd): "); start = LocalDate.parse(scanner.nextLine(), fmt); }
            catch (Exception e) { System.out.println("Invalid format."); }
        }
        while (end == null) {
            try { System.out.print("End date (yyyy-MM-dd): "); end = LocalDate.parse(scanner.nextLine(), fmt); }
            catch (Exception e) { System.out.println("Invalid format."); }
        }
        reportService.generateBillingReport(start, end);
    }

    private static void viewSpecificBill() {
        System.out.print("Bill ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        reportService.viewBillDetails(id);
    }
}
