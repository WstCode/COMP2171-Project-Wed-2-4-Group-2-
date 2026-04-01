import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
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
            OrderArchive archivedOrderRepository = new OrderArchive("archived_orders.txt");
            OrderManager orderManager = new OrderManager(orderRepository, archivedOrderRepository);

            ManageOrdersUI ordersUI = new ManageOrdersUI(orderManager);

            boolean running = true;

            while (running) {
                System.out.println("\n=== Home Menu ===");
                System.out.println("1. View Active Orders");
                System.out.println("2. Edit/Delete Orders");
                System.out.println("3. Add Order");
                System.out.println("4. Add Customer");
                System.out.println("5. Exit");

                System.out.print("Select an option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        ordersUI.displayOrderList();
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
                        running = false;
                        System.out.println("Exiting system...");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void createOrderFlow(
            Scanner scanner,
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

        System.out.print("Delivery date (YYYY-MM-DD): ");
        LocalDate deliveryDate = LocalDate.parse(scanner.nextLine());

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