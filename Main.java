// Main.java - Entry point
package vhs;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Virtual Healthcare System 2025 ===");

        // Check if CLI mode requested
        if (args.length > 0 && args[0].equals("--cli")) {
            runCLI();
            return;
        }

        // Load existing users or create sample
        try {
            users = DataManager.loadUsers();
            if (users.isEmpty()) {
                createSampleUsers();
            }
        } catch (Exception e) {
            System.out.println("Loading users failed, creating sample data...");
            createSampleUsers();
        }

        // Start with login screen
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(users).setVisible(true);
        });
    }

    private static void createSampleUsers() {
        // Create sample users
        Patient john = new Patient("P001", "John Doe", "john@email.com", "pass123",
                "Hypertension", "123-456-7890", "Jane Doe");

        Provider drSmith = new Provider("PR001", "Dr. Smith", "smith@clinic.com", "doc123",
                "Cardiology", "LIC-12345");

        Admin emma = new Admin("A001", "Emma Admin", "emma@vhs.com", "admin123",
                "System Administration");

        users.add(john);
        users.add(drSmith);
        users.add(emma);

        System.out.println("Sample users created.");
    }

    private static void runCLI() {
        System.out.println("Running in CLI mode...");

        // Create sample data
        DataManager dataManager = new DataManager();

        // Create and display some sample operations
        Patient john = new Patient("P001", "John Doe", "john@email.com", "pass123",
                "Hypertension", "123-456-7890", "Jane Doe");

        Provider drSmith = new Provider("PR001", "Dr. Smith", "smith@clinic.com", "doc123",
                "Cardiology", "LIC-12345");

        System.out.println("\nSample Data Created:");
        System.out.println("1. " + john);
        System.out.println("2. " + drSmith);

        System.out.println("\n=== DEMONSTRATION ===");
        System.out.println("1. Inheritance: Both Patient and Provider extend User");
        System.out.println("2. Collections: Using ArrayList for appointments");
        System.out.println("3. File I/O: Data can be saved/loaded from files");
        System.out.println("4. Exception Handling: All file operations are try-catch protected");

        System.out.println("\nTo use full features, run the GUI version without --cli flag.");
    }
}
