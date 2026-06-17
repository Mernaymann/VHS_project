package vhs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private List<User> users;
    private JButton loginBtn, registerBtn;

    public LoginFrame(List<User> users) {
        this.users = users;
        setTitle("Virtual Healthcare System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("VHS 2025 - Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        roleCombo = new JComboBox<>(new String[]{"Patient", "Provider", "Admin"});

        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(roleLabel);
        inputPanel.add(roleCombo);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> authenticate());
        registerBtn.addActionListener(e -> openRegistration());

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        // Add components
        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void authenticate() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = ((String) roleCombo.getSelectedItem()).toUpperCase();

        User loggedInUser = null;

        for (User user : users) {
            if (user.getEmail().equals(email) &&
                    user.getPassword().equals(password) &&
                    user.getRole().equals(role)) {
                loggedInUser = user;
                break;
            }
        }

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            openMainApplication(loggedInUser);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        new RegistrationFrame(users, this).setVisible(true);
    }

    private void openMainApplication(User user) {
        VHSGUI mainApp = new VHSGUI(user, users);
        mainApp.setVisible(true);
    }
}
