import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private String orderRecordsFile;
    private String customerRecordsFile;
    private List<Order> activeOrders;

    public OrderManager(String orderRecordsFile, String customerRecordsFile) {
        this.orderRecordsFile = orderRecordsFile;
        this.customerRecordsFile = customerRecordsFile;
        this.activeOrders = new ArrayList<>();
    }

    public Order createOrder(Customer customer, List<OrderItem> details, LocalDate deliveryDate) {
        if (customer == null || details == null || details.isEmpty() || deliveryDate == null) {
            return null;
        }

        String orderID = "ORD" + (activeOrders.size() + 1);
        Order order = new Order(orderID, LocalDate.now(), deliveryDate, customer.getCustomerID());

        for (OrderItem item : details) {
            order.addItem(item);
        }

        if (!order.validateOrderData()) {
            return null;
        }

        activeOrders.add(order);
        return order;
    }

    public void saveOrder(Order order) {
        if (order == null) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRecordsFile, true))) {
            writer.write(order.getOrderDetails());
            writer.write("----------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }
}


