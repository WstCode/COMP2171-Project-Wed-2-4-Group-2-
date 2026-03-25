import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class Order {
    private String orderID;
    private LocalDate orderDate;
    private OrderStatus status;
    private LocalDate deliveryDate;
    private List<OrderItem> items;
    private String customerID;
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

    /**
     Validates required fields for order processing.
     */
    public boolean validateOrderData() {
        return orderID != null && !orderID.isBlank() &&
               customerID != null && !customerID.isBlank() &&
               customerName != null && !customerName.isBlank() &&
               deliveryDate != null && 
               items != null && !items.isEmpty();
    }

    /**
     * Calculates the total price by summing OrderItem subtotals.
     */
    public double getTotalPrice() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubTotal();
        }
        return total;
    }

    /**
     * Adds an item to the order.
     */
    public void addItem(OrderItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    /**
     * Formats order data for the OrderRepository
     */
    public String getOrderDetails() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Order ID: %s%n", orderID));
    sb.append(String.format("Customer: %s (%s)%n", customerName, customerID));
    sb.append(String.format("Date: %s | Delivery: %s%n", orderDate, deliveryDate));
    sb.append(String.format("Status: %s%n", status));
    sb.append("Items Ordered:\n");
    for (OrderItem item : items) {
        sb.append(String.format(" - %s (x%d): $%.2f%n",
                item.getItemName(), item.getQuantity(), item.getSubTotal()));
    }
    sb.append(String.format("Total Price: $%.2f", getTotalPrice()));
    return sb.toString();
}

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }

    // --- Getters & Setters ---

    public String getOrderID() { return orderID; }
    
    public LocalDate getOrderDate() { return orderDate; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    
    public String getCustomerID() { return customerID; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}