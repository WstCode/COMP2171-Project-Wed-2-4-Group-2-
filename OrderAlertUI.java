import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderAlertUI extends JFrame {
    private final OrderManager orderManager;
    private final OrderAlertService alertService;
    private final DefaultListModel<String> listModel;
    private final JList<String> alertList;

    public OrderAlertUI(OrderManager orderManager, OrderAlertService alertService) {
        this.orderManager = orderManager;
        this.alertService = alertService;

        setTitle("Order Alerts");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        alertList = new JList<>(listModel);

        JButton refreshBtn = new JButton("Refresh Alerts");
        JButton viewOrdersBtn = new JButton("View Orders Near Deadline");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewOrdersBtn);

        add(new JScrollPane(alertList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadAlerts());
        viewOrdersBtn.addActionListener(e -> showOrdersNearDeadline());

        loadAlerts();
        setVisible(true);
    }

    private void loadAlerts() {
        listModel.clear();

        List<OrderAlert> alerts = alertService.checkUpcomingOrders(orderManager.getActiveOrders());

        if (alerts.isEmpty()) {
            listModel.addElement("No upcoming order alerts.");
            return;
        }

        for (OrderAlert alert : alerts) {
            listModel.addElement(alert.getMessage());
        }
    }

    private void showOrdersNearDeadline() {
        List<Order> orders = orderManager.getOrdersWithApproachingDeadlines(4);

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders with approaching deadlines.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append("Order ID: ").append(order.getOrderID())
              .append(" | Customer: ").append(order.getCustomerName())
              .append(" | Delivery Date: ").append(order.getDeliveryDate())
              .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Orders Near Deadline", JOptionPane.INFORMATION_MESSAGE);
    }
}