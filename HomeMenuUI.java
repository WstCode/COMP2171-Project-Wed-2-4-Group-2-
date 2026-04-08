import javax.swing.*;
import java.awt.*;

public class HomeMenuUI extends JFrame {
    private final BakeryStaff staff;
    private final OrderManager orderManager;
    private final PaymentManager paymentManager;
    private final CustomerManager customerManager;
    private final OrderAlertService alertService;
    private final OrderRepository orderRepository;

    public HomeMenuUI(BakeryStaff staff,
                      OrderManager orderManager,
                      PaymentManager paymentManager,
                      CustomerManager customerManager,
                      OrderAlertService alertService,
                      OrderRepository orderRepository) {
        this.staff = staff;
        this.orderManager = orderManager;
        this.paymentManager = paymentManager;
        this.customerManager = customerManager;
        this.alertService = alertService;
        this.orderRepository = orderRepository;

        setTitle("DoughJamaica Order Management");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel("Welcome, " + staff.getStaffID(), SwingConstants.CENTER);
        JButton activeOrdersBtn = new JButton("View Active Orders");
        JButton editDeleteBtn = new JButton("Edit / Delete Orders");
        JButton addOrderBtn = new JButton("Add Order");
        JButton addCustomerBtn = new JButton("Add Customer");
        JButton alertsBtn = new JButton("View Order Alerts");
        JButton completeBtn = new JButton("Complete Order");
        JButton paymentBtn = new JButton("Manage Payment");
        JButton logoutBtn = new JButton("Logout");

        panel.add(welcome);
        panel.add(activeOrdersBtn);
        panel.add(editDeleteBtn);
        panel.add(addOrderBtn);
        panel.add(addCustomerBtn);
        panel.add(alertsBtn);
        panel.add(completeBtn);
        panel.add(paymentBtn);
        panel.add(logoutBtn);

        add(panel);

        activeOrdersBtn.addActionListener(e -> showActiveOrders());
        editDeleteBtn.addActionListener(e -> new ManageOrdersUI(orderManager));
        addOrderBtn.addActionListener(e -> new CreateOrderUI(orderManager, customerManager, orderRepository));
        addCustomerBtn.addActionListener(e -> new CreateCustomerUI(customerManager));
        alertsBtn.addActionListener(e -> new OrderAlertUI(orderManager, alertService));
        completeBtn.addActionListener(e -> new CompleteOrderUI(orderManager, staff));
        paymentBtn.addActionListener(e -> new ManagePaymentUI(paymentManager, orderManager));
        logoutBtn.addActionListener(e -> logout());

        setVisible(true);
    }

    private void showActiveOrders() {
        java.util.List<Order> orders = orderManager.getActiveOrders();

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active orders available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append("Order ID: ").append(order.getOrderID())
              .append(" | Customer: ").append(order.getCustomerName())
              .append(" | Status: ").append(order.getStatus())
              .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Active Orders", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginUI(staff, orderManager, paymentManager, customerManager, alertService, orderRepository);
        }
    }
}