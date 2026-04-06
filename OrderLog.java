import java.time.LocalDateTime;


public class OrderLog {
    private String logID;
    private String orderID;
    private String staffID;
    private LocalDateTime timestamp;
    private String action;

    public OrderLog(String logID, String orderID, String staffID, String action) {
        this.logID = logID;
        this.orderID = orderID;
        this.staffID = staffID;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }


    public String getLogDetails() {
        return String.format("Log [%s] | Time: %s | Staff: %s | Order: %s | Action: %s",
                             logID, timestamp, staffID, orderID, action);
    }

    // Getters and Setters
    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
