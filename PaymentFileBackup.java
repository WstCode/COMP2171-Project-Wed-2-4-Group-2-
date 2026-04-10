import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class PaymentFileBackup {

    private final String filePath;
    private static final String SEPARATOR = "----------------------------------";

    public PaymentFileBackup(String filePath) {
        this.filePath = filePath;
    }

    public void savePayment(Payment payment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            writer.write("Payment ID: " + payment.getPaymentID());
            writer.newLine();

            writer.write("Order ID: " + payment.getOrderID());
            writer.newLine();

            writer.write("Status: " + payment.getStatus().name());
            writer.newLine();

            writer.write("Method: " + payment.getMethod().name());
            writer.newLine();

            writer.write("Last Updated: " + LocalDateTime.now());
            writer.newLine();

            writer.write(SEPARATOR);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("CRITICAL: Failed to write payment backup file: " + e.getMessage());
        }
    }
}
