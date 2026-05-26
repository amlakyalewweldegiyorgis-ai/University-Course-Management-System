import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payment implements java.io.Serializable {
    public String studentName;
    public LocalDate date;
    public double amount;
    public String transactionId;
    public static List<Payment> payments = new ArrayList<>();

    // Payment Constructor: For payment slip generation
    public Payment(String transactionId, String studentName, double amount) {
        this.transactionId = transactionId;
        this.studentName = studentName;
        this.date = LocalDate.now();
        this.amount = amount;
    }

    // Helper method to validate the Transaction ID
    public static boolean isValidTransactionId(String transactionId) {
        return transactionId != null && transactionId.length() > 5;
    }

    // Getter
    public String getStudentName() {
        return studentName;
    }

    // Setter
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
