import java.io.*;
import java.time.LocalDate;
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

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        File file = new File(orderRecordsFile);

        if (!file.exists()) {
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder block = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals(SEPARATOR)) {
                    if (block.length() > 0) {
                        Order order = parseOrderBlock(block.toString().trim());
                        if (order != null) {
                            orders.add(order);
                        }
                        block = new StringBuilder();
                    }
                } else {
                    block.append(line).append("\n");
                }
            }

            if (block.length() > 0) {
                Order order = parseOrderBlock(block.toString().trim());
                if (order != null) {
                    orders.add(order);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading orders: " + e.getMessage());
        }

        return orders;
    }

    public Order findOrderById(String orderID) {
        if (orderID == null || orderID.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }

        for (Order order : getAllOrders()) {
            if (order.getOrderID().equals(orderID)) {
                return order;
            }
        }

        return null;
    }

    public boolean updateOrder(Order updatedOrder) {
        if (updatedOrder == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        List<Order> allOrders = getAllOrders();
        boolean found = false;

        for (int i = 0; i < allOrders.size(); i++) {
            if (allOrders.get(i).getOrderID().equals(updatedOrder.getOrderID())) {
                allOrders.set(i, updatedOrder);
                found = true;
                break;
            }
        }

        if (found) {
            rewriteAllOrders(allOrders);
        }

        return found;
    }

    public boolean deleteOrder(String orderID) {
        if (orderID == null || orderID.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }

        List<Order> allOrders = getAllOrders();
        boolean found = false;

        for (int i = 0; i < allOrders.size(); i++) {
            if (allOrders.get(i).getOrderID().equals(orderID)) {
                allOrders.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            rewriteAllOrders(allOrders);
        }

        return found;
    }

    private void rewriteAllOrders(List<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRecordsFile, false))) {
            for (Order order : orders) {
                writer.write(order.getOrderDetails());
                writer.newLine();
                writer.write(SEPARATOR);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error rewriting orders: " + e.getMessage());
        }
    }

    private Order parseOrderBlock(String block) {
    try {
        String[] lines = block.split("\n");

        String orderID = "";
        String customerID = "";
        String customerName = "";
        LocalDate orderDate = null;
        LocalDate deliveryDate = null;
        Order.OrderStatus status = Order.OrderStatus.PENDING;
        List<OrderItem> items = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Order ID: ")) {
                orderID = line.substring("Order ID: ".length()).trim();

            } else if (line.startsWith("Customer: ")) {
                String customerPart = line.substring("Customer: ".length()).trim();
                int open = customerPart.lastIndexOf('(');
                int close = customerPart.lastIndexOf(')');
                if (open != -1 && close != -1 && open < close) {
                    customerName = customerPart.substring(0, open).trim();
                    customerID = customerPart.substring(open + 1, close).trim();
                }

            } else if (line.startsWith("Date: ")) {
                String datePart = line.substring("Date: ".length()).trim();
                String[] pieces = datePart.split("\\|");
                orderDate = LocalDate.parse(pieces[0].trim());
                deliveryDate = LocalDate.parse(
                    pieces[1].replace("Delivery:", "").trim()
                );

            } else if (line.startsWith("Status: ")) {
                String s = line.substring("Status: ".length()).trim();
                status = Order.OrderStatus.valueOf(s);

            } else if (line.startsWith("-") || line.startsWith(" - ")) {
                String itemLine = line.replaceFirst("^-\\s*", "").replaceFirst("^\\s*-\\s*", "").trim();

                int qtyStart = itemLine.indexOf("(x");
                int qtyEnd = itemLine.indexOf("):");
                int priceIndex = itemLine.lastIndexOf("$");

                if (qtyStart != -1 && qtyEnd != -1 && priceIndex != -1) {
                    String itemName = itemLine.substring(0, qtyStart).trim();
                    int quantity = Integer.parseInt(itemLine.substring(qtyStart + 2, qtyEnd).trim());
                    double subtotal = Double.parseDouble(itemLine.substring(priceIndex + 1).trim());

                    double unitPrice = subtotal / quantity;
                    items.add(new OrderItem(itemName, quantity, unitPrice));
                }
            }
        }

        if (orderID.isBlank() || customerID.isBlank() || customerName.isBlank()
                || orderDate == null || deliveryDate == null) {
            return null;
        }

        Order order = new Order(orderID, orderDate, deliveryDate, customerID, customerName);
        order.setStatus(status);

        for (OrderItem item : items) {
            order.addItem(item);
        }

        return order;

    } catch (Exception e) {
        System.out.println("Error parsing order block: " + e.getMessage());
        return null;
    }
}
}
