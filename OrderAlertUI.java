import java.util.ArrayList;
import java.util.List;

public class OrderAlertUI {

    private OrderManager orderManager;
    private OrderAlertService alertService;

    public OrderAlertUI(OrderManager orderManager, OrderAlertService alertService) {
        this.orderManager = orderManager;
        this.alertService = alertService;
    }

    public List<Order> viewActiveOrders() {
        List<OrderAlert> alerts = alertService.getActiveAlerts();
        List<Order> activeOrders = new ArrayList<>();

        if (alerts == null || alerts.isEmpty()) {
            return activeOrders;
        }

        for (OrderAlert alert : alerts) {
            Order order = orderManager.findOrderById(alert.getOrderID());
            if (order != null) {
                activeOrders.add(order);
            }
        }

        return activeOrders;
    }

    public void displayActiveOrders(List<Order> orders) {
        System.out.println("\nUpcoming Orders (Approaching Deadline):");

        for (Order order : orders) {
            System.out.println(
                "Order ID: " + order.getOrderID() +
                " | Customer: " + order.getCustomerName() +
                " | Delivery Date: " + order.getDeliveryDate()
            );
        }
    }

    public Order selectOrder(String orderID) {
        Order order = orderManager.findOrderById(orderID);

        if (order == null) {
            showError("Order not found.");
        }

        return order;
    }

    public void displayOrderDetails(Order order) {
        if (order == null) {
            showError("Invalid order.");
            return;
        }

        System.out.println("\nOrder Details:");
        System.out.println("Order ID: " + order.getOrderID());
        System.out.println("Customer Name: " + order.getCustomerName());
        System.out.println("Customer ID: " + order.getCustomerID());
        System.out.println("Order Date: " + order.getOrderDate());
        System.out.println("Delivery/Pickup Date: " + order.getDeliveryDate());
    }

    // 🔹 Error handling
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }
}
