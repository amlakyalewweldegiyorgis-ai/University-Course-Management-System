import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Payment implements java.io.Serializable {
    public int studentId;
    public Date date;
    public double amount;
    public String transactionId;
    public static List<Payment> payments = new ArrayList<>();

    // Payment Constructor: For payment slip generation
    public Payment(String transactionId, int studentId, double amount) {
        this.transactionId = transactionId;
        this.studentId = studentId;
        this.date = new Date();
        this.amount = amount;
    }

    // Payment Constructor: For loading data from DB
    public Payment(String transactionId, int studentId, Date date, double amount) {
        this.transactionId = transactionId;
        this.studentId = studentId;
        this.date = date;
        this.amount = amount;
    }

    private static boolean checkTransactionIdDuplication(String transactionId) {
        for (Payment payment : Payment.payments){
            if (Objects.equals(payment.getTransactionId(), transactionId)){
                return false;
            } else if (!transactionId.startsWith("GORP-")) {
                return false;
            }
        } return true;
    }

    // Helper method to validate the Transaction ID
    public static boolean isValidTransactionId(String transactionId) {
        return transactionId != null && transactionId.length() > 5 && checkTransactionIdDuplication(transactionId);
    }

    public static void addPaymentData(Payment payment) {
        payments.add(payment);
    }

    // Getters

    public String getTransactionId() {
        return transactionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String toString() {
        if (this.studentId == -1) {
            return String.format("  %-20s  %-19s  %-29s  %-8s", this.getTransactionId(), "DELETED STUDENT", this.getDate(), this.getAmount());
        }
        return String.format("  %-20s  %-19s  %-29s  %-8s", this.getTransactionId(), this.getStudentId(), this.getDate(), this.getAmount());
    }

}
