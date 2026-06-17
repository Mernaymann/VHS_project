// Provider.java
package vhs;

import java.util.ArrayList;
import java.util.List;

public class Provider extends User {
    private String specialty;
    private String licenseNumber;
    private List<String> availableSlots;
    private List<Appointment> appointments;

    public Provider() {
        super();
        this.availableSlots = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public Provider(String id, String name, String email, String password,
                    String specialty, String licenseNumber) {
        super(id, name, email, password, "PROVIDER");
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.availableSlots = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Getters and Setters
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public List<String> getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(List<String> availableSlots) { this.availableSlots = availableSlots; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    // Methods
    public void addAvailableSlot(String slot) {
        availableSlots.add(slot);
    }

    public void removeAvailableSlot(String slot) {
        availableSlots.remove(slot);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    @Override
    public void displayInfo() {
        System.out.println("Provider: " + getName() + " | Specialty: " + specialty);
    }

    @Override
    public String toString() {
        return super.toString() + ", Specialty: " + specialty + ", License: " + licenseNumber;
    }
}