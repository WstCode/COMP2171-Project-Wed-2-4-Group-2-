import javax.swing.SwingUtilities;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection conn = DatabaseService.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            BakeryStaff staff = new BakeryStaff("S001", "pass123");

            CustomerRepository customerRepository = new CustomerRepository();
            CustomerManager customerManager = new CustomerManager(customerRepository);

            OrderRepository orderRepository = new OrderRepository(orders.txt);
            OrderArchive orderArchive = new OrderArchive("archived_orders.txt");
            PaymentRepository paymentRepository = new PaymentRepository(payments.txt);

            OrderManager orderManager = new OrderManager(orderRepository, orderArchive, paymentRepository);
            PaymentManager paymentManager = new PaymentManager(paymentRepository);
            OrderAlertService alertService = new OrderAlertService(4);

            new LoginUI(staff, orderManager, paymentManager, customerManager, alertService, orderRepository);
        });
    }
}
