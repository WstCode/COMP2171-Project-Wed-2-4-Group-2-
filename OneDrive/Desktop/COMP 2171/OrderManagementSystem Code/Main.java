import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BakeryStaff staff = new BakeryStaff("S001", "admin", "pass123");
        OrderManager manager = new OrderManager("orders.txt", "customers.txt");

        if (!staff.login("admin", "pass123")) {
            System.out.println("Login failed.");
            return;
        }

        Customer customer = new Customer("C001", "Janice Brown", "8761234567", "Spanish Town");
        staff.createCustomer(customer);

        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Birthday Cake", 1, 4500.00));
        items.add(new OrderItem("Cupcakes", 12, 250.00));

        staff.createOrder(manager, customer, items, LocalDate.of(2026, 3, 20));
    }
}
