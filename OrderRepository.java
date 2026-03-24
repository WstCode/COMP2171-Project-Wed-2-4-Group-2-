import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final String orderRecordsFile;
    private static final String SEPARATOR = "----------------------------------";

    public OrderRepository(String orderRecordsFile) {
        this.orderRecordsFile = orderRecordsFile;
    }

    public void saveOrder(Order order) throws IOException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRecordsFile, true))) {
            writer.write(order.getOrderDetails());
            writer.newLine();
            writer.write(SEPARATOR);
            writer.newLine();
        }
    }

    public List<String> getAllOrders() throws IOException {
        List<String> orderBlocks = new ArrayList<>();
        File file = new File(orderRecordsFile);

        if (!file.exists()) {
            return orderBlocks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder block = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals(SEPARATOR)) {
                    if (block.length() > 0) {
                        orderBlocks.add(block.toString().trim());
                        block = new StringBuilder();
                    }
                } else {
                    block.append(line).append("\n");
                }
            }
        }

        return orderBlocks;
    }

    public String findOrderById(String orderID) throws IOException {
        if (orderID == null || orderID.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }

        for (String block : getAllOrders()) {
            if (block.contains("Order ID: " + orderID)) {
                return block;
            }
        }

        return null;
    }

    public boolean updateOrder(Order order) throws IOException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        List<String> allOrders = getAllOrders();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRecordsFile, false))) {
            for (String block : allOrders) {
                if (block.contains("Order ID: " + order.getOrderID())) {
                    writer.write(order.getOrderDetails());
                    found = true;
                } else {
                    writer.write(block);
                }
                writer.newLine();
                writer.write(SEPARATOR);
                writer.newLine();
            }
        }

        return found;
    }

    public boolean deleteOrder(String orderID) throws IOException {
        if (orderID == null || orderID.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }

        List<String> allOrders = getAllOrders();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRecordsFile, false))) {
            for (String block : allOrders) {
                if (block.contains("Order ID: " + orderID)) {
                    found = true;
                } else {
                    writer.write(block);
                    writer.newLine();
                    writer.write(SEPARATOR);
                    writer.newLine();
                }
            }
        }

        return found;
    }
}