import javax.swing.*;
import java.awt.*;

public class CreateCustomerUI extends JFrame {
    private final CustomerManager customerManager;

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;

    public CreateCustomerUI(CustomerManager customerManager) {
        this.customerManager = customerManager;

        setTitle("Create Customer");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Customer ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        JButton saveButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");
        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(e -> createCustomer());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void createCustomer() {
        Customer customer = customerManager.createCustomer(
                idField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                addressField.getText().trim()
        );

        if (customer != null) {
            JOptionPane.showMessageDialog(this, "Customer created successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}