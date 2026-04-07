import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int ALERT_DAYS_THRESHOLD = 4;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            BakeryStaff staff = new BakeryStaff("S001", "pass123");

            System.out.println("=== Bakery Staff Login ===");

            System.out.print("Enter staff ID: ");
            String enteredID = scanner.nextLine();

            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            if (!staff.login(enteredID, enteredPassword)) {
                System.out.println("Login failed. Unauthorized user.");
                return;
            }

            System.out.println("Login successful.");

            CustomerRepository customerRepository = new CustomerRepository();
            CustomerManager customerManager = new CustomerManager(customerRepository);
            OrderRepository orderRepository = new OrderRepository("orders.txt");
            OrderArchive archiveOrderRepository = new OrderArchive("archived_orders.txt");
            PaymentRepository paymentRepository = new PaymentRepository("payments.txt");
            OrderManager orderManager = new OrderManager(orderRepository, archiveOrderRepository, paymentRepository);
            OrderAlertService alertService = new OrderAlertService(ALERT_DAYS_THRESHOLD);

            ManageOrdersUI ordersUI = new ManageOrdersUI(orderManager);

            boolean running = true;

            while (running) {
                System.out.println("\n=== Home Menu ===");
                System.out.println("1. View Active Orders");
                System.out.println("2. Edit/Delete Orders");
                System.out.println("3. Add Order");
                System.out.println("4. Add Customer");
                System.out.println("5. View Order Alerts");
                System.out.println("6. Complete Order");
                System.out.println("7. Manage Payment");
                System.out.println("8. Exit");

                System.out.print("Select an option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        viewActiveOrdersWithAlerts(orderManager, alertService, ordersUI);
                        break;

                    case "2":
                        ordersUI.start();
                        break;

                    case "3":
                        createOrderFlow(scanner, customerRepository, customerManager, orderManager, orderRepository);
                        break;

                    case "4":
                        createCustomerFlow(scanner, customerManager);
                        break;

                    case "5":
                        viewOrderAlerts(orderManager, alertService);
                        break;

                    case "6":
                        completeOrderFlow(scanner, orderManager, staff);
                        break;

                    case "7":
                        managePaymentFlow(scanner, orderManager);
                        break;

                    case "8":
                        running = false;
                        System.out.println("Exiting system...");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void viewActiveOrdersWithAlerts(OrderManager orderManager,
                                                   OrderAlertService alertService,
                                                   ManageOrdersUI ordersUI) {
        List<Order> activeOrders = orderManager.getActiveOrders();
        List<OrderAlert> alerts = alertService.checkUpcomingOrders(activeOrders);

        if (!alerts.isEmpty()) {
            System.out.println("\n=== Alerts ===");
            for (OrderAlert alert : alerts) {
                System.out.println(alert.getMessage());
            }
        }

        if (activeOrders.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }

        ordersUI.displayOrderList();
    }

    private static void viewOrderAlerts(OrderManager orderManager, OrderAlertService alertService) {
        System.out.println("\n=== Order Alerts ===");

        List<Order> activeOrders = orderManager.getActiveOrders();
        if (activeOrders.isEmpty()) {
            System.out.println("No active orders.");
            return;
        }

        List<OrderAlert> alerts = alertService.checkUpcomingOrders(activeOrders);
        if (alerts.isEmpty()) {
            System.out.println("No upcoming order deadlines at this time.");
            return;
        }

        for (OrderAlert alert : alerts) {
            System.out.println(alert.getMessage());
        }
    }

    private static void completeOrderFlow(Scanner scanner, OrderManager orderManager, BakeryStaff staff) {
        System.out.println("\n=== Complete Order ===");

        List<Order> activeOrders = orderManager.getActiveOrders();
        if (activeOrders.isEmpty()) {
            System.out.println("No active orders available.");
            return;
        }

        for (Order order : activeOrders) {
            System.out.println("Order ID: " + order.getOrderID()
                    + " | Customer ID: " + order.getCustomerID()
                    + " | Status: " + order.getStatus());
        }

        System.out.print("Enter Order ID to complete: ");
        String orderID = scanner.nextLine();

        Order order = orderManager.findOrderById(orderID);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("\nSelected Order:");
        System.out.println(order.getOrderDetails());

        System.out.print("Confirm completion? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Completion cancelled.");
            return;
        }

        boolean completed = orderManager.completeOrder(orderID, staff.getStaffID());

        if (completed) {
            System.out.println("Order marked as completed and archived.");
        } else {
            System.out.println("Failed to complete order.");
        }
    }

    private static void managePaymentFlow(Scanner scanner, OrderManager orderManager) {
        System.out.println("\n=== Manage Payment Information ===");

        List<Order> activeOrders = orderManager.getActiveOrders();
        if (activeOrders.isEmpty()) {
            System.out.println("No active orders available.");
            return;
        }

        for (Order order : activeOrders) {
            System.out.println("Order ID: " + order.getOrderID()
                    + " | Customer ID: " + order.getCustomerID()
                    + " | Status: " + order.getStatus());
        }

        System.out.print("Enter Order ID: ");
        String orderID = scanner.nextLine();

        Order selectedOrder = orderManager.findOrderById(orderID);
        if (selectedOrder == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("\nSelected Order:");
        System.out.println(selectedOrder.getOrderDetails());

        Payment existingPayment = orderManager.getPayment(orderID);
        if (existingPayment != null) {
            System.out.println("\nCurrent Payment Details:");
            System.out.println(existingPayment.getPaymentDetails());
        } else {
            System.out.println("\nNo existing payment record found for this order.");
        }

        PaymentInfo status = readPaymentStatus(scanner);
        if (status == null) {
            System.out.println("Invalid payment status.");
            return;
        }

        PaymentInfo method = readPaymentMethod(scanner);
        if (method == null) {
            System.out.println("Invalid payment method.");
            return;
        }

        if (!orderManager.validatePayment(status, method)) {
            System.out.println("Invalid payment data entered.");
            return;
        }

        System.out.print("Confirm payment update? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Payment update cancelled.");
            return;
        }

        boolean updated = orderManager.updatePayment(orderID, status, method);

        if (updated) {
            System.out.println("Payment recorded successfully.");
        } else {
            System.out.println("Failed to update payment information.");
        }
    }

    private static PaymentInfo readPaymentStatus(Scanner scanner) {
        System.out.println("Enter payment status:");
        System.out.println("1. PAID");
        System.out.println("2. PENDING");
        System.out.println("3. OVERDUE");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                return PaymentInfo.PAID;
            case "2":
                return PaymentInfo.PENDING;
            case "3":
                return PaymentInfo.OVERDUE;
            default:
                return null;
        }
    }

    private static PaymentInfo readPaymentMethod(Scanner scanner) {
        System.out.println("Enter payment method:");
        System.out.println("1. POS");
        System.out.println("2. TRANSFER");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                return PaymentInfo.POS_ON_DELIVERY;
            case "2":
                return PaymentInfo.BANK_TRANSFER;
            default:
                return null;
        }
    }

    private static void createOrderFlow(Scanner scanner,
                                        CustomerRepository customerRepository,
                                        CustomerManager customerManager,
                                        OrderManager orderManager,
                                        OrderRepository orderRepository) {

        System.out.println("\n=== Create Order ===");

        System.out.print("Enter customer ID: ");
        String customerID = scanner.nextLine();

        Customer customer = customerRepository.findCustomerById(customerID);

        if (customer == null) {
            System.out.println("Customer not found. Creating new customer.");

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            customer = customerManager.createCustomer(customerID, name, email, phone, address);

            if (customer == null) {
                System.out.println("Customer creation failed.");
                return;
            }
        }

        List<OrderItem> items = new ArrayList<>();

        System.out.print("Item name: ");
        String itemName = scanner.nextLine();

        System.out.print("Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        items.add(new OrderItem(itemName, qty, price));

        LocalDate deliveryDate = readDeliveryDate(scanner);
        if (deliveryDate == null) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        Order order = orderManager.createOrder(
                customer.getCustomerID(),
                customer.getName(),
                items,
                deliveryDate
        );

        if (order == null) {
            System.out.println("Order creation failed.");
            return;
        }

        try {
            orderRepository.saveOrder(order);
        } catch (Exception e) {
            System.out.println("Error saving order: " + e.getMessage());
            return;
        }

        System.out.println("Order created successfully!");
        System.out.println(order.getOrderDetails());
    }

    private static LocalDate readDeliveryDate(Scanner scanner) {
        System.out.print("Delivery date (YYYY-MM-DD): ");
        String input = scanner.nextLine();

        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e1) {
            try {
                DateTimeFormatter flexibleFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                return LocalDate.parse(input, flexibleFormatter);
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }

    private static void createCustomerFlow(Scanner scanner, CustomerManager manager) {
        System.out.println("\n=== Create Customer ===");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        Customer customer = manager.createCustomer(id, name, email, phone, address);

        if (customer != null) {
            System.out.println("Customer created successfully.");
        } else {
            System.out.println("Failed to create customer.");
        }
    }
}