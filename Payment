import java.time.LocalDateTime;

public class Payment {

    private String paymentID;
    private String orderID;
    private PaymentInfo status;
    private PaymentInfo method;
    private LocalDateTime lastUpdated;

    public Payment(String paymentID, String orderID, PaymentInfo status, PaymentInfo method) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.status = status;
        this.method = method;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getOrderID() {
        return orderID;
    }

    public PaymentInfo getStatus() {
        return status;
    }

    public PaymentInfo getMethod() {
        return method;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void updateStatus(PaymentInfo status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateMethod(PaymentInfo method) {
        this.method = method;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean validate() {

        // Valid status values
        if (!(status == PaymentInfo.PAID ||
              status == PaymentInfo.PENDING ||
              status == PaymentInfo.OVERDUE)) {
            return false;
        }

        // Valid method values
        if (!(method == PaymentInfo.POS_ON_DELIVERY ||
              method == PaymentInfo.BANK_TRANSFER)) {
            return false;
        }

        return true;
    }

    public String getPaymentDetails() {
        return "Payment ID: " + paymentID +
               "\nOrder ID: " + orderID +
               "\nStatus: " + status +
               "\nMethod: " + method +
               "\nLast Updated: " + lastUpdated;
    }
}
