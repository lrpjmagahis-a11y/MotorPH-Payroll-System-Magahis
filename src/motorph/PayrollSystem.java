package motorph;

import java.io.*;
import java.util.*;

public class PayrollSystem {
    // Scalability: HashMap handles thousands of employees instantly
    static Map<String, String[]> employeeMap = new HashMap<>();
    static String loggedInID = "";
    static boolean isAdmin = false;

    public static void main(String[] args) {
        loadEmployeeData();
        
        Scanner sc = new Scanner(System.in);
        System.out.println("==============================================");
        System.out.println("        MOTORPH PAYROLL SYSTEM v6.0          ");
        System.out.println("==============================================\n");

        System.out.print("👤 User ID: ");
        String id = sc.nextLine().trim();
        System.out.print("🔑 PIN: ");
        String pin = sc.nextLine().trim();

        // Admin Access Feature
        if (id.equalsIgnoreCase("admin") && pin.equals("admin123")) {
            isAdmin = true;
            System.out.println("\n🛠️ ADMIN ACCESS GRANTED.");
            showAdminMenu();
        } 
        // Employee Login Feature
        else if (authenticate(id, pin)) {
            loggedInID = id;
            System.out.println("\n✅ LOGIN SUCCESSFUL!");
            showDashboard();
        } else {
            System.out.println("\n❌ ACCESS DENIED: Incorrect ID or PIN.");
        }
    }

    public static void loadEmployeeData() {
        // Look for CSV in project root
        File file = new File("EmployeeDetails.csv");
        if (!file.exists()) file = new File("..", "EmployeeDetails.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip Header
            while ((line = br.readLine()) != null) {
                // Regex handles commas inside addresses correctly
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 20) {
                    employeeMap.put(data[0].trim(), data);
                }
            }
            System.out.println("📊 System: " + employeeMap.size() + " employees loaded.");
        } catch (Exception e) {
            System.out.println("[!] ERROR: EmployeeDetails.csv not found.");
        }
    }

    public static boolean authenticate(String id, String pin) {
        if (!employeeMap.containsKey(id)) return false;
        // Index 19 is exactly the PIN column in your CSV
        return employeeMap.get(id)[19].trim().equals(pin);
    }

    public static void showAdminMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("[1] View Workforce Summary");
            System.out.println("[2] Process Full Payroll (All Staff)");
            System.out.println("[3] Logout");
            System.out.print("Selection: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.println("Total Employees: " + employeeMap.size());
            } else if (choice.equals("2")) {
                for (String[] emp : employeeMap.values()) {
                    calculatePayroll(emp, true);
                }
            } else if (choice.equals("3")) break;
        }
    }

    public static void showDashboard() {
        Scanner sc = new Scanner(System.in);
        String[] emp = employeeMap.get(loggedInID);
        while (true) {
            System.out.println("\nWELCOME, " + emp[2].toUpperCase() + " " + emp[1].toUpperCase());
            System.out.println("[1] View Profile  [2] Calculate Payslip  [3] Logout");
            System.out.print("Selection: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.println("\n--- EMPLOYEE PROFILE ---");
                System.out.println("ID: " + emp[0]);
                System.out.println("Position: " + emp[11]);
                System.out.println("SSS: " + emp[6] + " | TIN: " + emp[8]);
            } else if (choice.equals("2")) {
                calculatePayroll(emp, false);
            } else if (choice.equals("3")) break;
        }
    }

    public static void calculatePayroll(String[] emp, boolean isBatch) {
        Scanner sc = new Scanner(System.in);
        String month = "03", cutoff = "1";

        if (!isBatch) {
            System.out.print("\nEnter Month (01-12): "); month = sc.nextLine();
            System.out.print("Select Cutoff [1] 1-15  [2] 16-31: "); cutoff = sc.nextLine();
        }

        double hourlyRate = Double.parseDouble(emp[18].trim());
        double totalHours = 0, totalLateMins = 0;

        File attFile = new File("AttendanceRecords.csv");
        if (!attFile.exists()) attFile = new File("..", "AttendanceRecords.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {
            String line; br.readLine();
            while ((line = br.readLine()) != null) {
                String[] att = line.split(",");
                if (att[0].trim().equals(emp[0]) && att[1].startsWith(month)) {
                    // Bi-monthly Cutoff Logic
                    int day = Integer.parseInt(att[1].split("/")[1]);
                    if ((cutoff.equals("1") && day <= 15) || (cutoff.equals("2") && day > 15)) {
                        String[] in = att[2].split(":"), out = att[3].split(":");
                        double hIn = Double.parseDouble(in[0]), mIn = Double.parseDouble(in[1]);
                        double hOut = Double.parseDouble(out[0]), mOut = Double.parseDouble(out[1]);

                        if (hIn > 8 || (hIn == 8 && mIn > 0)) totalLateMins += ((hIn - 8) * 60) + mIn;
                        double dayHrs = (hOut + mOut/60) - (hIn + mIn/60);
                        totalHours += (dayHrs > 5) ? dayHrs - 1 : dayHrs;
                    }
                }
            }
            
            // Deduction Engine Logic
            double gross = (hourlyRate * totalHours) - ((hourlyRate / 60) * totalLateMins);
            double sss = gross * 0.045; 
            double phil = gross * 0.02;
            double pagibig = 100.00;
            double tax = (gross > 12500) ? (gross - 12500) * 0.20 : 0;
            double net = gross - (sss + phil + pagibig + tax);

            if (isBatch) {
                System.out.printf("Employee: %s | Net Pay: P%,.2f\n", emp[0], net);
            } else {
                System.out.println("\n--- PAYSLIP SUMMARY ---");
                System.out.println("Hours Worked:  " + String.format("%.2f", totalHours));
                System.out.println("Gross Salary:  P " + String.format("%,.2f", gross));
                System.out.println("Total Deductions: P " + String.format("%,.2f", (sss+phil+pagibig+tax)));
                System.out.println("NET PAY:       P " + String.format("%,.2f", net));
            }
        } catch (Exception e) { System.out.println("[!] Attendance error."); }
    }
}
