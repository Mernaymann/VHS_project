// EHR.java (Electronic Health Record)
package vhs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EHR implements Serializable {
    private String recordId;
    private Patient patient;
    private List<MedicalRecord> medicalRecords;
    private List<Prescription> prescriptions;
    private Date creationDate;
    private Date lastUpdated;

    public EHR(Patient patient) {
        this.patient = patient;
        this.recordId = "EHR" + patient.getId();
        this.medicalRecords = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.creationDate = new Date();
        this.lastUpdated = new Date();
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public void setMedicalRecords(List<MedicalRecord> medicalRecords) { this.medicalRecords = medicalRecords; }

    public List<Prescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescription> prescriptions) { this.prescriptions = prescriptions; }

    public Date getCreationDate() { return creationDate; }

    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }

    // Methods
    public void addMedicalRecord(String diagnosis, String treatment, String notes, Provider provider) {
        MedicalRecord record = new MedicalRecord(diagnosis, treatment, notes, provider, new Date());
        medicalRecords.add(record);
        lastUpdated = new Date();
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
        lastUpdated = new Date();
    }

    public List<Prescription> getActivePrescriptions() {
        List<Prescription> active = new ArrayList<>();
        for (Prescription p : prescriptions) {
            if (p.isActive()) {
                active.add(p);
            }
        }
        return active;
    }
    // Updated: Adding new feature commits!
    @Override
    public String toString() {
        return "EHR ID: " + recordId +
                ", Patient: " + patient.getName() +
                ", Records: " + medicalRecords.size() +
                ", Prescriptions: " + prescriptions.size();
    }
}
