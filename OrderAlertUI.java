import java.util.ArrayList;
import java.util.List;

public class OrderAlertUI {

    private OrderManager orderManager;
    private OrderAlertService alertService;

    public OrderAlertUI(OrderManager orderManager, OrderAlertService alertService) {
        this.orderManager = orderManager;
        this.alertService = alertService;
    }

    // 🔹 Step 1: Get upcoming (alerted) orders
    public List<Order> viewActiveOrders() {
        List<OrderAlert> alerts = alertService.getActiveAlerts();
        List<Order> activeOrders = new ArrayList<>();

        if (alerts == null || alerts.isEmpty()) {
            return activeOrders;
        }

        for (OrderAlert alert : alerts) {
            Order order = orderManager.findOrderByID(alert.getOrderID());
            if (order != null) {
                activeOrders.add(order);
            }
        }

        return activeOrders;
    }

    // 🔹 Step 2: Display list of upcoming orders
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

    // 🔹 Step 3: Select order
    public Order selectOrder(String orderID) {
        Order order = orderManager.findOrderByID(orderID);

        if (order == null) {
            showError("Order not found.");
        }

        return order;
    }

    // 🔹 Step 4: Display full order details
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
