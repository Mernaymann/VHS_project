// Appointment.java
package vhs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment implements Serializable {
    private String appointmentId;
    private Date dateTime;
    private String status; // Scheduled, Completed, Cancelled
    private Patient patient;
    private Provider provider;
    private Bill bill;

    public Appointment() {}

    public Appointment(String appointmentId, Date dateTime, Patient patient, Provider provider) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.patient = patient;
        this.provider = provider;
        this.status = "Scheduled";
        this.bill = new Bill("B" + appointmentId.substring(1), this);
    }

    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Provider getProvider() { return provider; }
    public void setProvider(Provider provider) { this.provider = provider; }

    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }

    // Methods
    public void cancel() {
        this.status = "Cancelled";
        System.out.println("Appointment cancelled: " + appointmentId);
    }

    public void reschedule(Date newDateTime) {
        this.dateTime = newDateTime;
        this.status = "Rescheduled";
        System.out.println("Appointment rescheduled: " + appointmentId);
    }

    public void complete() {
        this.status = "Completed";
        System.out.println("Appointment completed: " + appointmentId);
    }
    // Updated: Adding new feature commits!
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "Appointment ID: " + appointmentId +
                ", Date: " + sdf.format(dateTime) +
                ", Status: " + status +
                ", Patient: " + patient.getName() +
                ", Provider: " + provider.getName();
    }
}
