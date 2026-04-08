import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ManageOrdersUI extends JFrame {
    private final OrderManager orderManager;
    private final DefaultListModel<String> listModel;
    private final JList<String> orderList;

    public ManageOrdersUI(OrderManager orderManager) {
        this.orderManager = orderManager;

        setTitle("Manage Orders");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);

        JButton refreshBtn = new JButton("Refresh");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(new JScrollPane(orderList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadOrders());
        editBtn.addActionListener(e -> editSelectedOrder());
        deleteBtn.addActionListener(e -> deleteSelectedOrder());

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

    private String getSelectedOrderId() {
        String selected = orderList.getSelectedValue();
        if (selected == null) return null;
        return selected.split("\\|")[0].trim();
    }

    private void editSelectedOrder() {
        String orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        Order order = orderManager.findOrderById(orderId);
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order not found.");
            return;
        }

        String newName = JOptionPane.showInputDialog(this, "New Customer Name:", order.getCustomerName());
        String newDateText = JOptionPane.showInputDialog(this, "New Delivery Date (YYYY-MM-DD):", order.getDeliveryDate());

        LocalDate newDate = null;
        try {
            if (newDateText != null && !newDateText.isBlank()) {
                newDate = LocalDate.parse(newDateText.trim());
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format.");
            return;
        }

        boolean updated = orderManager.editOrder(orderId, newName, newDate, null);

        if (updated) {
            JOptionPane.showMessageDialog(this, "Order updated.");
            loadOrders();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update order.");
        }
    }

    private void deleteSelectedOrder() {
        String orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete order " + orderId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean deleted = orderManager.deleteOrder(orderId);

        if (deleted) {
            JOptionPane.showMessageDialog(this, "Order deleted.");
            loadOrders();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete order.");
        }
    }
}