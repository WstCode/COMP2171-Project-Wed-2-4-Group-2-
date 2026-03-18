import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OrderRepository {
    private final String orderRecordsFile;

    public OrderRepository(String orderRecordsFile) {
        this.orderRecordsFile = orderRecordsFile;
    }

    public void saveOrder(Order order) throws IOException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRecordsFile, true))) {
            writer.write(order.getOrderDetails());
            writer.write("----------------------------------");
            writer.newLine();
        }
    }
}