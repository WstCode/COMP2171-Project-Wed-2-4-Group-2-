public class PaymentManager {
    private final PaymentRepository paymentRepository;

    public PaymentManager(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment getPayment(String orderID) {
        return paymentRepository.findPaymentByOrderID(orderID);
    }

    public boolean updatePayment(String orderID, PaymentInfo status, PaymentInfo method) {
        if (orderID == null || orderID.isBlank()) {
            return false;
        }

        if (!validatePayment(status, method)) {
            return false;
        }

        Payment payment = paymentRepository.findPaymentByOrderID(orderID);

        if (payment == null) {
            String paymentID = "PAY" + (paymentRepository.getAllPayments().size() + 1);
            payment = new Payment(paymentID, orderID, status, method);
            paymentRepository.savePayment(payment);
            return true;
        }

        payment.updateStatus(status);
        payment.updateMethod(method);

        return paymentRepository.updatePayment(payment);
    }

    public boolean validatePayment(PaymentInfo status, PaymentInfo method) {
        return status != null && method != null && status.isStatus() && method.isMethod();
    }
}