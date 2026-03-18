package motorph;

import java.io.*;
import java.util.*;

public class PayrollSystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("======================================");
        System.out.println("   MOTORPH ENTERPRISE PAYROLL V2.0");
        System.out.println("======================================");
        
        System.out.print("Enter Username: ");
        String user = input.nextLine();
        System.out.print("Enter 5-digit Password: ");
        String pass = input.nextLine();

        // 1. Secure Login System
        if (pass.length() == 5 && (user.equals("admin") || user.equals("employee"))) {
            showMenu(user, input);
        } else {
            System.out.println("[!] Access Denied. Use a 5-digit password.");
        }
    }

    public static void showMenu(String role, Scanner input) {
        while (true) {
            System.out.println("\n--- " + role.toUpperCase() + " DASHBOARD ---");
            System.out.println("1. Process Payroll (Search & Calculate)");
            System.out.println("2. Exit");
            System.out.print("Select: ");
            int choice = input.nextInt();

            if (choice == 1) {
                System.out.print("Enter Employee ID (10001-10100): ");
                String id = input.next();
                calculatePayroll(id);
            } else break;
        }
    }

    public static void calculatePayroll(String id) {
        String file = "Employee_Data.csv"; 
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) {
                    double basicSalary = Double.parseDouble(data[4]);
                    
                    // 2. Deduction Engine Logic
                    double sss = basicSalary * 0.045; // 4.5% SSS
                    double philhealth = basicSalary * 0.02; // 2% PhilHealth
                    double pagibig = 100.0; // Fixed Pag-IBIG
                    double tax = (basicSalary > 20000) ? (basicSalary * 0.15) : 0; // Simple Tax logic
                    
                    double totalDeductions = sss + philhealth + pagibig + tax;
                    double netPay = basicSalary - totalDeductions;
                    double biMonthly = netPay / 2; // 3. Cutoff Management (1-15, 16-31)

                    System.out.println("\n[PAYROLL SUMMARY FOR: " + data[2] + " " + data[1] + "]");
                    System.out.println("--------------------------------------");
                    System.out.println("Monthly Basic: P" + basicSalary);
                    System.out.println("SSS Contribution: P" + sss);
                    System.out.println("PhilHealth: P" + philhealth);
                    System.out.println("Tax: P" + tax);
                    System.out.println("--------------------------------------");
                    System.out.println("NET MONTHLY PAY: P" + netPay);
                    System.out.println("BI-MONTHLY PAY (Cutoff): P" + biMonthly);
                    return;
                }
            }
            System.out.println("[!] ID Not Found.");
        } catch (Exception e) {
            System.out.println("[!] Error: Ensure Employee_Data.csv is in the root folder.");
        }
    }
}
