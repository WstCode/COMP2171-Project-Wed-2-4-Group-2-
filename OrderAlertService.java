import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderAlertService {

    private List<OrderAlert> alertList;
    private int thresholdTime; // in hours

    // constructor
    public OrderAlertService(int thresholdTime) {
        this.alertList = new ArrayList<>();
        this.thresholdTime = thresholdTime;
    }

    // checkUpcomingOrders(orders : List<Order>) : List<OrderAlert>
    public List<OrderAlert> checkUpcomingOrders(List<Order> orders) {
        List<OrderAlert> newAlerts = new ArrayList<>();

        for (Order order : orders) {
            if (order != null && order.isDeadlineNear(thresholdTime)) {

                if (!alertExists(order.getOrderID())) {
                    OrderAlert alert = createAlert(order);
                    alertList.add(alert);
                    newAlerts.add(alert);
                }
            }
        }
        return newAlerts;
    }

    // createAlert(order : Order) : OrderAlert
    public OrderAlert createAlert(Order order) {
        String alertID = "ALERT-" + System.currentTimeMillis();

        return new OrderAlert(
                alertID,
                order.getOrderID(),
                LocalDateTime.now(),
                "Order for " + order.getCustomerName() + " is due soon!"
        );
    }

    // getActiveAlerts() : List<OrderAlert>
    public List<OrderAlert> getActiveAlerts() {
        List<OrderAlert> activeAlerts = new ArrayList<>();

        for (OrderAlert alert : alertList) {
            if (alert.isActive()) {
                activeAlerts.add(alert);
            }
        }
        return activeAlerts;
    }

    // monitorDeadlines() : void
    public void monitorDeadlines() {
        // In a real system, this would run continuously or on a timer
        System.out.println("Monitoring deadlines...");
    }

    
    private boolean alertExists(String orderID) {
        for (OrderAlert alert : alertList) {
            if (alert.getOrderID().equals(orderID) && alert.isActive()) {
                return true;
            }
        }
        return false;
    }
}
