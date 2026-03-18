
public class BakeryStaff {

    // attributes
    private String staffID;
    private String password;
    private boolean loggedIn;

    // constructor
    public BakeryStaff(String staffID, String password) {
        this.staffID = staffID;
        this.password = password;
        this.loggedIn = false;
    }

    // login
    public boolean login(String enteredID, String enteredPassword) {
    if (this.staffID.equals(enteredID) && this.password.equals(enteredPassword)) {
        loggedIn = true;
        System.out.println("Login successful. Welcome, " + staffID + "!");
        return true;
    } else {
        System.out.println("Login failed. Invalid ID or password.");
        return false;
    }
}

    // logout
    public void logout() {
        if (loggedIn) {
            loggedIn = false;
            System.out.println("Logout successful. Goodbye, " + staffID + "!");
        } else {
            System.out.println("You are not logged in.");
        }
    }

    public String getStaffID() {
        return staffID;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setUsername(String staffId) {
        this.staffID = staffID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "BakeryStaff{" +
                "staffID='" + staffID + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }
}