# MotorPH Automatic Payroll System v3.1
**Course Project: IT101 - Computer Programming 1**

An advanced procedural Java payroll system tailored for MotorPH. This version features a dual-interface for Employees and Payroll Staff, allowing for detailed profile management and granular payroll reporting from June to December.

---

## 📌 Project Contributor

### Princess Jade R. Magahis – Lead Developer & Data Architect
* **System Logic:** Developed procedural algorithms for net pay, gross pay, and statutory deductions.
* **Attendance Engine:** Implemented strict 8:00 AM–5:00 PM work-hour logic with a 5-minute grace period and lunch deductions.
* **User Experience:** Designed an interactive CLI menu allowing employees to generate specific cutoff reports on-demand.

---

## 💻 Key Features

### 👤 Employee Mode
* **Basic Info:** Quick view of Name, ID, and Birthday.
* **Detailed Profile:** Access to all sensitive data (SSS, TIN, PhilHealth, Address, and Salary Rates).
* **Flexible Payroll:** * Generate a **Full Year Report** (June-December).
    * Generate a **Specific Cutoff** (Choice of Month and 1st/2nd Cutoff).

### 🔑 Payroll Staff Mode
* **Global Processing:** Generate full payroll history for any specific employee by ID.
* **Batch Processing:** Run the entire company's payroll (100+ records) in a single execution.

---

## 🛠️ Business Rules (Logic Engine)
* **Standard Shift:** 08:00 to 17:00 (Max 8 hours + 1 hour lunch).
* **Grace Period:** Logins at **08:05** or earlier are rounded to **08:00** (No late penalty).
* **Deductions:** * **PhilHealth:** 3% of Monthly Gross.
    * **Pag-IBIG (LOVE):** Fixed 100.00.
    * **Withholding Tax:** 15% (Applied on 2nd cutoff based on total monthly earnings).

---

## 📂 Project Structure
* `src/motorph/PayrollSystem.java` – Core application logic (Single File).
* `Employee Details.csv` – Employee master database.
* `Attendance Record.csv` – Attendance logs (June to December).
* `.gitignore` – Configured to exclude build artifacts and NetBeans private data.

---

## ▶️ How to Run
1. **Environment:** Ensure **JDK 11+** is installed.
2. **File Setup:** Place the two CSV files in the **Project Root** folder (same level as the `src` folder).
3. **Execution:** Open in **NetBeans** and run `PayrollSystem.java`.

### 🔐 Credentials
| User Role | Username | Password |
| :--- | :--- | :--- |
| **Employee** | `employee` | `12345` |
| **Payroll Staff** | `payroll_staff` | `12345` |

---

## 📅 Documentation
[📊 View Project Plan & Data Sheets (Google Sheets)](https://docs.google.com/spreadsheets/d/13hAkgWlgDkVVQ-ZN1YtAlJpFTIJS0ifu/edit?usp=sharing&ouid=117006809110779909774&rtpof=true&sd=true)
