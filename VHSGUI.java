// VHSGUI.java - Main GUI Application
package vhs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VHSGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextArea outputArea;
    private DefaultListModel<String> userListModel;
    private DefaultListModel<String> appointmentListModel;

    // Data collections
    private List<User> users;
    private List<Appointment> appointments;
    private User currentUser; // ADD THIS FIELD for role-based access

    // ========== CONSTRUCTOR 1: For login system ==========
    public VHSGUI(User currentUser, List<User> users) {
        this.currentUser = currentUser;
        this.users = users;
        this.appointments = new ArrayList<>();

        setTitle("Virtual Healthcare System 2025 - Welcome " + currentUser.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load appointments if available
        try {
            appointments = DataManager.loadAppointments();
        } catch (Exception e) {
            System.out.println("No appointments loaded: " + e.getMessage());
        }

        // Initialize based on role
        initializeRoleBasedComponents();
    }

    // ========== CONSTRUCTOR 2: Original (for backward compatibility) ==========
    public VHSGUI() {
        setTitle("Virtual Healthcare System 2025");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize data
        users = new ArrayList<>();
        appointments = new ArrayList<>();
        loadSampleData();

        // Create UI components
        initializeComponents();
    }

    // ========== ROLE-BASED COMPONENTS ==========
    private void initializeRoleBasedComponents() {
        tabbedPane = new JTabbedPane();

        // Common tabs for all users
        tabbedPane.addTab("Dashboard", createDashboardPanel());

        // Role-specific tabs
        String role = currentUser.getRole();

        switch (role) {
            case "PATIENT":
                tabbedPane.addTab("My Appointments", createPatientAppointmentPanel());
                tabbedPane.addTab("My EHR", createPatientEHRPanel());
                tabbedPane.addTab("My Bills", createPatientBillingPanel());
                break;

            case "PROVIDER":
                tabbedPane.addTab("Manage Availability", createAvailabilityPanel());
                tabbedPane.addTab("My Appointments", createProviderAppointmentPanel());
                tabbedPane.addTab("Patient EHR", createProviderEHRPanel());
                break;

            case "ADMIN":
                tabbedPane.addTab("User Management", createUserManagementPanel());
                tabbedPane.addTab("System Analytics", createAnalyticsPanel());
                tabbedPane.addTab("All Appointments", createAdminAppointmentPanel());
                break;
        }

        // File operations for all
        tabbedPane.addTab("File Operations", createFileOperationsPanel());

        add(tabbedPane);
    }

    // ========== ORIGINAL INITIALIZE (for backward compatibility) ==========
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Dashboard Tab
        tabbedPane.addTab("Dashboard", createDashboardPanel());

        // User Management Tab
        tabbedPane.addTab("Users", createUserManagementPanel());

        // Appointment Tab
        tabbedPane.addTab("Appointments", createAppointmentPanel());

        // EHR Tab
        tabbedPane.addTab("EHR", createEHRPanel());

        // Billing Tab
        tabbedPane.addTab("Billing", createBillingPanel());

        // File Operations Tab
        tabbedPane.addTab("File Operations", createFileOperationsPanel());

        add(tabbedPane);
    }

    // ========== NEW ROLE-SPECIFIC PANELS ==========

    private JPanel createPatientAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        // Show only current patient's appointments
        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;
            StringBuilder sb = new StringBuilder();
            sb.append("=== MY APPOINTMENTS ===\n\n");

            for (Appointment app : patient.getAppointments()) {
                sb.append(app).append("\n\n");
            }

            if (patient.getAppointments().isEmpty()) {
                sb.append("No appointments scheduled.\n");
            }

            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add booking button
        JButton bookBtn = new JButton("Book New Appointment");
        bookBtn.addActionListener(e -> bookAppointment());
        panel.add(bookBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPatientEHRPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;
            displayEHR(patient.getId(), textArea);
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPatientBillingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;
            StringBuilder sb = new StringBuilder();
            sb.append("=== MY BILLS ===\n\n");

            // Find bills for this patient's appointments
            for (Appointment app : appointments) {
                if (app.getPatient().getId().equals(patient.getId()) && app.getBill() != null) {
                    sb.append(app.getBill()).append("\n\n");
                }
            }

            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProviderAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        if (currentUser instanceof Provider) {
            Provider provider = (Provider) currentUser;
            StringBuilder sb = new StringBuilder();
            sb.append("=== MY APPOINTMENTS ===\n\n");

            for (Appointment app : provider.getAppointments()) {
                sb.append(app).append("\n\n");
            }

            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProviderEHRPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField patientIdField = new JTextField();
        JButton viewBtn = new JButton("View Patient EHR");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        viewBtn.addActionListener(e -> {
            String patientId = patientIdField.getText();
            if (!patientId.isEmpty()) {
                displayEHR(patientId, textArea);
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Patient ID:"));
        topPanel.add(patientIdField);
        topPanel.add(viewBtn);

        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAdminAppointmentPanel() {
        // Reuse the original appointment panel
        return createAppointmentPanel();
    }

    private JPanel createAvailabilityPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField slotField = new JTextField(20);
        JButton addBtn = new JButton("Add Time Slot");
        DefaultListModel<String> slotModel = new DefaultListModel<>();
        JList<String> slotList = new JList<>(slotModel);

        if (currentUser instanceof Provider) {
            Provider provider = (Provider) currentUser;

            // Load existing slots
            for (String slot : provider.getAvailableSlots()) {
                slotModel.addElement(slot);
            }

            addBtn.addActionListener(e -> {
                String slot = slotField.getText();
                if (!slot.isEmpty()) {
                    provider.addAvailableSlot(slot);
                    slotModel.addElement(slot);
                    slotField.setText("");
                }
            });
        }

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Time Slot (YYYY-MM-DD HH:MM):"));
        inputPanel.add(slotField);
        inputPanel.add(addBtn);

        JScrollPane listScroll = new JScrollPane(slotList);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(listScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        showStatistics(); // This populates outputArea

        // Copy statistics to this panel
        textArea.setText(outputArea.getText());

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ========== ORIGINAL PANELS (Keep these unchanged) ==========

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton showStatsBtn = new JButton("Show Statistics");
        showStatsBtn.addActionListener(e -> showStatistics());

        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> refreshData());

        JButton clearBtn = new JButton("Clear Output");
        clearBtn.addActionListener(e -> outputArea.setText(""));

        buttonPanel.add(showStatsBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(clearBtn);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // User List
        userListModel = new DefaultListModel<>();
        refreshUserList();

        JList<String> userList = new JList<>(userListModel);
        JScrollPane listScroll = new JScrollPane(userList);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JButton addPatientBtn = new JButton("Add Patient");
        addPatientBtn.addActionListener(e -> addPatient());

        JButton addProviderBtn = new JButton("Add Provider");
        addProviderBtn.addActionListener(e -> addProvider());

        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.addActionListener(e -> viewUserDetails(userList.getSelectedValue()));

        JButton saveUsersBtn = new JButton("Save to File");
        saveUsersBtn.addActionListener(e -> saveUsersToFile());

        JButton loadUsersBtn = new JButton("Load from File");
        loadUsersBtn.addActionListener(e -> loadUsersFromFile());

        JButton searchBtn = new JButton("Search User");
        searchBtn.addActionListener(e -> searchUser());

        buttonPanel.add(addPatientBtn);
        buttonPanel.add(addProviderBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(saveUsersBtn);
        buttonPanel.add(loadUsersBtn);
        buttonPanel.add(searchBtn);

        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        appointmentListModel = new DefaultListModel<>();
        refreshAppointmentList();

        JList<String> appointmentList = new JList<>(appointmentListModel);
        JScrollPane listScroll = new JScrollPane(appointmentList);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JButton bookAppointmentBtn = new JButton("Book Appointment");
        bookAppointmentBtn.addActionListener(e -> bookAppointment());

        JButton cancelAppointmentBtn = new JButton("Cancel Appointment");
        cancelAppointmentBtn.addActionListener(e -> cancelAppointment(appointmentList.getSelectedValue()));

        JButton completeAppointmentBtn = new JButton("Complete Appointment");
        completeAppointmentBtn.addActionListener(e -> completeAppointment(appointmentList.getSelectedValue()));

        JButton saveAppointmentsBtn = new JButton("Save Appointments");
        saveAppointmentsBtn.addActionListener(e -> saveAppointmentsToFile());

        JButton loadAppointmentsBtn = new JButton("Load Appointments");
        loadAppointmentsBtn.addActionListener(e -> loadAppointmentsFromFile());

        JButton searchAppointmentsBtn = new JButton("Search by Patient");
        searchAppointmentsBtn.addActionListener(e -> searchAppointmentsByPatient());

        buttonPanel.add(bookAppointmentBtn);
        buttonPanel.add(cancelAppointmentBtn);
        buttonPanel.add(completeAppointmentBtn);
        buttonPanel.add(saveAppointmentsBtn);
        buttonPanel.add(loadAppointmentsBtn);
        buttonPanel.add(searchAppointmentsBtn);

        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEHRPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea ehrArea = new JTextArea();
        ehrArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ehrArea);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField patientIdField = new JTextField();
        JButton viewEHRBtn = new JButton("View EHR");
        JButton addRecordBtn = new JButton("Add Medical Record");
        JButton addPrescriptionBtn = new JButton("Add Prescription");
        JButton exportEHRBtn = new JButton("Export EHR Data");

        viewEHRBtn.addActionListener(e -> {
            String patientId = patientIdField.getText();
            if (!patientId.isEmpty()) {
                displayEHR(patientId, ehrArea);
            }
        });

        addRecordBtn.addActionListener(e -> addMedicalRecord(patientIdField.getText()));
        addPrescriptionBtn.addActionListener(e -> addPrescription(patientIdField.getText()));
        exportEHRBtn.addActionListener(e -> exportEHRData());

        inputPanel.add(new JLabel("Patient ID:"));
        inputPanel.add(patientIdField);
        inputPanel.add(viewEHRBtn);
        inputPanel.add(addRecordBtn);
        inputPanel.add(addPrescriptionBtn);
        inputPanel.add(exportEHRBtn);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBillingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea billingArea = new JTextArea();
        billingArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billingArea);

        JPanel inputPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JTextField billIdField = new JTextField();
        JButton viewBillBtn = new JButton("View Bill");
        JButton makePaymentBtn = new JButton("Make Payment");
        JButton generateBillBtn = new JButton("Generate Bill");
        JButton exportBillsBtn = new JButton("Export Bills");

        viewBillBtn.addActionListener(e -> {
            String billId = billIdField.getText();
            if (!billId.isEmpty()) {
                displayBill(billId, billingArea);
            }
        });

        makePaymentBtn.addActionListener(e -> makePayment(billIdField.getText()));
        generateBillBtn.addActionListener(e -> generateBill());
        exportBillsBtn.addActionListener(e -> exportBillingData());

        inputPanel.add(new JLabel("Bill ID:"));
        inputPanel.add(billIdField);
        inputPanel.add(viewBillBtn);
        inputPanel.add(makePaymentBtn);
        inputPanel.add(generateBillBtn);
        inputPanel.add(exportBillsBtn);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFileOperationsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JButton exportAllBtn = new JButton("Export All Data");
        JButton importBtn = new JButton("Import Data");
        JButton backupBtn = new JButton("Create Backup");
        JButton restoreBtn = new JButton("Restore Backup");
        JButton clearAllBtn = new JButton("Clear All Data");
        JButton showFilesBtn = new JButton("Show Data Files");
        JButton validateBtn = new JButton("Validate Data");
        JButton exitBtn = new JButton("Exit Application");

        exportAllBtn.addActionListener(e -> exportAllData());
        importBtn.addActionListener(e -> importData());
        backupBtn.addActionListener(e -> createBackup());
        restoreBtn.addActionListener(e -> restoreBackup());
        clearAllBtn.addActionListener(e -> clearAllData());
        showFilesBtn.addActionListener(e -> showDataFiles());
        validateBtn.addActionListener(e -> validateData());
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(exportAllBtn);
        panel.add(importBtn);
        panel.add(backupBtn);
        panel.add(restoreBtn);
        panel.add(clearAllBtn);
        panel.add(showFilesBtn);
        panel.add(validateBtn);
        panel.add(exitBtn);

        return panel;
    }

    // ========== DATA MANAGEMENT METHODS ==========

    private void loadSampleData() {
        // Create sample patients
        Patient john = new Patient("P001", "John Doe", "john@email.com", "pass123",
                "Chronic condition management", "123-456-7890", "Jane Doe");

        // Updated: Adding new feature commits!

        // Create sample providers
        Provider drSmith = new Provider("PR001", "Dr. Smith", "smith@clinic.com", "doc123",
                "Cardiology", "LIC-12345");
        drSmith.addAvailableSlot("2024-01-15 10:00");
        drSmith.addAvailableSlot("2024-01-15 14:00");

        Provider nurseLucy = new Provider("PR002", "Nurse Lucy", "lucy@clinic.com", "nurse123",
                "Chronic Condition Management", "LIC-67890");
        nurseLucy.addAvailableSlot("2024-01-16 09:00");
        nurseLucy.addAvailableSlot("2024-01-16 11:00");

        // Create sample admin
        Admin emma = new Admin("A001", "Emma Admin", "emma@vhs.com", "admin123", "System Administration");

        users.add(john);
        users.add(drSmith);
        users.add(nurseLucy);
        users.add(emma);

        // Create sample appointment
        Appointment app1 = new Appointment("A001", new Date(), john, drSmith);
        john.addAppointment(app1);
        drSmith.addAppointment(app1);
        appointments.add(app1);

        // Add sample medical record
        john.getEhr().addMedicalRecord("Hypertension", "Lisinopril 10mg daily",
                "Monitor blood pressure weekly", drSmith);

        // Add sample prescription
        Prescription pres1 = new Prescription("RX001", "Lisinopril", "10mg",
                "Take once daily", new Date(), drSmith);
        john.getEhr().addPrescription(pres1);
    }

    private void searchAvailableSlots() {
        JComboBox<Provider> providerCombo = new JComboBox<>();
        for (User user : users) {
            if (user instanceof Provider) {
                providerCombo.addItem((Provider) user);
            }
        }
    }

    private void refreshUserList() {
        if (userListModel != null) {
            userListModel.clear();
            for (User user : users) {
                userListModel.addElement(user.toString());
            }
        }
    }

    private void refreshAppointmentList() {
        if (appointmentListModel != null) {
            appointmentListModel.clear();
            for (Appointment app : appointments) {
                appointmentListModel.addElement(app.toString());
            }
        }
    }

    // ========== ALL DATA MANAGEMENT METHODS ==========

    private void showStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== VIRTUAL HEALTHCARE SYSTEM STATISTICS ===\n");
        stats.append("Total Users: ").append(users.size()).append("\n");

        int patientCount = 0, providerCount = 0, adminCount = 0;
        for (User user : users) {
            if (user instanceof Patient) patientCount++;
            else if (user instanceof Provider) providerCount++;
            else if (user instanceof Admin) adminCount++;
        }

        stats.append("  - Patients: ").append(patientCount).append("\n");
        stats.append("  - Providers: ").append(providerCount).append("\n");
        stats.append("  - Admins: ").append(adminCount).append("\n");
        stats.append("\n");
        stats.append("Total Appointments: ").append(appointments.size()).append("\n");

        int scheduled = 0, completed = 0, cancelled = 0;
        for (Appointment app : appointments) {
            switch (app.getStatus()) {
                case "Scheduled": scheduled++; break;
                case "Completed": completed++; break;
                case "Cancelled": cancelled++; break;
            }
        }

        stats.append("  - Scheduled: ").append(scheduled).append("\n");
        stats.append("  - Completed: ").append(completed).append("\n");
        stats.append("  - Cancelled: ").append(cancelled).append("\n");
        stats.append("\n");
        stats.append("System Date: ").append(new Date()).append("\n");

        outputArea.setText(stats.toString());
    }

    private void refreshData() {
        refreshUserList();
        refreshAppointmentList();
        outputArea.setText("Data refreshed successfully!\n");
        showStatistics();
    }

    private void addPatient() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();
        JTextField medicalField = new JTextField();
        JTextField contactField = new JTextField();

        panel.add(new JLabel("Patient ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Medical History:"));
        panel.add(medicalField);
        panel.add(new JLabel("Contact Number:"));
        panel.add(contactField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Patient",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Patient patient = new Patient(
                        idField.getText(),
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        medicalField.getText(),
                        contactField.getText(),
                        "Emergency: " + contactField.getText()
                );

                users.add(patient);
                refreshUserList();
                outputArea.setText("Patient added successfully:\n" + patient);
            } catch (Exception e) {
                outputArea.setText("Error adding patient: " + e.getMessage());
            }
        }
    }

    private void addProvider() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();
        JTextField specialtyField = new JTextField();

        panel.add(new JLabel("Provider ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Specialty:"));
        panel.add(specialtyField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Provider",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Provider provider = new Provider(
                        idField.getText(),
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        specialtyField.getText(),
                        "LIC-" + System.currentTimeMillis()
                );

                users.add(provider);
                refreshUserList();
                outputArea.setText("Provider added successfully:\n" + provider);
            } catch (Exception e) {
                outputArea.setText("Error adding provider: " + e.getMessage());
            }
        }
    }

    private void viewUserDetails(String userString) {
        if (userString == null) {
            outputArea.setText("Please select a user first!");
            return;
        }

        // Extract ID from the string (first part after "ID: ")
        String[] parts = userString.split(",");
        if (parts.length > 0) {
            String idPart = parts[0];
            String id = idPart.substring(idPart.indexOf(":") + 2);

            for (User user : users) {
                if (user.getId().equals(id)) {
                    outputArea.setText("USER DETAILS:\n");
                    outputArea.append("ID: " + user.getId() + "\n");
                    outputArea.append("Name: " + user.getName() + "\n");
                    outputArea.append("Email: " + user.getEmail() + "\n");
                    outputArea.append("Role: " + user.getRole() + "\n");

                    if (user instanceof Patient) {
                        Patient p = (Patient) user;
                        outputArea.append("Medical History: " + p.getMedicalHistory() + "\n");
                        outputArea.append("Contact: " + p.getContactNumber() + "\n");
                        outputArea.append("Appointments: " + p.getAppointments().size() + "\n");
                    } else if (user instanceof Provider) {
                        Provider p = (Provider) user;
                        outputArea.append("Specialty: " + p.getSpecialty() + "\n");
                        outputArea.append("Available Slots: " + p.getAvailableSlots().size() + "\n");
                    }
                    return;
                }
            }
        }
        outputArea.setText("User not found!");
    }

    private void saveUsersToFile() {
        try {
            DataManager.saveUsers(users);
            outputArea.setText("Users saved to file successfully!");
        } catch (IOException e) {
            outputArea.setText("Error saving users: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        try {
            users = DataManager.loadUsers();
            refreshUserList();
            outputArea.setText("Users loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            outputArea.setText("Error loading users: " + e.getMessage());
        }
    }

    private void searchUser() {
        String searchId = JOptionPane.showInputDialog(this, "Enter User ID to search:");
        if (searchId != null && !searchId.trim().isEmpty()) {
            User foundUser = DataManager.findUserById(searchId);
            if (foundUser != null) {
                outputArea.setText("USER FOUND:\n" + foundUser);
            } else {
                outputArea.setText("User with ID '" + searchId + "' not found.");
            }
        }
    }

    private void bookAppointment() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField appIdField = new JTextField("A" + (appointments.size() + 1));
        JTextField patientIdField = new JTextField();
        JTextField providerIdField = new JTextField();
        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

        panel.add(new JLabel("Appointment ID:"));
        panel.add(appIdField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Provider ID:"));
        panel.add(providerIdField);
        panel.add(new JLabel("Date (yyyy-MM-dd HH:mm):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Book Appointment",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Find patient and provider
                Patient patient = null;
                Provider provider = null;

                for (User user : users) {
                    if (user.getId().equals(patientIdField.getText()) && user instanceof Patient) {
                        patient = (Patient) user;
                    }
                    if (user.getId().equals(providerIdField.getText()) && user instanceof Provider) {
                        provider = (Provider) user;
                    }
                }

                if (patient == null || provider == null) {
                    outputArea.setText("Error: Patient or Provider not found!");
                    return;
                }

                // Parse date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date appointmentDate = sdf.parse(dateField.getText());

                // Create appointment
                Appointment appointment = new Appointment(
                        appIdField.getText(),
                        appointmentDate,
                        patient,
                        provider
                );

                // Add to collections
                appointments.add(appointment);
                patient.addAppointment(appointment);
                provider.addAppointment(appointment);

                refreshAppointmentList();
                outputArea.setText("Appointment booked successfully!\n" + appointment);

            } catch (Exception e) {
                outputArea.setText("Error booking appointment: " + e.getMessage());
            }
        }
    }

    private void cancelAppointment(String appointmentString) {
        if (appointmentString == null) {
            outputArea.setText("Please select an appointment first!");
            return;
        }

        // Extract appointment ID
        String[] parts = appointmentString.split(",");
        if (parts.length > 0) {
            String idPart = parts[0];
            String appId = idPart.substring(idPart.indexOf(":") + 2);

            for (Appointment app : appointments) {
                if (app.getAppointmentId().equals(appId)) {
                    app.cancel();
                    refreshAppointmentList();
                    outputArea.setText("Appointment cancelled: " + appId);
                    return;
                }
            }
        }
        outputArea.setText("Appointment not found!");
    }

    private void completeAppointment(String appointmentString) {
        if (appointmentString == null) {
            outputArea.setText("Please select an appointment first!");
            return;
        }

        String[] parts = appointmentString.split(",");
        if (parts.length > 0) {
            String idPart = parts[0];
            String appId = idPart.substring(idPart.indexOf(":") + 2);

            for (Appointment app : appointments) {
                if (app.getAppointmentId().equals(appId)) {
                    app.complete();

                    // Generate prescription if needed
                    if (JOptionPane.showConfirmDialog(this,
                            "Generate prescription for this appointment?",
                            "Prescription", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        Prescription prescription = new Prescription(
                                "RX" + appId.substring(1),
                                "Sample Medication",
                                "10mg",
                                "Take as directed",
                                new Date(),
                                app.getProvider()
                        );

                        Patient patient = app.getPatient();
                        patient.getEhr().addPrescription(prescription);
                        outputArea.setText("Appointment completed and prescription generated!");
                    } else {
                        outputArea.setText("Appointment completed successfully!");
                    }

                    refreshAppointmentList();
                    return;
                }
            }
        }
        outputArea.setText("Appointment not found!");
    }
    private void collectFeedback(Appointment appointment) {
        String feedback = JOptionPane.showInputDialog("Please provide feedback:");
        if (feedback != null) {
            // Store feedback
            System.out.println("Feedback for " + appointment.getProvider().getName() + ": " + feedback);
        }
    }

    private void saveAppointmentsToFile() {
        try {
            DataManager.saveAppointments(appointments);
            outputArea.setText("Appointments saved to file successfully!");
        } catch (IOException e) {
            outputArea.setText("Error saving appointments: " + e.getMessage());
        }
    }

    private void loadAppointmentsFromFile() {
        try {
            appointments = DataManager.loadAppointments();
            refreshAppointmentList();
            outputArea.setText("Appointments loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            outputArea.setText("Error loading appointments: " + e.getMessage());
        }
    }

    private void searchAppointmentsByPatient() {
        String patientId = JOptionPane.showInputDialog(this, "Enter Patient ID to search appointments:");
        if (patientId != null && !patientId.trim().isEmpty()) {
            List<Appointment> patientApps = DataManager.findAppointmentsByPatientId(patientId);
            if (!patientApps.isEmpty()) {
                outputArea.setText("APPOINTMENTS FOR PATIENT " + patientId + ":\n");
                for (Appointment app : patientApps) {
                    outputArea.append(app + "\n");
                }
            } else {
                outputArea.setText("No appointments found for patient ID: " + patientId);
            }
        }
    }

    private void displayEHR(String patientId, JTextArea ehrArea) {
        for (User user : users) {
            if (user.getId().equals(patientId) && user instanceof Patient) {
                Patient patient = (Patient) user;
                EHR ehr = patient.getEhr();

                StringBuilder ehrText = new StringBuilder();
                ehrText.append("=== ELECTRONIC HEALTH RECORD ===\n");
                ehrText.append("Patient: ").append(patient.getName()).append("\n");
                ehrText.append("EHR ID: ").append(ehr.getRecordId()).append("\n");
                ehrText.append("Created: ").append(ehr.getCreationDate()).append("\n");
                ehrText.append("Last Updated: ").append(ehr.getLastUpdated()).append("\n\n");

                ehrText.append("MEDICAL RECORDS:\n");
                if (ehr.getMedicalRecords().isEmpty()) {
                    ehrText.append("  No medical records found.\n");
                } else {
                    for (MedicalRecord record : ehr.getMedicalRecords()) {
                        ehrText.append("  - ").append(record).append("\n");
                    }
                }

                ehrText.append("\nPRESCRIPTIONS:\n");
                if (ehr.getPrescriptions().isEmpty()) {
                    ehrText.append("  No prescriptions found.\n");
                } else {
                    for (Prescription prescription : ehr.getPrescriptions()) {
                        ehrText.append("  - ").append(prescription).append("\n");
                    }
                }

                ehrArea.setText(ehrText.toString());
                return;
            }
        }
        ehrArea.setText("Patient not found or not a valid patient!");
    }

    private void addMedicalRecord(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            outputArea.setText("Please enter a Patient ID first!");
            return;
        }

        for (User user : users) {
            if (user.getId().equals(patientId) && user instanceof Patient) {
                Patient patient = (Patient) user;

                JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
                JTextField diagnosisField = new JTextField();
                JTextField treatmentField = new JTextField();
                JTextField notesField = new JTextField();
                JTextField providerIdField = new JTextField();

                panel.add(new JLabel("Diagnosis:"));
                panel.add(diagnosisField);
                panel.add(new JLabel("Treatment:"));
                panel.add(treatmentField);
                panel.add(new JLabel("Notes:"));
                panel.add(notesField);
                panel.add(new JLabel("Provider ID:"));
                panel.add(providerIdField);

                int result = JOptionPane.showConfirmDialog(this, panel,
                        "Add Medical Record",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    // Find provider
                    Provider provider = null;
                    for (User u : users) {
                        if (u.getId().equals(providerIdField.getText()) && u instanceof Provider) {
                            provider = (Provider) u;
                            break;
                        }
                    }

                    if (provider != null) {
                        patient.getEhr().addMedicalRecord(
                                diagnosisField.getText(),
                                treatmentField.getText(),
                                notesField.getText(),
                                provider
                        );
                        outputArea.setText("Medical record added successfully for patient: " + patientId);
                    } else {
                        outputArea.setText("Provider not found!");
                    }
                }
                return;
            }
        }
        outputArea.setText("Patient not found!");
    }

    private void addPrescription(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            outputArea.setText("Please enter a Patient ID first!");
            return;
        }

        for (User user : users) {
            if (user.getId().equals(patientId) && user instanceof Patient) {
                Patient patient = (Patient) user;

                JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
                JTextField medField = new JTextField();
                JTextField dosageField = new JTextField();
                JTextField instructionsField = new JTextField();
                JTextField providerIdField = new JTextField();

                panel.add(new JLabel("Medication:"));
                panel.add(medField);
                panel.add(new JLabel("Dosage:"));
                panel.add(dosageField);
                panel.add(new JLabel("Instructions:"));
                panel.add(instructionsField);
                panel.add(new JLabel("Provider ID:"));
                panel.add(providerIdField);

                int result = JOptionPane.showConfirmDialog(this, panel,
                        "Add Prescription",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    // Find provider
                    Provider provider = null;
                    for (User u : users) {
                        if (u.getId().equals(providerIdField.getText()) && u instanceof Provider) {
                            provider = (Provider) u;
                            break;
                        }
                    }

                    if (provider != null) {
                        Prescription prescription = new Prescription(
                                "RX" + System.currentTimeMillis(),
                                medField.getText(),
                                dosageField.getText(),
                                instructionsField.getText(),
                                new Date(),
                                provider
                        );

                        patient.getEhr().addPrescription(prescription);
                        outputArea.setText("Prescription added successfully for patient: " + patientId);
                    } else {
                        outputArea.setText("Provider not found!");
                    }
                }
                return;
            }
        }
        outputArea.setText("Patient not found!");
    }

    private void exportEHRData() {
        try {
            String filename = "ehr_export_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
            DataManager.exportDataToText(filename);
            outputArea.setText("EHR data exported to: " + filename);
        } catch (IOException e) {
            outputArea.setText("Error exporting EHR data: " + e.getMessage());
        }
    }

    private void displayBill(String billId, JTextArea billingArea) {
        for (Appointment app : appointments) {
            if (app.getBill() != null && app.getBill().getBillId().equals(billId)) {
                Bill bill = app.getBill();
                StringBuilder billText = new StringBuilder();
                billText.append("=== BILL DETAILS ===\n");
                billText.append("Bill ID: ").append(bill.getBillId()).append("\n");
                billText.append("Patient: ").append(app.getPatient().getName()).append("\n");
                billText.append("Provider: ").append(app.getProvider().getName()).append("\n");
                billText.append("Appointment: ").append(app.getAppointmentId()).append("\n");
                billText.append("Amount: $").append(bill.getAmount()).append("\n");
                billText.append("Issue Date: ").append(bill.getIssueDate()).append("\n");
                billText.append("Due Date: ").append(bill.getDueDate()).append("\n");
                billText.append("Status: ").append(bill.getStatus()).append("\n");

                if (bill.getPayment() != null) {
                    billText.append("\nPAYMENT INFORMATION:\n");
                    billText.append(bill.getPayment());
                }

                billingArea.setText(billText.toString());
                return;
            }
        }
        billingArea.setText("Bill not found!");
    }

    private void makePayment(String billId) {
        if (billId == null || billId.trim().isEmpty()) {
            outputArea.setText("Please enter a Bill ID first!");
            return;
        }

        for (Appointment app : appointments) {
            if (app.getBill() != null && app.getBill().getBillId().equals(billId)) {
                Bill bill = app.getBill();

                if (bill.getStatus().equals("Paid")) {
                    outputArea.setText("Bill is already paid!");
                    return;
                }

                JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
                JTextField amountField = new JTextField(String.valueOf(bill.getAmount()));
                JComboBox<String> methodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "PayPal"});

                panel.add(new JLabel("Amount:"));
                panel.add(amountField);
                panel.add(new JLabel("Payment Method:"));
                panel.add(methodCombo);

                int result = JOptionPane.showConfirmDialog(this, panel,
                        "Make Payment",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Payment payment = new Payment(
                                "PAY" + System.currentTimeMillis(),
                                Double.parseDouble(amountField.getText()),
                                (String) methodCombo.getSelectedItem()
                        );

                        bill.makePayment(payment);
                        outputArea.setText("Payment successful for bill: " + billId);
                    } catch (NumberFormatException e) {
                        outputArea.setText("Invalid amount format!");
                    }
                }
                return;
            }
        }
        outputArea.setText("Bill not found!");
    }

    private void generateBill() {
        if (appointments.isEmpty()) {
            outputArea.setText("No appointments available to generate bills!");
            return;
        }

        StringBuilder billReport = new StringBuilder();
        billReport.append("=== BILLING REPORT ===\n");

        double totalAmount = 0;
        for (Appointment app : appointments) {
            if (app.getBill() != null) {
                Bill bill = app.getBill();
                billReport.append("Appointment: ").append(app.getAppointmentId())
                        .append(", Bill: ").append(bill.getBillId())
                        .append(", Amount: $").append(bill.getAmount())
                        .append(", Status: ").append(bill.getStatus())
                        .append("\n");
                totalAmount += bill.getAmount();
            }
        }

        billReport.append("\nTOTAL OUTSTANDING AMOUNT: $").append(totalAmount);
        outputArea.setText(billReport.toString());
    }

    private void exportBillingData() {
        try {
            String filename = "billing_export_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("=== BILLING DATA EXPORT ===");
                writer.println("Export Date: " + new Date());
                writer.println();

                double totalCollected = 0;
                int paidBills = 0;

                for (Appointment app : appointments) {
                    if (app.getBill() != null) {
                        Bill bill = app.getBill();
                        writer.println("Bill ID: " + bill.getBillId());
                        writer.println("  Appointment: " + app.getAppointmentId());
                        writer.println("  Patient: " + app.getPatient().getName());
                        writer.println("  Amount: $" + bill.getAmount());
                        writer.println("  Status: " + bill.getStatus());
                        writer.println("  Due Date: " + bill.getDueDate());

                        if (bill.getStatus().equals("Paid")) {
                            totalCollected += bill.getAmount();
                            paidBills++;
                        }
                        writer.println();
                    }
                }

                writer.println("SUMMARY:");
                writer.println("  Total Bills: " + appointments.size());
                writer.println("  Paid Bills: " + paidBills);
                writer.println("  Total Collected: $" + totalCollected);
            }

            outputArea.setText("Billing data exported to: " + filename);
        } catch (IOException e) {
            outputArea.setText("Error exporting billing data: " + e.getMessage());
        }
    }

    private void exportAllData() {
        try {
            String filename = "vhs_full_export_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
            DataManager.exportDataToText(filename);
            outputArea.setText("All data exported successfully to: " + filename);
        } catch (IOException e) {
            outputArea.setText("Error exporting data: " + e.getMessage());
        }
    }

    private void importData() {
        // In a real application, this would read from a file
        outputArea.setText("Import functionality would read from a file format.\n" +
                "For now, sample data is loaded automatically.");
    }

    private void createBackup() {
        try {
            // Save current state
            DataManager.saveUsers(users);
            DataManager.saveAppointments(appointments);

            // Create backup copy
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupName = "backup_" + timestamp;

            // Copy files
            File usersFile = new File("users.dat");
            File appointmentsFile = new File("appointments.dat");

            if (usersFile.exists()) {
                java.nio.file.Files.copy(usersFile.toPath(),
                        new File("users_backup_" + timestamp + ".dat").toPath());
            }
            if (appointmentsFile.exists()) {
                java.nio.file.Files.copy(appointmentsFile.toPath(),
                        new File("appointments_backup_" + timestamp + ".dat").toPath());
            }

            outputArea.setText("Backup created successfully: " + backupName);
        } catch (IOException e) {
            outputArea.setText("Error creating backup: " + e.getMessage());
        }
    }

    private void restoreBackup() {
        // In a real application, this would restore from backup files
        outputArea.setText("Restore functionality would load from backup files.\n" +
                "For now, please use 'Load from File' options.");
    }

    private void clearAllData() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all data? This cannot be undone!",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            users.clear();
            appointments.clear();
            refreshUserList();
            refreshAppointmentList();
            outputArea.setText("All data cleared successfully!");
        }
    }

    private void showDataFiles() {
        StringBuilder filesInfo = new StringBuilder();
        filesInfo.append("=== DATA FILES ===\n");

        File[] files = new File(".").listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".dat") || file.getName().endsWith(".txt")) {
                    filesInfo.append(file.getName())
                            .append(" - ")
                            .append(file.length() / 1024)
                            .append(" KB\n");
                }
            }
        }

        outputArea.setText(filesInfo.toString());
    }

    private void validateData() {
        StringBuilder validation = new StringBuilder();
        validation.append("=== DATA VALIDATION ===\n");

        // Validate users
        validation.append("Users: ").append(users.size()).append(" total\n");
        for (User user : users) {
            if (user.getId() == null || user.getId().isEmpty()) {
                validation.append("  ERROR: User without ID found\n");
            }
        }

        // Validate appointments
        validation.append("Appointments: ").append(appointments.size()).append(" total\n");
        for (Appointment app : appointments) {
            if (app.getPatient() == null || app.getProvider() == null) {
                validation.append("  ERROR: Appointment without patient/provider: ").append(app.getAppointmentId()).append("\n");
            }
        }

        validation.append("\nValidation complete!");
        outputArea.setText(validation.toString());
    }

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For backward compatibility - starts without login
            VHSGUI gui = new VHSGUI();
            gui.setVisible(true);
        });
    }
}
