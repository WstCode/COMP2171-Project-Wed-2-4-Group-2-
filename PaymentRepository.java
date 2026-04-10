import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    private final Connection conn;
    private final PaymentFileBackup backupP;

    public PaymentRepository(Connection conn) {
        this.conn = conn;
        this.backupP = new PaymentFileBackup("payments.txt");
    }

    public void savePayment(Payment payment) {
        if (payment == null || !payment.validate()) {
            throw new IllegalArgumentException("Payment is null or invalid.");
        }

        try {
            String sql = "INSERT INTO payments (payment_id, order_id, status, method, last_updated) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, payment.getPaymentID());
            stmt.setString(2, payment.getOrderID());
            stmt.setString(3, payment.getStatus().name());
            stmt.setString(4, payment.getMethod().name());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getLastUpdated()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DB FAILED → writing to backup file: " + e.getMessage());

            backupP.savePayment(payment);
        }
    }

    public Payment findPaymentByOrderID(String orderID) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractPayment(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error finding payment: " + e.getMessage());
        }

        return null;
    }

    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payments SET status = ?, method = ?, last_updated = ? WHERE order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, payment.getStatus().name());
            stmt.setString(2, payment.getMethod().name());
            stmt.setTimestamp(3, Timestamp.valueOf(payment.getLastUpdated()));
            stmt.setString(4, payment.getOrderID());

            int rows = stmt.executeUpdate();

            // If no row updated → insert instead (upsert behavior)
            if (rows == 0) {
                savePayment(payment);
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payments";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(extractPayment(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving payments: " + e.getMessage());
        }

        return payments;
    }

    private Payment extractPayment(ResultSet rs) throws SQLException {
        String paymentID = rs.getString("payment_id");
        String orderID = rs.getString("order_id");
        PaymentInfo status = PaymentInfo.valueOf(rs.getString("status"));
        PaymentInfo method = PaymentInfo.valueOf(rs.getString("method"));
        LocalDateTime lastUpdated = rs.getTimestamp("last_updated").toLocalDateTime();

        Payment payment = new Payment(paymentID, orderID, status, method);
        payment.setLastUpdated(lastUpdated);

        return payment;
    }
}
