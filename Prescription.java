// Prescription.java
package vhs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prescription implements Serializable {
    private String prescriptionId;
    private Patient patient;
    private String medication;
    private String dosage;
    private String instructions;
    private Date issueDate;
    private Date expiryDate;
    private boolean isActive;
    private Provider provider;

    public Prescription(String prescriptionId, String medication, String dosage,
                        String instructions, Date issueDate, Provider provider) {
        this.prescriptionId = prescriptionId;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
        this.issueDate = issueDate;
        // Set expiry to 30 days from issue
        this.expiryDate = new Date(issueDate.getTime() + (30L * 24 * 60 * 60 * 1000));
        this.isActive = true;
        this.provider = provider;
    }

    public Patient getPatient() {
        return patient;
    }

    // Getters and Setters
    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }

    // Methods
    public void renew() {
        this.issueDate = new Date();
        this.expiryDate = new Date(issueDate.getTime() + (30L * 24 * 60 * 60 * 1000));
        this.isActive = true;
        System.out.println("Prescription renewed: " + prescriptionId);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Prescription: " + medication +
                " (" + dosage + ")" +
                ", Issue Date: " + sdf.format(issueDate) +
                ", Expiry: " + sdf.format(expiryDate) +
                ", Active: " + isActive;
    }
}
