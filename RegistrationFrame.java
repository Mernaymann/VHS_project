package vhs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RegistrationFrame extends JFrame {
    private JComboBox<String> userTypeCombo;
    private JPanel dynamicPanel;
    private CardLayout cardLayout;
    private List<User> users;
    private LoginFrame loginFrame;

    public RegistrationFrame(List<User> users, LoginFrame loginFrame) {
        this.users = users;
        this.loginFrame = loginFrame;

        setTitle("Register - Virtual Healthcare System");
        setSize(500, 400);
        setLocationRelativeTo(loginFrame);
        setLayout(new BorderLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        // User Type Selection
        JPanel typePanel = new JPanel(new FlowLayout());
        typePanel.add(new JLabel("Register as:"));
        userTypeCombo = new JComboBox<>(new String[]{"Patient", "Provider", "Admin"});
        userTypeCombo.addActionListener(e -> showRegistrationForm());
        typePanel.add(userTypeCombo);

        // Dynamic Forms Panel
        cardLayout = new CardLayout();
        dynamicPanel = new JPanel(cardLayout);

        // Add forms
        dynamicPanel.add(createPatientForm(), "PATIENT");
        dynamicPanel.add(createProviderForm(), "PROVIDER");
        dynamicPanel.add(createAdminForm(), "ADMIN");

        add(typePanel, BorderLayout.NORTH);
        add(dynamicPanel, BorderLayout.CENTER);

        showRegistrationForm();
    }

    private JPanel createPatientForm() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField("P" + (users.size() + 100));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField medicalField = new JTextField();
        JTextField contactField = new JTextField();

        panel.add(new JLabel("Patient ID:"));
        panel.add(idField);
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Medical History:"));
        panel.add(medicalField);
        panel.add(new JLabel("Contact Number:"));
        panel.add(contactField);

        JButton registerBtn = new JButton("Register Patient");
        registerBtn.addActionListener(e -> {
            Patient patient = new Patient(
                    idField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    medicalField.getText(),
                    contactField.getText(),
                    "Emergency: " + contactField.getText()
            );
            users.add(patient);
            JOptionPane.showMessageDialog(this, "Patient registered successfully!");
            dispose();
        });

        panel.add(new JLabel(""));
        panel.add(registerBtn);

        return panel;
    }

    private JPanel createProviderForm() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField("PR" + (users.size() + 100));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField specialtyField = new JTextField();

        panel.add(new JLabel("Provider ID:"));
        panel.add(idField);
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Specialty:"));
        panel.add(specialtyField);

        JButton registerBtn = new JButton("Register Provider");
        registerBtn.addActionListener(e -> {
            Provider provider = new Provider(
                    idField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    specialtyField.getText(),
                    "LIC-" + System.currentTimeMillis()
            );
            users.add(provider);
            JOptionPane.showMessageDialog(this, "Provider registered successfully!");
            dispose();
        });

        panel.add(new JLabel(""));
        panel.add(registerBtn);

        return panel;
    }

    private JPanel createAdminForm() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField("A" + (users.size() + 100));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField deptField = new JTextField();

        panel.add(new JLabel("Admin ID:"));
        panel.add(idField);
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);

        JButton registerBtn = new JButton("Register Admin");
        registerBtn.addActionListener(e -> {
            Admin admin = new Admin(
                    idField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    deptField.getText()
            );
            users.add(admin);
            JOptionPane.showMessageDialog(this, "Admin registered successfully!");
            dispose();
        });

        panel.add(new JLabel(""));
        panel.add(registerBtn);

        return panel;
    }

    private void showRegistrationForm() {
        String userType = ((String) userTypeCombo.getSelectedItem()).toUpperCase();
        cardLayout.show(dynamicPanel, userType);
    }
}
