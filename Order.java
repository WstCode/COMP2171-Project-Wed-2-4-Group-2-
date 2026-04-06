import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class Order {
    private String orderID;
    private LocalDate orderDate;
    private OrderStatus status;
    private LocalDate deliveryDate;
    private List<OrderItem> items;
    private String customerID;
    private LocalDate pickupTime;
    private LocalDate completedDateTime;
    private String paymentID;
    private String customerName;


    public Order(String orderID, LocalDate orderDate, LocalDate deliveryDate, String customerID, String customerName) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.customerID = customerID;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }


    public boolean validateOrderData() {
        return orderID != null && !orderID.isBlank() &&
               customerID != null && !customerID.isBlank() &&
               deliveryDate != null && 
               items != null && !items.isEmpty();
    }

    public String getOrderDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order ID: %s%n", orderID));
        sb.append(String.format("Customer: %s (%s)%n", customerName, customerID));
        sb.append(String.format("Date: %s | Delivery: %s%n", orderDate, deliveryDate));
        sb.append(String.format("Status: %s%n", status));
        for (OrderItem item : items) {
            sb.append(String.format("- %s (x%d): $%.2f%n", 
                      item.getItemName(), item.getQuantity(), item.getSubTotal()));
        }
        return sb.toString();
    }

    public void addItem(OrderItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubTotal();
        }
        return total;
    }

    public void updateCustomerName(String name) {
        if (name != null && !name.isBlank()) {
            this.customerName = name;
        }
    }

    public void updateDeliveryDate(LocalDate d) {
        if (d != null) {
            this.deliveryDate = d;
        }
    }

    public void updateItems(List<OrderItem> items) {
        if (items != null && !items.isEmpty()) {
            this.items = items;
        }
    }

    /**
     * Checks if the deadline is within the specified threshold hours.
     */
    public boolean isDeadlineNear(int thresholdHours) {
        LocalDate due = getDueTime();
        if (due == null) return false;
        
        // Since due time is a LocalDate, the difference is converted to hours to compare
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), due);
        double hoursBetween = daysBetween * 24;
        
        // Return true if it is due within the threshold window (or is overdue/due today)
        return hoursBetween >= 0 && hoursBetween <= thresholdHours;
    }

    public LocalDate getDueTime() {
        // Defaults to pickupTime if set, otherwise falls back to deliveryDate
        return (pickupTime != null) ? pickupTime : deliveryDate;
    }

    public void markAsCompleted() {
        this.status = OrderStatus.COMPLETED;
        this.completedDateTime = LocalDate.now();
    }

    public void setPayment(Payment payment) {
        if (payment != null) {
            this.paymentID = payment.getPaymentID();
        }
    }

    // --- Enumerations & Accessors ---

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }

    public String getOrderID() { return orderID; }
    public LocalDate getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public List<OrderItem> getItems() { return items; }
    public String getCustomerID() { return customerID; }
    public String getPaymentID() { return paymentID; }
    public LocalDate getPickupTime() { return pickupTime; }
    public LocalDate getCompletedDateTime() { return completedDateTime; }
    public String getCustomerName() { return customerName; }

    public void setStatus(OrderStatus status) { this.status = status; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.updateDeliveryDate(deliveryDate); }
    public void setItems(List<OrderItem> items) { this.updateItems(items); }
    public void setCustomerName(String name) { this.updateCustomerName(name); }
    public void setPickupTime(LocalDate pickupTime) { this.pickupTime = pickupTime; }
}
