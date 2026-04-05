import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final String paymentRecordsFile;
    private static final String SEPARATOR = "----------------------------------";

    public PaymentRepository(String paymentRecordsFile) {
        this.paymentRecordsFile = paymentRecordsFile;
    }

    public void savePayment(Payment payment) {
        if (payment == null || !payment.validate()) {
            throw new IllegalArgumentException("Payment is null or invalid.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paymentRecordsFile, true))) {
            writer.write(payment.getPaymentDetails());
            writer.write(SEPARATOR);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving payment: " + e.getMessage());
        }
    }

    public Payment findPaymentByOrderID(String orderID) {
        if (orderID == null || orderID.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank.");
        }

        for (Payment payment : getAllPayments()) {
            if (payment.getOrderID().equals(orderID)) {
                return payment;
            }
        }

        return null;
    }

    public boolean updatePayment(Payment updatedPayment) {
        if (updatedPayment == null || !updatedPayment.validate()) {
            return false;
        }

        List<Payment> allPayments = getAllPayments();
        boolean found = false;

        for (int i = 0; i < allPayments.size(); i++) {
            if (allPayments.get(i).getOrderID().equals(updatedPayment.getOrderID())) {
                allPayments.set(i, updatedPayment);
                found = true;
                break;
            }
        }

        if (!found) {
            allPayments.add(updatedPayment);
        }

        rewritePaymentsFile(allPayments);
        return true;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        File file = new File(paymentRecordsFile);

        if (!file.exists()) {
            return payments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder block = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals(SEPARATOR)) {
                    if (block.length() > 0) {
                        Payment payment = parsePaymentBlock(block.toString().trim());
                        if (payment != null) {
                            payments.add(payment);
                        }
                        block = new StringBuilder();
                    }
                } else {
                    block.append(line).append("\n");
                }
            }

            if (block.length() > 0) {
                Payment payment = parsePaymentBlock(block.toString().trim());
                if (payment != null) {
                    payments.add(payment);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading payments: " + e.getMessage());
        }

        return payments;
    }

    private void rewritePaymentsFile(List<Payment> payments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paymentRecordsFile, false))) {
            for (Payment payment : payments) {
                writer.write(payment.getPaymentDetails());
                writer.write(SEPARATOR);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error rewriting payments file: " + e.getMessage());
        }
    }

    private Payment parsePaymentBlock(String block) {
        try {
            String[] lines = block.split("\n");

            String paymentID = "";
            String orderID = "";
            PaymentStatus status = null;
            PaymentMethod method = null;
            LocalDateTime lastUpdated = null;

            for (String line : lines) {
                line = line.trim();

                if (line.startsWith("Payment ID: ")) {
                    paymentID = line.substring("Payment ID: ".length()).trim();
                } else if (line.startsWith("Order ID: ")) {
                    orderID = line.substring("Order ID: ".length()).trim();
                } else if (line.startsWith("Status: ")) {
                    status = PaymentStatus.valueOf(line.substring("Status: ".length()).trim());
                } else if (line.startsWith("Method: ")) {
                    method = PaymentMethod.valueOf(line.substring("Method: ".length()).trim());
                } else if (line.startsWith("Last Updated: ")) {
                    lastUpdated = LocalDateTime.parse(line.substring("Last Updated: ".length()).trim());
                }
            }

            if (paymentID.isBlank() || orderID.isBlank() || status == null || method == null) {
                return null;
            }

            Payment payment = new Payment(paymentID, orderID, status, method);
            if (lastUpdated != null) {
                payment.setLastUpdated(lastUpdated);
            }

            return payment;

        } catch (Exception e) {
            System.out.println("Error parsing payment block: " + e.getMessage());
            return null;
        }
    }
}