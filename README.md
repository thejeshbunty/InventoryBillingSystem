# Inventory & Billing System

![Java](https://img.shields.io/badge/Language-Java-blue) ![Build](https://github.com/thejeshbunty/InventoryBillingSystem/actions/workflows/build.yml/badge.svg) ![Version](https://img.shields.io/badge/Version-1.0-orange)

A *Java console-based Inventory & Billing System* that allows Admins and Cashiers to manage products, billing, stock levels, and reporting. The project is modular, with services for authentication, product management, billing, and reporting.


🚀 Features

🔑 Authentication & Roles

| Feature       | Description                                      |
|---------------|--------------------------------------------------|
| Roles         | Admin & Cashier roles with different privileges |
| Credentials   | Option to *change credentials*                |

 📦 Product Management (Admin only)

| Feature        | Description                                  |
|----------------|----------------------------------------------|
| View Products  | List all products in the inventory           |
| Add Product    | Add new products                              |
| Update Product | Update existing product details               |
| Delete Product | Remove products from inventory                |
| Low Stock      | View low stock products                        |
| Export CSV     | Export low stock products to CSV             |

🧾 Billing

| Feature          | Description                                  |
|------------------|----------------------------------------------|
| Create Bill      | Create new bills for customers               |
| Multiple Products| Add multiple products in one bill            |
| Stock Validation | Check stock before billing                    |
| Low Stock Alerts | Alerts after billing                          |
| View Bill        | View specific bill details                    |

 📊 Reporting

| Feature            | Description                                |
|--------------------|--------------------------------------------|
| User Bills         | View bills created by logged-in user       |
| Date Range Bills   | View all bills by date range               |
| Detailed Reports   | Generate detailed billing reports          |


 📂 Project Structure

project/ │   Main.java              # Entry point ├───src/ │   ├───model/             # Data models (User, Product, BillItem) │   ├───service/           # Business logic (Auth, Billing, Product, Reports) │   └───... ├───lib/                   # External JAR libraries (if any) ├───bin/                   # Compiled .class files └───README.md              # Documentation



 🔧 Setup & Run

1. Clone the repository:
```bash
git clone https://github.com/thejeshbunty/InventoryBillingSystem.git
cd InventoryBillingSystem

2. Compile the project:



javac -d bin -cp lib/* src//*.java Main.java

3. Run the program:



java -cp bin:lib/* Main



🔐 Default Credentials

Role	Username	Password

Admin	admin	bunty
Cashier	cashier	bunty


⚠ You can change these credentials in the application using the Change Credentials option in the menu.


---

📜 License

This project is for educational purposes only.

✅ *Only this content* should go in your README.md on GitHub.  

Do you want me to *also add actual working GitHub Actions workflow* so your build badge shows live status?
