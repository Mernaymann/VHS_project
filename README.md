# 🏥 Virtual Healthcare System (VHS 2025)

## Overview

This project implements a **Virtual Healthcare System (VirtualCare 2025)** designed to support remote healthcare services, including patient management, provider management, appointment scheduling, electronic health records (EHR), prescriptions, billing, and feedback collection.

The system is developed following **Software Architecture principles**, including:
- UML Structural Design (Class Diagram + Use Case Diagram)
- 3-Tier Architecture Design
- Java Implementation of the full system model

---

# 📌 Part 1: Software Design (UML & Architecture)

## 🎯 Objective

The goal of Part 1 is to design the **high-level architecture and structural model** of the system before implementation.

---

## 🏗️ 3-Tier Architecture

The system is designed using a 3-layer architecture:

### 1. Presentation Layer (UI Layer)
Responsible for user interaction.

Includes:
- Patient Dashboard
- Provider Dashboard
- Admin Panel
- Appointment Booking Interface
- Feedback Forms

---

### 2. Business Logic Layer (Service Layer)
Handles system operations and rules.

Includes:
- PatientService
- ProviderService
- AppointmentService
- EHRService
- BillingService
- FeedbackService

Responsibilities:
- Booking logic
- Validation rules
- Data processing
- System workflows

---

### 3. Data Layer (Persistence Layer)
Handles storage and retrieval of system data.

Includes:
- PatientRepository
- ProviderRepository
- AppointmentRepository
- EHRRepository
- BillingRepository
- FeedbackRepository

Responsibilities:
- File handling / database operations
- Data persistence
- Data retrieval and search

---

## 📊 UML Diagrams

### ✔ Use Case Diagram Includes:
- Register Patient / Provider
- Login / Authentication
- Book Appointment
- Manage Schedule
- Conduct Virtual Consultation
- Update EHR
- Issue Prescription
- Generate Billing
- Submit Feedback

Actors:
- Patient
- Healthcare Provider
- Administrator

---

### ✔ Class Diagram Includes:

Core Classes:
- Patient
- Provider
- Appointment
- Consultation
- ElectronicHealthRecord (EHR)
- Prescription
- Billing
- Feedback
- Admin

Relationships:
- Patient books Appointment
- Provider conducts Consultation
- Consultation updates EHR
- Appointment generates Billing
- Patient submits Feedback

---

# 💻 Part 2: Java Implementation

## 🎯 Objective

Part 2 implements the system design using **Java programming**, ensuring that all classes and relationships match the UML model.

---

## 🧱 Key Features Implemented

### 👤 Patient Management
- Patient registration
- Profile updates
- View medical history
- View appointments

### 🩺 Provider Management
- Provider registration
- Manage availability
- View assigned patients

### 📅 Appointment System
- Book virtual appointments
- View schedules
- Prevent scheduling conflicts

### 🏥 Electronic Health Records (EHR)
- Store diagnosis
- Store treatment plans
- Update patient records after consultation

### 💊 Prescription System
- Issue digital prescriptions
- Store prescription history

### 💰 Billing System
- Generate invoices for consultations
- Track payments

### ⭐ Feedback System
- Submit ratings and comments
- Store feedback for analysis

---

## 🧪 Technical Implementation Details

- Language: Java
- Paradigm: Object-Oriented Programming (OOP)
- Data Storage: File-based persistence / Collections
- Error Handling: Checked exceptions used where appropriate
- Design Pattern: Layered Architecture (3-Tier)

---

## 📂 Project Structure

```text id="vhc_structure"
VirtualHealthcareSystem/
│
├── presentation/
│   ├── PatientUI.java
│   ├── ProviderUI.java
│   ├── AdminUI.java
│
├── service/
│   ├── PatientService.java
│   ├── ProviderService.java
│   ├── AppointmentService.java
│   ├── EHRService.java
│   ├── BillingService.java
│   ├── FeedbackService.java
│
├── data/
│   ├── PatientRepository.java
│   ├── ProviderRepository.java
│   ├── AppointmentRepository.java
│   ├── EHRRepository.java
│   ├── BillingRepository.java
│   ├── FeedbackRepository.java
│
├── model/
│   ├── Patient.java
│   ├── Provider.java
│   ├── Appointment.java
│   ├── Consultation.java
│   ├── EHR.java
│   ├── Prescription.java
│   ├── Billing.java
│   ├── Feedback.java
│   ├── Admin.java
│
├── Main.java
└── README.md
```

---

## 🔁 System Workflow

1. Admin registers providers and configures system
2. Patient registers and receives unique ID
3. Patient books appointment with provider
4. Virtual consultation is conducted
5. EHR is updated automatically
6. Prescription is issued if needed
7. Billing is generated
8. Patient submits feedback

---

## 📈 Expected Outcomes

- Fully functional virtual healthcare system simulation
- Clear mapping between UML design and Java implementation
- Demonstration of software architecture principles
- Proper use of OOP, inheritance, and modular design

---

## 🛠️ Technologies Used

- Java
- Object-Oriented Programming (OOP)
- File Handling / Serialization
- UML Design Tools
- Git & GitHub (Version Control)

---

## 👩‍💻 Author

**Merna Ayman**  
Artificial Intelligence Student
