import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a customer order within the bakery's management system.
 */
public class Order {
    private String orderID;
    private LocalDate orderDate;
    private OrderStatus status;
    private LocalDate deliveryDate;
    private List<OrderItem> items;
    private String customerID;

    /**
     * Constructor for a new Order.
     */
public Order(String orderID, LocalDate orderDate, LocalDate deliveryDate, String customerID) {
    this.orderID = orderID;
    this.orderDate = orderDate;
    this.deliveryDate = deliveryDate; 
    this.customerID = customerID;
    this.items = new ArrayList<>();
    this.status = OrderStatus.PENDING;
}

    /**
     * Validates order completeness as required by the CRC model
     * @return true if orderID, customerID, and items list are valid.
     */
  public boolean validateOrderData() {
        return orderID != null && customerID != null && items != null && !items.isEmpty();
    }

    /**
     * Generates a formatted string of order information for staff 
     * Supports the requirement to display order details 
     * @return Formatted string containing ID, Status, and associated Customer.
     */
public String getOrderDetails() {
        return String.format("Order ID: %s | Customer: %s | Status: %s", 
                             orderID, customerID, status);
    }

  /**
 * Defines valid states for a bakery order
 */
public enum OrderStatus {
    PENDING,
    COMPLETED
}

public void addItem(OrderItem item) {
    this.items.add(item);
}

public double getTotalPrice() {
    return items.stream().mapToDouble(OrderItem::getSubTotal).sum();
}

    // Standard Accessors 
public String getOrderID() { return orderID; }
public LocalDate getOrderDate() { return orderDate; }
public OrderStatus getStatus() { return status; }
public void setStatus(OrderStatus status) { this.status = status; }
public LocalDate getDeliveryDate() { return deliveryDate; }
public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
public List<OrderItem> getItems() { return items; }
public void setItems(List<OrderItem> items) { this.items = items; }
public String getCustomerID() { return customerID; }
}
