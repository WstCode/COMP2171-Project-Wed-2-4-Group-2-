import java.awt.*;
import java.util.List;
import javax.swing.*;

public class CompleteOrderUI extends JFrame {

    private final OrderManager orderManager;
    private final BakeryStaff staff;

    private JList<String> orderList;
    private DefaultListModel<String> listModel;

    public CompleteOrderUI(OrderManager orderManager, BakeryStaff staff) {
        this.orderManager = orderManager;
        this.staff = staff;

        setTitle("Complete Customer Order");
        setSize(400, 400);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);

        JButton completeBtn = new JButton("Complete Order");
        JButton cancelBtn = new JButton("Close");

        loadOrders();

        completeBtn.addActionListener(e -> completeOrder());
        cancelBtn.addActionListener(e -> dispose());

        setLayout(new BorderLayout());
        add(new JScrollPane(orderList), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(completeBtn);
        bottom.add(cancelBtn);

        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    // STEP 1 & 2
    private void loadOrders() {
        List<Order> orders = orderManager.getActiveOrders();

        // EXTENSION 2.1
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active orders.");
            dispose();
            return;
        }

        listModel.clear();
        for (Order order : orders) {
            listModel.addElement(order.getOrderID() + " - " + order.getCustomerName());
        }
    }

    // STEP 3–9
    private void completeOrder() {

        int selectedIndex = orderList.getSelectedIndex();

        // EXTENSION 3.1
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order.");
            return;
        }

        String selectedValue = listModel.getElementAt(selectedIndex);
        String orderID = selectedValue.split(" - ")[0];

        // STEP 4 & 5 – Confirm action
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Complete this order?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        // EXTENSION 5.1
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // STEP 6–8
        boolean success = orderManager.completeOrder(orderID, staff.getStaffID());

        if (!success) {
            // EXTENSION 6.1
            JOptionPane.showMessageDialog(this, "Error: Unable to complete order.");
            return;
        }

        // STEP 9
        JOptionPane.showMessageDialog(this, "Order completed and archived successfully.");

        loadOrders(); // refresh list
    }
}
