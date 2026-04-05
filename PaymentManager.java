public class PaymentManager {

    // Attribute
    private PaymentRepository paymentRepository;

    // Constructor
    public PaymentManager(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Retrieve payment by Order ID
    public Payment getPayment(String orderID) {
        return paymentRepository.findPaymentByOrderID(orderID);
    }

    // Update payment information (MAIN USE CASE METHOD)
    public boolean updatePayment(String orderID,
                                 PaymentInfo status,
                                 PaymentInfo method) {

        // Step 1: Find existing payment
        Payment payment = paymentRepository.findPaymentByOrderID(orderID);

        if (payment == null) {
            System.out.println("Error: Order not found");
            return false;
        }

        // Step 2: Update values
        payment.updateStatus(status);
        payment.updateMethod(method);

        // Step 3: Validate
        if (!payment.validate()) {
            System.out.println("Error: Invalid payment data");
            return false;
        }

        // Step 4: Save to file
        boolean updated = paymentRepository.updatePayment(payment);

        if (updated) {
            System.out.println("Payment recorded!");
            return true;
        } else {
            System.out.println("Error: Could not update payment");
            return false;
        }
    }

    // Validation helper (optional but good design)
    public boolean validatePayment(PaymentInfo status, PaymentInfo method) {

        // Check valid status
        boolean validStatus =
                (status == PaymentInfo.PAID ||
                 status == PaymentInfo.PENDING ||
                 status == PaymentInfo.OVERDUE);

        // Check valid method
        boolean validMethod =
                (method == PaymentInfo.POS_ON_DELIVERY ||
                 method == PaymentInfo.BANK_TRANSFER);

        return validStatus && validMethod;
    }
}
