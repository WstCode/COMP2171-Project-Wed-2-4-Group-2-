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

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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
        return paymentID != null && !paymentID.isBlank()
                && orderID != null && !orderID.isBlank()
                && status != null && status.isStatus()
                && method != null && method.isMethod()
                && lastUpdated != null;
    }

    public String getPaymentDetails() {
        return "Payment ID: " + paymentID +
               "\nOrder ID: " + orderID +
               "\nStatus: " + status +
               "\nMethod: " + method +
               "\nLast Updated: " + lastUpdated;
    }
}