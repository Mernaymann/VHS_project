// Bill.java
package vhs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bill implements Serializable {
    private String billId;
    private double amount;
    private Date issueDate;
    private Date dueDate;
    private String status; // Pending, Paid, Overdue
    private Payment payment;
    private Appointment appointment;

    public Bill(String billId, Appointment appointment) {
        this.billId = billId;
        this.appointment = appointment;
        this.amount = 150.0; // Default consultation fee
        this.issueDate = new Date();
        this.dueDate = new Date(issueDate.getTime() + (14L * 24 * 60 * 60 * 1000)); // 14 days
        this.status = "Pending";
    }

    // Getters and Setters
    public String getBillId() { return billId; }
    public void setBillId(String billId) { this.billId = billId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    // Methods
    public void makePayment(Payment payment) {
        this.payment = payment;
        this.status = "Paid";
        System.out.println("Payment processed for bill: " + billId);
    }

    public void calculateCharges() {
        // Calculate based on appointment type, duration, etc.
        this.amount = 100.0; // Base consultation
        if (appointment.getProvider().getSpecialty().equals("Cardiology")) {
            this.amount += 50.0; // Specialty fee
        }
    }

    public boolean isOverdue() {
        return new Date().after(dueDate) && status.equals("Pending");
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Bill ID: " + billId +
                ", Amount: $" + amount +
                ", Issue Date: " + sdf.format(issueDate) +
                ", Due Date: " + sdf.format(dueDate) +
                ", Status: " + status;
    }
}