package vhs;

import javax.swing.JOptionPane;
import java.util.*;
import java.text.SimpleDateFormat;

public class NotificationService {
    private List<Appointment> appointments;
    private User currentUser; // Added: current user reference

    // Constructor with appointments only
    public NotificationService(List<Appointment> appointments) {
        this.appointments = appointments;
        this.currentUser = null; // Initially null, should be set based on context
    }

    // Alternative constructor with current user
    public NotificationService(List<Appointment> appointments, User currentUser) {
        this.appointments = appointments;
        this.currentUser = currentUser;
    }

    // Method to set current user if not in constructor
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void checkAndSendReminders() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        // Check appointments in next 24 hours
        calendar.add(Calendar.HOUR, 24);
        Date tomorrow = calendar.getTime();

        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals("Scheduled")) {
                Date appTime = appointment.getDateTime();

                // Send reminder 24 hours before
                if (appTime.after(now) && appTime.before(tomorrow)) {
                    sendReminder(appointment);
                }

                // Send reminder 1 hour before
                calendar.setTime(now);
                calendar.add(Calendar.HOUR, 1);
                Date oneHourFromNow = calendar.getTime();

                if (appTime.after(now) && appTime.before(oneHourFromNow)) {
                    sendUrgentReminder(appointment);
                }
            }
        }
    }

    private void sendReminder(Appointment appointment) {
        String message = String.format(
                "REMINDER: You have an appointment with %s on %s at %s",
                appointment.getProvider().getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(appointment.getDateTime()),
                new SimpleDateFormat("HH:mm").format(appointment.getDateTime())
        );

        // In real system: Send email/SMS
        System.out.println("Notification sent to " + appointment.getPatient().getName() + ": " + message);

        // For GUI: Show dialog (only for current user)
        if (currentUser != null && currentUser.getId().equals(appointment.getPatient().getId())) {
            JOptionPane.showMessageDialog(null, message, "Appointment Reminder", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sendUrgentReminder(Appointment appointment) {
        String message = String.format(
                "URGENT: Your appointment with %s is in 1 hour (%s)",
                appointment.getProvider().getName(),
                new SimpleDateFormat("HH:mm").format(appointment.getDateTime())
        );

        System.out.println("URGENT notification: " + message);

        if (currentUser != null && currentUser.getId().equals(appointment.getPatient().getId())) {
            JOptionPane.showMessageDialog(null, message, "Urgent Reminder", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void sendPrescriptionRenewalReminder(Prescription prescription) {
        Date expiry = prescription.getExpiryDate();
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expiry);
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date weekBeforeExpiry = calendar.getTime();

        if (now.after(weekBeforeExpiry) && now.before(expiry)) {
            String message = String.format(
                    "PRESCRIPTION RENEWAL: Your prescription for %s expires on %s",
                    prescription.getMedication(),
                    new SimpleDateFormat("yyyy-MM-dd").format(expiry)
            );

            System.out.println("Prescription renewal reminder: " + message);

            // Optional: Show GUI notification for current user if they're the patient
            if (currentUser != null && currentUser.getId().equals(prescription.getPatient().getId())) {
                JOptionPane.showMessageDialog(null, message, "Prescription Renewal", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
