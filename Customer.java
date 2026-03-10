import java.time.LocalDate;
import java.util.List;

public class Customer {
    private String customerID;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    public Customer(String customerID, String name, String email, String phoneNumber, String address) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getCustomerID() { 
        return customerID; 
    }
    public String getName() { 
        return name; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public String getAddress() { 
        return address; 
    }

    public boolean validateContactDetails() {
        return validateEmail(email) && validatePhone(phoneNumber) && name != null && !name.isBlank();
    }

    private boolean validateEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean validatePhone(String phone) {
        if (phone == null) return false;
        String phoneRegex = "^[0-9]{7,15}$"; 
        return phone.matches(phoneRegex);
    }

    public String getCustomerInfo() {
        return "Customer ID: " + customerID + "\n"
             + "Name: " + name + "\n"
             + "Email: " + email + "\n"
             + "Phone: " + phoneNumber + "\n"
             + "Address: " + address + "\n";
    }

    public Order placeOrder(OrderManager manager, List<OrderItem> items, LocalDate deliveryDate) {
        if (!validateContactDetails()) {
            System.out.println("Invalid contact details. Cannot place order.");
            return null;
        }
        return manager.createOrder(this, items, deliveryDate);
    }
}