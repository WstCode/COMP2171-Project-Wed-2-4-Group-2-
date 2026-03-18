import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private List<Customer> customers;

    public CustomerRepository() {
        this.customers = new ArrayList<>();
    }

    public Customer findCustomerById(String customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    public boolean customerExists(String customerID) {
        return findCustomerById(customerID) != null;
    }

    public void saveCustomer(Customer customer) {
        if (customer != null) {
            customers.add(customer);
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}