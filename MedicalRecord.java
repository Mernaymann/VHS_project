// MedicalRecord.java
package vhs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MedicalRecord implements Serializable {
    private String diagnosis;
    private String treatment;
    private String notes;
    private Provider provider;
    private Date recordDate;

    public MedicalRecord(String diagnosis, String treatment, String notes, Provider provider, Date recordDate) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.provider = provider;
        this.recordDate = recordDate;
    }

    // Getters and Setters
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }

    public Date getRecordDate() { return recordDate; }
    public void setRecordDate(Date recordDate) { this.recordDate = recordDate; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Diagnosis: " + diagnosis +
                ", Treatment: " + treatment +
                ", Date: " + sdf.format(recordDate) +
                ", Provider: " + provider.getName();
    }
}