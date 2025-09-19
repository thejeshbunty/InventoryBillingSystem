package service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static int getChoice(String prompt, int maxChoice) {
        int choice = -1;
        while (choice < 1 || choice > maxChoice) {
            System.out.print(prompt);
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (choice < 1 || choice > maxChoice) {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + maxChoice + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the invalid input
                choice = -1; // reset choice to continue loop
            }
        }
        return choice;
    }

    public static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Admin Menu");
        System.out.println("2. Cashier Menu");
        System.out.println("3. Exit");
    }

    public static void displayAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. View All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Go Back");
        System.out.println("6. Exit");
    }
    
    public static void displayCashierMenu() {
        System.out.println("\n--- Cashier Menu ---");
        System.out.println("1. Create New Bill");
        System.out.println("2. View My Bills");
        System.out.println("3. View Bills by Date Range");
        System.out.println("4. Go Back");
        System.out.println("5. Exit");
    }
}