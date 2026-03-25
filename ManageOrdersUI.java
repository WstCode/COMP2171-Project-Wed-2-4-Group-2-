import java.time.LocalDate;
import java.util.List;
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

    private void editOrder(Order order) {
        displayEditForm(order);

        System.out.print("New Customer Name: ");
        String newCustomerName = scanner.nextLine();

        System.out.print("New Delivery Date (YYYY-MM-DD, leave blank to keep current): ");
        String deliveryDateInput = scanner.nextLine();

        LocalDate newDeliveryDate = null;
        if (!deliveryDateInput.isBlank()) {
            newDeliveryDate = LocalDate.parse(deliveryDateInput);
        }

        // Keeping current items for now
        List<OrderItem> newItems = null;

        boolean updated = orderManager.editOrder(
            order.getOrderID(),
            newCustomerName,
            newDeliveryDate,
            newItems
        );

        if (updated) {
            showSuccessMessage("Order updated successfully.");
        } else {
            showErrorMessage("Failed to update order.");
        }
    }

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

    public void displayOrderList() {
        List<Order> orders = orderManager.getAllOrders();

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