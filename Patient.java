// Patient.java
package vhs;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private String medicalHistory;
    private String contactNumber;
    private String emergencyContact;
    private List<Appointment> appointments;
    private EHR ehr;

    public Patient() {
        super();
        this.appointments = new ArrayList<>();
        this.ehr = new EHR(this);
    }

    public Patient(String id, String name, String email, String password,
                   String medicalHistory, String contactNumber, String emergencyContact) {
        super(id, name, email, password, "PATIENT");
        this.medicalHistory = medicalHistory;
        this.contactNumber = contactNumber;
        this.emergencyContact = emergencyContact;
        this.appointments = new ArrayList<>();
        this.ehr = new EHR(this);
    }

    // Getters and Setters
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public EHR getEhr() { return ehr; }
    public void setEhr(EHR ehr) { this.ehr = ehr; }

    // Updated: Adding new feature commits
    // Methods
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    @Override
    public void displayInfo() {
        System.out.println("Patient: " + getName() + " | Medical History: " + medicalHistory);
    }

    @Override
    public String toString() {
        return super.toString() + ", Contact: " + contactNumber + ", Medical History: " + medicalHistory;
    }
}