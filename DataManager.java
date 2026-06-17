// DataManager.java - Handles file operations
package vhs;

import java.io.*;
import java.util.*;

public class DataManager {
    private static final String USERS_FILE = "users.dat";
    private static final String APPOINTMENTS_FILE = "appointments.dat";
    private static final String EHR_FILE = "ehr.dat";

    // Save all users to file
    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Users saved successfully: " + users.size() + " records");
        }
    }

    // Load users from file
    public static List<User> loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (List<User>) ois.readObject();
        }
    }

    // Save appointments
    public static void saveAppointments(List<Appointment> appointments) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(APPOINTMENTS_FILE))) {
            oos.writeObject(appointments);
            System.out.println("Appointments saved: " + appointments.size());
        }
    }

    // Updated: Adding new feature commits

    // Load appointments
    public static List<Appointment> loadAppointments() throws IOException, ClassNotFoundException {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(APPOINTMENTS_FILE))) {
            return (List<Appointment>) ois.readObject();
        }
    }

    // Search users by ID
    public static User findUserById(String id) {
        try {
            List<User> users = loadUsers();
            for (User user : users) {
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        } catch (Exception e) {
            System.err.println("Error searching user: " + e.getMessage());
        }
        return null;
    }

    // Search appointments by patient ID
    public static List<Appointment> findAppointmentsByPatientId(String patientId) {
        List<Appointment> result = new ArrayList<>();
        try {
            List<Appointment> appointments = loadAppointments();
            for (Appointment app : appointments) {
                if (app.getPatient().getId().equals(patientId)) {
                    result.add(app);
                }
            }
        } catch (Exception e) {
            System.err.println("Error searching appointments: " + e.getMessage());
        }
        return result;
    }

    // Export data to text file (for backup)
    public static void exportDataToText(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== VIRTUAL HEALTHCARE SYSTEM DATA EXPORT ===");
            writer.println("Export Date: " + new Date());
            writer.println();

            try {
                List<User> users = loadUsers();
                writer.println("USERS (" + users.size() + "):");
                for (User user : users) {
                    writer.println("  " + user);
                }
                writer.println();

                List<Appointment> appointments = loadAppointments();
                writer.println("APPOINTMENTS (" + appointments.size() + "):");
                for (Appointment app : appointments) {
                    writer.println("  " + app);
                }
            } catch (Exception e) {
                writer.println("Error loading data: " + e.getMessage());
            }
        }
    }
}
