import java.util.Scanner;

public class ManagePaymentUI {

    // Attributes
    private PaymentManager paymentManager;
    private OrderManager orderManager;
    private Scanner scanner;

    // Constructor
    public ManagePaymentUI(PaymentManager paymentManager, OrderManager orderManager) {
        this.paymentManager = paymentManager;
        this.orderManager = orderManager;
        this.scanner = new Scanner(System.in);
    }

    // Main method to run the use case
    public void managePayment() {

        // Step 1: Select Order
        System.out.print("Enter Order ID: ");
        String orderID = scanner.nextLine();

        Order order = orderManager.findOrderById(orderID);

        if (order == null) {
            showErrorMessage("Order not found");
            return;
        }

        // Step 2: Retrieve and display payment info
        Payment payment = paymentManager.getPayment(orderID);

        if (payment != null) {
            displayPaymentDetails(payment);
        } else {
            System.out.println("No existing payment record. Creating new one...");
        }

        // Step 3: Input payment details
        PaymentInfo status = inputStatus();
        PaymentInfo method = inputMethod();

        // Step 4: Validate before confirming
        if (!paymentManager.validatePayment(status, method)) {
            showErrorMessage("Invalid payment data");
            return;
        }

        // Step 5: Confirm update
        if (!confirmUpdate()) {
            System.out.println("Update cancelled. Returning to Home Menu...");
            return;
        }

        // Step 6: Update payment
        boolean success = paymentManager.updatePayment(orderID, status, method);

        // Step 7: Display result
        if (success) {
            showSuccessMessage();
        } else {
            showErrorMessage("Payment update failed");
        }
    }

    // Display payment details
    public void displayPaymentDetails(Payment payment) {
        System.out.println("\n--- Current Payment Details ---");
        System.out.println(payment.getPaymentDetails());
    }

    // Input Status
    private PaymentInfo inputStatus() {
        System.out.println("\nSelect Payment Status:");
        System.out.println("1. PAID");
        System.out.println("2. PENDING");
        System.out.println("3. OVERDUE");

        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        switch (choice) {
            case 1: return PaymentInfo.PAID;
            case 2: return PaymentInfo.PENDING;
            case 3: return PaymentInfo.OVERDUE;
            default:
                showErrorMessage("Invalid choice");
                return inputStatus(); // retry
        }
    }

    // Input Method
    private PaymentInfo inputMethod() {
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. POS_ON_DELIVERY");
        System.out.println("2. BANK_TRANSFER");

        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        switch (choice) {
            case 1: return PaymentInfo.POS_ON_DELIVERY;
            case 2: return PaymentInfo.BANK_TRANSFER;
            default:
                showErrorMessage("Invalid choice");
                return inputMethod(); // retry
        }
    }

    // Confirm update
    public boolean confirmUpdate() {
        System.out.print("\nConfirm update? (yes/no): ");
        String input = scanner.nextLine();

        return input.equalsIgnoreCase("yes");
    }

    // Success message
    public void showSuccessMessage() {
        System.out.println("Payment recorded!");
    }

    // Error message
    public void showErrorMessage(String message) {
        System.out.println("Error: " + message);
    }
}
