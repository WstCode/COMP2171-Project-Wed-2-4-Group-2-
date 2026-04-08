import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderUI extends JFrame {
    private final OrderManager orderManager;
    private final CustomerManager customerManager;
    private final OrderRepository orderRepository;

    private JTextField customerIdField;
    private JTextField customerNameField;
    private JTextField itemNameField;
    private JTextField quantityField;
    private JTextField unitPriceField;
    private JTextField deliveryDateField;

    public CreateOrderUI(OrderManager orderManager,
                         CustomerManager customerManager,
                         OrderRepository orderRepository) {
        this.orderManager = orderManager;
        this.customerManager = customerManager;
        this.orderRepository = orderRepository;

        setTitle("Create Order");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Customer ID:"));
        customerIdField = new JTextField();
        add(customerIdField);

        add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        add(customerNameField);

        add(new JLabel("Item Name:"));
        itemNameField = new JTextField();
        add(itemNameField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        add(new JLabel("Unit Price:"));
        unitPriceField = new JTextField();
        add(unitPriceField);

        add(new JLabel("Delivery Date (YYYY-MM-DD):"));
        deliveryDateField = new JTextField();
        add(deliveryDateField);

        JButton createBtn = new JButton("Create Order");
        JButton cancelBtn = new JButton("Cancel");
        add(createBtn);
        add(cancelBtn);

        createBtn.addActionListener(e -> createOrder());
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void createOrder() {
        try {
            String customerID = customerIdField.getText().trim();
            String customerName = customerNameField.getText().trim();
            String itemName = itemNameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double unitPrice = Double.parseDouble(unitPriceField.getText().trim());
            LocalDate deliveryDate = LocalDate.parse(deliveryDateField.getText().trim());

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem(itemName, quantity, unitPrice));

            Order order = orderManager.createOrder(customerID, customerName, items, deliveryDate);

            if (order == null) {
                JOptionPane.showMessageDialog(this, "Order creation failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            orderRepository.saveOrder(order);
            JOptionPane.showMessageDialog(this, "Order created successfully.");
            dispose();

        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric and date values.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}