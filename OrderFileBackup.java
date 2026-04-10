import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OrderFileBackup {

    private final String fileName;
    private static final String SEPARATOR = "----------------------------------";

    public OrderFileBackup(String fileName) {
        this.fileName = fileName;
    }

    public void saveOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            writer.write("Order ID: " + order.getOrderID());
            writer.newLine();

            writer.write("Customer: " + order.getCustomerName() + " (" + order.getCustomerID() + ")");
            writer.newLine();

            writer.write("Date: " + order.getOrderDate() + " | Delivery: " + order.getDeliveryDate());
            writer.newLine();

            writer.write("Status: " + order.getStatus());
            writer.newLine();

            writer.write("Payment ID: " + (order.getPaymentID() != null ? order.getPaymentID() : "N/A"));
            writer.newLine();

            writer.write("Pickup Time: " + (order.getPickupTime() != null ? order.getPickupTime() : "N/A"));
            writer.newLine();

            writer.write("Completed Time: " + (order.getCompletedDateTime() != null ? order.getCompletedDateTime() : "N/A"));
            writer.newLine();

            for (OrderItem item : order.getItems()) {
                double subtotal = item.getQuantity() * item.getUnitPrice();

                writer.write(String.format(
                        "- %s (x%d): $%.2f",
                        item.getItemName(),
                        item.getQuantity(),
                        subtotal
                ));
                writer.newLine();
            }

            writer.write(SEPARATOR);
            writer.newLine();

            System.out.println("Order saved to backup file: " + fileName);

        } catch (IOException e) {
            System.out.println("Failed to write backup file: " + e.getMessage());
        }
    }
}
