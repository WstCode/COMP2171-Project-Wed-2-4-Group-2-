import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ManageOrdersUI {

    private BakeryStaff currentStaff;
    private OrderManager orderManager;
    private Scanner scanner;

    public ManageOrdersUI(BakeryStaff currentStaff, OrderManager orderManager) {
        this.currentStaff = currentStaff;
        this.orderManager = orderManager;
        this.scanner = new Scanner(System.in);
    }

    // Display all active orders
    public void displayOrderList() {
        List<Order> orders = orderManager.getActiveOrders();

        if (orders.isEmpty()) {
            System.out.println("No active orders found.");
            return;
        }

        System.out.println("=== Active Orders ===");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderID()
                    + " | Status: " + order.getStatus()
                    + " | Total: $" + order.getTotalPrice());
        }
    }

    // Select order by ID
    public String selectOrder() {
        System.out.print("Enter Order ID: ");
        return scanner.nextLine();
    }

    // Display order details
    public void displayOrderDetails(Order order) {
        if (order == null) {
            showErrorMessage();
            return;
        }

        System.out.println("\n=== Order Details ===");
        System.out.println("Order ID: " + order.getOrderID());
        System.out.println("Order Date: " + order.getOrderDate());
        System.out.println("Delivery Date: " + order.getDeliveryDate());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Items:");

        order.getItems().forEach(item -> {
            System.out.println("- " + item.getItemName()
                    + " x" + item.getQuantity()
                    + " ($" + item.getUnitPrice() + ")");
        });

        System.out.println("Total Price: $" + order.getTotalPrice());
    }

    // Display edit form
    public void displayEditForm(Order order) {
        if (order == null) {
            showErrorMessage();
            return;
        }

        System.out.println("Editing Order: " + order.getOrderID());

        System.out.print("Enter new delivery date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();

        Map<String, Object> updates = Map.of("deliveryDate", newDate);

        boolean updated = orderManager.editOrder(order.getOrderID(), updates);

        if (updated) {
            showSuccessMessage();
        } else {
            showErrorMessage();
        }
    }

    // Confirm deletion
    public boolean displayDeleteConfirmation() {
        System.out.print("Are you sure you want to delete this order? (yes/no): ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("yes");
    }

    // Success message
    public void showSuccessMessage() {
        System.out.println("Operation completed successfully.");
    }

    // Error message
    public void showErrorMessage() {
        System.out.println("An error occurred. Please try again.");
    }

    // Run simple UI menu (optional helper)
    public void run() {
        if (!currentStaff.isLoggedIn()) {
            System.out.println("Access denied. Staff not logged in.");
            return;
        }

        displayOrderList();
        String orderId = selectOrder();

        Order order = orderManager.findOrderById(orderId);

        if (order == null) {
            showErrorMessage();
            return;
        }

        displayOrderDetails(order);

        System.out.print("Edit (e) or Delete (d)? ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("e")) {
            displayEditForm(order);
        } else if (choice.equalsIgnoreCase("d")) {
            if (displayDeleteConfirmation()) {
                boolean deleted = orderManager.deleteOrder(orderId);
                if (deleted) {
                    showSuccessMessage();
                } else {
                    showErrorMessage();
                }
            }
        }
    }
}
