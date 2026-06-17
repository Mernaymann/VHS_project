// Admin.java
package vhs;

public class Admin extends User {
    private String department;

    public Admin() {
        super();
    }

    public Admin(String id, String name, String email, String password, String department) {
        super(id, name, email, password, "ADMIN");
        this.department = department;
    }

    // Getters and Setters
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    // Admin-specific methods
    public void manageProviderAccount(Provider provider) {
        System.out.println("Managing provider account: " + provider.getName());
    }

    public void generateAnalytics() {
        System.out.println("Generating system analytics...");
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + getName() + " | Department: " + department);
    }

    @Override
    public String toString() {
        return super.toString() + ", Department: " + department;
    }
}
