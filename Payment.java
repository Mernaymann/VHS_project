// Payment.java
package vhs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment implements Serializable {
    private String paymentId;
    private double amount;
    private Date paymentDate;
    private String paymentMethod; // Credit Card, Debit Card, PayPal
    private String transactionStatus;

    public Payment(String paymentId, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = new Date();
        this.transactionStatus = "Completed";
    }

    // Getters and Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionStatus() { return transactionStatus; }
    public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "Payment ID: " + paymentId +
                ", Amount: $" + amount +
                ", Date: " + sdf.format(paymentDate) +
                ", Method: " + paymentMethod;
    }
}
