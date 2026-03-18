import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final List<Order> activeOrders;

    public OrderManager() {
        this.activeOrders = new ArrayList<>();
    }

    public Order createOrder(Customer customer, List<OrderItem> details, LocalDate deliveryDate) {
        if (customer == null) {
            return null;
        }

        if (details == null || details.isEmpty() || deliveryDate == null) {
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

    public List<Order> getActiveOrders() {
        return activeOrders;
    }
}