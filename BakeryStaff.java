public class BakeryStaff {
    //attributes
    private String staffID;
    private String username;
    private String password;
    private boolean loggedIn;


    //constructor
    public BakeryStaff(String staffID, String username, String password){
        this.staffID = staffID;
        this.username = username;
        this.password = password;
        this.loggedIn = false;
    }

    // Methods
    public boolean login(String enteredUsername, String enteredPassword) {
        if (this.username.equals(enteredUsername) && this.password.equals(enteredPassword)) {
            this.loggedIn = true;
            System.out.println("Login successful. Welcome, " + username + "!");
            return true;
        } else {
            System.out.println("Login failed. Invalid username or password.");
            return false;
        }
    }

    public void logout() {
        if (loggedIn) {
            loggedIn = false;
            System.out.println("Logout successful. Goodbye, " + username + "!");
        } else {
            System.out.println("You are not logged in.");
        }
    }

    public Order createOrder(OrderManager manager, Customer customer, String details) {
        if (!loggedIn) {
            System.out.println("Access denied. Please log in first.");
            return null;
        }
        return manager.createOrder(customer, details);
    }

    public Customer createCustomer(OrderManager manager, String name,
                                   String contact, String address) {
        if (!loggedIn) {
            System.out.println("Access denied. Please log in first.");
            return null;
        }
        return manager.createCustomer(name, contact, address);
    }

    public String getStaffID() {
        return staffID;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "BakeryStaff{" +
                "staffID='"  + staffID  + '\'' +
                ", username='" + username + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }

}