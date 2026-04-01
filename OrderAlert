import java.time.LocalDateTime;

public class OrderAlert {
    //attributes
    private String alertID;
    private String orderID;
    private LocalDateTime alertTime;
    private String message;
    private boolean isActive;

    //constructor
    public OrderAlert(String alertID, String orderID, LocalDateTime alertTime, String message) {
        this.alertID = alertID;
        this.orderID = orderID;
        this.alertTime = alertTime;
        this.message = message;
        this.isActive = true; // default to active when created
    }
    // generate message (optional if message is passed in constructor)
    public String generateMessage() {
        return "Alert: Order " + orderID + " is approaching its deadline.";
    }

    // activate alert
    public void activateAlert() {
        this.isActive = true;
    }

    // deactivate alert
    public void deactivateAlert() {
        this.isActive = false;
    }

    // check if alert time has been reached
    public boolean isDue() {
        return LocalDateTime.now().isAfter(alertTime) ||
               LocalDateTime.now().isEqual(alertTime);
    }

    // getters
    public String getAlertID() {
        return alertID;
    }

    public String getOrderID() {
        return orderID;
    }

    public LocalDateTime getAlertTime() {
        return alertTime;
    }

    public String getMessage() {
        return message;
    }

    public boolean isActive() {
        return isActive;
    }

    // setters (only where needed)
    public void setAlertTime(LocalDateTime alertTime) {
        this.alertTime = alertTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // toString (for display/debugging)
    @Override
    public String toString() {
        return "OrderAlert{" +
                "alertID='" + alertID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", alertTime=" + alertTime +
                ", message='" + message + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
    

