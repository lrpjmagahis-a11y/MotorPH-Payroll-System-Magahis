package motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PayrollSystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("======================================");
        System.out.println("   MOTORPH PAYROLL SYSTEM LOGIN");
        System.out.println("======================================");
        
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        
        System.out.print("Enter 5-digit Password: ");
        String password = input.nextLine();
        
        if (password.equals("12345")) {
            showMenu(username, input);
        } else {
            System.out.println("\n[!] Invalid Credentials. Access Denied.");
        }
    }

    public static void showMenu(String user, Scanner input) {
        int choice = 0;
        while (choice != 2) {
            System.out.println("\n--- Welcome, " + user + " ---");
            System.out.println("1. Search Employee by ID (from 100 Records)");
            System.out.println("2. Exit");
            System.out.print("\nSelect an option: ");
            choice = input.nextInt();

            if (choice == 1) {
                System.out.print("Enter Employee ID (e.g., 10001): ");
                String searchId = input.next();
                searchEmployee(searchId);
            }
        }
        System.out.println("System Logged Out.");
    }

    public static void searchEmployee(String id) {
        String csvFile = "data/Employee_Data.csv"; 
        String line = "";
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] employee = line.split(","); 
                if (employee[0].equals(id)) {
                    System.out.println("\n[Match Found!]");
                    System.out.println("ID: " + employee[0]);
                    System.out.println("Name: " + employee[2] + " " + employee[1]);
                    System.out.println("Position: " + employee[3]);
                    System.out.println("Basic Salary: P" + employee[4]);
                    found = true;
                    break;
                }
            }
            if (!found) System.out.println("\n[!] Employee ID " + id + " not found.");
        } catch (IOException e) {
            System.out.println("[!] System Error: Cannot find data/Employee_Data.csv");
        }
    }
}
