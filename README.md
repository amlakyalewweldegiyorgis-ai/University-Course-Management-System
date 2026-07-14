# University Course Management System

A robust, console-based Java application designed to manage student registrations, course enrollments, prerequisites, and payment processing with secure data validations.

## 🚀 Key Features
* **Student Management:** Secure registration with regex-validated names, gender checks, and unique ID verification.
* **Course Administration:** Add, update, and remove courses alongside structured prerequisite chain validation.
* **Payment Processing:** Secure transaction tracking with strict prefix (`GORP-`) and duplication validation guards.
* **Resilient Architecture:** Complete input buffer protection to prevent runtime skipping and crash loops.

## 🛠️ Execution & Deployment

### 1. Compilation
Compile all source `.java` files from your root terminal directory:
```bash
javac *.java