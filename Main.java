import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BakeryStaff staff = new BakeryStaff("S001",  "pass123");

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
        OrderManager orderManager = new OrderManager(orderRepository);

        //ManageOrdersUI init
        ManageOrdersUI ordersUI = new ManageOrdersUI(manager);

        ordersUI.start();

        System.out.println("Create Order selected.");

        System.out.print("Enter customer ID: ");
        String customerID = scanner.nextLine();

        Customer customer = customerRepository.findCustomerById(customerID);

        if (customer != null) {
            System.out.println("Existing customer selected.");
        } else {
            System.out.println("Customer not found. Creating new customer.");

            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            customer = customerManager.createCustomer(
                customerID, name, email, phoneNumber, address
            );

            if (customer == null) {
                System.out.println("Customer creation failed.");
                scanner.close();
                return;
            }

            System.out.println("Customer created successfully.");
        }

        List<OrderItem> items = new ArrayList<>();

        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter unit price: ");
        double unitPrice = Double.parseDouble(scanner.nextLine());

        items.add(new OrderItem(itemName, quantity, unitPrice));

        System.out.print("Do you want to add another item? (Y/N): ");
        String addAnother = scanner.nextLine();

        if (addAnother.equalsIgnoreCase("Y")) {
            System.out.print("Enter second item name: ");
            String secondItemName = scanner.nextLine();

            System.out.print("Enter quantity: ");
            int secondQuantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter unit price: ");
            double secondUnitPrice = Double.parseDouble(scanner.nextLine());

            items.add(new OrderItem(secondItemName, secondQuantity, secondUnitPrice));
        }

        System.out.print("Enter delivery date (YYYY-MM-DD): ");
        LocalDate deliveryDate = LocalDate.parse(scanner.nextLine());

        Order order = orderManager.createOrder(customer, items, deliveryDate);

        if (order == null) {
            System.out.println("Invalid order details.");
            scanner.close();
            return;
        }

        System.out.print("Confirm order entry? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Order entry discarded.");
            scanner.close();
            return;
        }

        try {
            orderRepository.saveOrder(order);
        } catch (IOException e) {
            System.out.println("Failed to save order: " + e.getMessage());
            scanner.close();
            return;
        }

        System.out.println("Order created successfully.");
        System.out.println(order.getOrderDetails());

        scanner.close();
    }
}
