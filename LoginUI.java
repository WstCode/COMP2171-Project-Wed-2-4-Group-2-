import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private final BakeryStaff staff;
    private final OrderManager orderManager;
    private final PaymentManager paymentManager;
    private final CustomerManager customerManager;
    private final OrderAlertService alertService;
    private final OrderRepository orderRepository;

    private JTextField staffIdField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginUI(BakeryStaff staff,
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

        setTitle("DoughJamaica Login");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPanel.add(new JLabel("Staff ID:"));
        staffIdField = new JTextField();
        formPanel.add(staffIdField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginButton = new JButton("Login");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {
        String enteredID = staffIdField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword());

        if (staff.login(enteredID, enteredPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful.");
            dispose();
            new HomeMenuUI(staff, orderManager, paymentManager, customerManager, alertService, orderRepository);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid ID or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}