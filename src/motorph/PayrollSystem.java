package motorph;

import java.io.*;
import java.util.*;

public class PayrollSystem {
    static Map<String, String[]> employeeMap = new HashMap<>();
    static String loggedInID = "";

    public static void main(String[] args) {
        loadEmployeeData();
        
        Scanner sc = new Scanner(System.in);
        System.out.println("==============================================");
        System.out.println("        MOTORPH SELF-SERVICE PORTAL v5.0       ");
        System.out.println("==============================================\n");

        System.out.print("👤 Employee ID: ");
        String id = sc.nextLine();
        System.out.print("🔑 PIN: ");
        String pin = sc.nextLine();

        if (authenticate(id, pin)) {
            loggedInID = id;
            System.out.println("\n✅ Login Successful!");
            showDashboard();
        } else {
            System.out.println("\n❌ Access Denied: Incorrect ID or PIN.");
        }
    }

    public static void loadEmployeeData() {
        // This handles both Root and sub-folder locations
        File file = new File("EmployeeDetails.csv");
        if (!file.exists()) file = new File("..", "EmployeeDetails.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 20) {
                    employeeMap.put(data[0].trim(), data);
                }
            }
            System.out.println("✅ Loaded " + employeeMap.size() + " employees.");
        } catch (Exception e) {
            System.out.println("[!] ERROR: EmployeeDetails.csv not found.");
        }
    }

    public static boolean authenticate(String id, String pin) {
        if (!employeeMap.containsKey(id)) return false;
        return employeeMap.get(id)[19].trim().equals(pin);
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
                System.out.println("\n--- PROFILE ---");
                System.out.println("ID: " + emp[0] + " | Position: " + emp[11]);
                System.out.println("Basic Salary: P" + emp[13]);
            } else if (choice.equals("2")) {
                calculatePayslip(emp);
            } else if (choice.equals("3")) break;
        }
    }

    public static void calculatePayslip(String[] emp) {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Month (e.g., 03): ");
        String month = sc.nextLine();
        
        double hourlyRate = Double.parseDouble(emp[18].trim());
        double totalHours = 0;
        double totalLateMins = 0;

        File attFile = new File("AttendanceRecords.csv");
        if (!attFile.exists()) attFile = new File("..", "AttendanceRecords.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] att = line.split(",");
                if (att[0].trim().equals(loggedInID) && att[1].trim().startsWith(month)) {
                    String[] in = att[2].trim().split(":");
                    String[] out = att[3].trim().split(":");
                    double hIn = Double.parseDouble(in[0]), mIn = Double.parseDouble(in[1]);
                    double hOut = Double.parseDouble(out[0]), mOut = Double.parseDouble(out[1]);

                    if (hIn > 8 || (hIn == 8 && mIn > 0)) totalLateMins += ((hIn - 8) * 60) + mIn;
                    double dayHrs = (hOut + mOut/60) - (hIn + mIn/60);
                    totalHours += (dayHrs > 5) ? dayHrs - 1 : dayHrs;
                }
            }
            
            double gross = (hourlyRate * totalHours) - ((hourlyRate / 60) * totalLateMins);
            double sss = gross * 0.045, phil = gross * 0.02, tax = (gross > 20000) ? (gross - 20000) * 0.2 : 0;
            double net = gross - (sss + phil + tax + 100);

            System.out.println("\n--- MARCH PAYSLIP ---");
            System.out.println("Gross Pay:     P " + String.format("%.2f", gross));
            System.out.println("Deductions:    P " + String.format("%.2f", (sss+phil+tax+100)));
            System.out.println("NET PAY:       P " + String.format("%.2f", net));
        } catch (Exception e) { System.out.println("[!] Attendance error."); }
    }
}
