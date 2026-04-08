import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagePaymentUI extends JFrame {
    private final PaymentManager paymentManager;
    private final OrderManager orderManager;

    private JComboBox<String> statusBox;
    private JComboBox<String> methodBox;
    private DefaultListModel<String> listModel;
    private JList<String> orderList;

    public ManagePaymentUI(PaymentManager paymentManager, OrderManager orderManager) {
        this.paymentManager = paymentManager;
        this.orderManager = orderManager;

        setTitle("Manage Payment");
        setSize(550, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Payment Status:"));
        statusBox = new JComboBox<>(new String[]{"PAID", "PENDING", "OVERDUE"});
        formPanel.add(statusBox);

        formPanel.add(new JLabel("Payment Method:"));
        methodBox = new JComboBox<>(new String[]{"POS_ON_DELIVERY", "BANK_TRANSFER"});
        formPanel.add(methodBox);

        JButton refreshBtn = new JButton("Refresh Orders");
        JButton viewBtn = new JButton("View Current Payment");
        JButton saveBtn = new JButton("Save Payment");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(saveBtn);

        add(new JScrollPane(orderList), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadOrders());
        viewBtn.addActionListener(e -> viewPayment());
        saveBtn.addActionListener(e -> savePayment());

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

    private void viewPayment() {
        String orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        Payment payment = paymentManager.getPayment(orderId);

        if (payment == null) {
            JOptionPane.showMessageDialog(this, "No payment record found for this order.");
        } else {
            JTextArea area = new JTextArea(payment.getPaymentDetails());
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Payment Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void savePayment() {
        String orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        PaymentInfo status = PaymentInfo.valueOf((String) statusBox.getSelectedItem());
        PaymentInfo method = PaymentInfo.valueOf((String) methodBox.getSelectedItem());

        boolean success = paymentManager.updatePayment(orderId, status, method);

        if (success) {
            JOptionPane.showMessageDialog(this, "Payment saved successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save payment.");
        }
    }
}