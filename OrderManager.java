import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final List<Order> activeOrders;
    private final OrderRepository repository;

    public OrderManager(OrderRepository repository) {
        this.repository = repository;
        this.activeOrders = new ArrayList<>();
    }

    public Order createOrder(String customerID, String customerName, List<OrderItem> details, LocalDate deliveryDate) {
        if (customerID == null || customerID.isBlank()
                || customerName == null || customerName.isBlank()
                || details == null || details.isEmpty()
                || deliveryDate == null) {
            return null;
        }

        String orderID = "ORD" + (repository.getAllOrders().size() + 1);
        Order order = new Order(orderID, LocalDate.now(), deliveryDate, customerID, customerName);

        for (OrderItem item : details) {
            order.addItem(item);
        }

        if (!order.validateOrderData()) {
            return null;
        }

        activeOrders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return repository.getAllOrders();
    }

    public Order findOrderById(String orderID) {
        return repository.findOrderById(orderID);
    }

    public boolean editOrder(String orderID, String newCustomerName, LocalDate newDeliveryDate, List<OrderItem> newItems) {
        Order order = repository.findOrderById(orderID);

        if (order == null) {
            return false;
        }

        if (newCustomerName != null && !newCustomerName.isBlank()) {
            order.setCustomerName(newCustomerName);
        }

        if (newDeliveryDate != null) {
            order.setDeliveryDate(newDeliveryDate);
        }

        if (newItems != null && !newItems.isEmpty()) {
            order.setItems(newItems);
        }

        if (!order.validateOrderData()) {
            return false;
        }

        return repository.updateOrder(order);
    }

    public boolean deleteOrder(String orderID) {
        Order order = repository.findOrderById(orderID);

        if (order == null) {
            return false;
        }

        activeOrders.remove(order);
        return repository.deleteOrder(orderID);
    }
}