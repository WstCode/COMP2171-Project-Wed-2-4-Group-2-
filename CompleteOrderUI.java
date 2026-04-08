import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompleteOrderUI extends JFrame {
    private final OrderManager orderManager;
    private final BakeryStaff staff;
    private final DefaultListModel<String> listModel;
    private final JList<String> orderList;

    public CompleteOrderUI(OrderManager orderManager, BakeryStaff staff) {
        this.orderManager = orderManager;
        this.staff = staff;

        setTitle("Complete Order");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);

        JButton completeBtn = new JButton("Complete Selected Order");
        JButton refreshBtn = new JButton("Refresh");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(completeBtn);

        add(new JScrollPane(orderList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadOrders());
        completeBtn.addActionListener(e -> completeSelectedOrder());

        loadOrders();
        setVisible(true);
    }

    private void loadOrders() {
        listModel.clear();
        List<Order> orders = orderManager.getActiveOrders();
        for (Order order : orders) {
            listModel.addElement(order.getOrderID() + " | " + order.getCustomerName() + " | " + order.getStatus());
        }
    }

    private void completeSelectedOrder() {
        String selected = orderList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        String orderId = selected.split("\\|")[0].trim();

        boolean success = orderManager.completeOrder(orderId, staff.getStaffID());

        if (success) {
            JOptionPane.showMessageDialog(this, "Order completed and archived.");
            loadOrders();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to complete order.");
        }
    }
}