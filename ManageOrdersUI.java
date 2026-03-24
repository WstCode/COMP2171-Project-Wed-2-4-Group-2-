import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ManageOrdersUI {

    private OrderManager orderManager;
    private Scanner scanner;

    public ManageOrdersUI(OrderManager orderManager) {
        this.orderManager = orderManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        displayOrderList();

        String orderId = selectOrder();
        Order order = orderManager.findOrderById(orderId);

        if (order == null) {
            showErrorMessage("Order not found.");
            return;
        }

        displayOrderDetails(order);

        System.out.print("\nSelect Option: (E)dit or (D)elete: ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("E")) {
            editOrder(order);
        } else if (choice.equalsIgnoreCase("D")) {
            deleteOrder(order);
        } else {
            showErrorMessage("Invalid option selected.");
        }
    }

    /* ================= EDIT FLOW ================= */

    private void editOrder(Order order) {

        displayEditForm(order);

        Map<String, Object> updates = collectUpdatedData();

        boolean updated = orderManager.editOrder(order.getOrderID(), updates);

        if (updated) {
            showSuccessMessage("Order updated successfully.");
        } else {
            showErrorMessage("Failed to update order.");
        }
    }

    /* ================= DELETE FLOW ================= */

    private void deleteOrder(Order order) {

        if (!displayDeleteConfirmation()) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean deleted = orderManager.deleteOrder(order.getOrderID());

        if (deleted) {
            showSuccessMessage("Order deleted successfully.");
        } else {
            showErrorMessage("Failed to delete order.");
        }
    }

    /* ================= SUPPORT METHODS ================= */

    public void displayOrderList() {
        List<Order> orders = orderManager.getActiveOrders();

        System.out.println("\n=== Existing Orders ===");

        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }

        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderID()
                    + " | Customer: " + order.getCustomerID()
                    + " | Status: " + order.getStatus());
        }
    }

    public String selectOrder() {
        System.out.print("\nEnter Order ID: ");
        return scanner.nextLine();
    }

    public void displayOrderDetails(Order order) {
        System.out.println("\n=== Order Details ===");
        System.out.println("Order ID: " + order.getOrderID());
        System.out.println("Customer ID: " + order.getCustomerID());
        System.out.println("Order Date: " + order.getOrderDate());
        System.out.println("Delivery Date: " + order.getDeliveryDate());
        System.out.println("Status: " + order.getStatus());

        order.getItems().forEach(item -> {
            System.out.println("- " + item.getItemName()
                    + " | Qty: " + item.getQuantity()
                    + " | Unit Price: $" + item.getUnitPrice());
        });

        System.out.println("Total: $" + order.getTotalPrice());
    }

    public void displayEditForm(Order order) {
        System.out.println("\n=== Edit Order ===");
        System.out.println("Leave blank to keep current values.");
    }

    private Map<String, Object> collectUpdatedData() {
        Map<String, Object> updates = new HashMap<>();

        System.out.print("New Customer ID: ");
        String customerId = scanner.nextLine();
        if (!customerId.isBlank()) {
            updates.put("customerID", customerId);
        }

        System.out.print("New Delivery Date (YYYY-MM-DD): ");
        String deliveryDate = scanner.nextLine();
        if (!deliveryDate.isBlank()) {
            updates.put("deliveryDate", deliveryDate);
        }

        System.out.print("New Status: ");
        String status = scanner.nextLine();
        if (!status.isBlank()) {
            updates.put("status", status);
        }

        return updates;
    }

    public boolean displayDeleteConfirmation() {
        System.out.print("Are you sure you want to delete this order? (yes/no): ");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("yes");
    }

    public void showSuccessMessage(String message) {
        System.out.println("\nSUCCESS: " + message);
    }

    public void showErrorMessage(String message) {
        System.out.println("\nERROR: " + message);
    }
}
