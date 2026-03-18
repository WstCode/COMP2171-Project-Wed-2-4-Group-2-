
public class CustomerManager {
    private CustomerRepository customerRepository;

    public CustomerManager(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String customerID, String name, String email,
                                   String phoneNumber, String address) {
        Customer customer = new Customer(customerID, name, email, phoneNumber, address);

        if (!customer.validateContactDetails()) {
            return null;
        }

        if (customerRepository.customerExists(customerID)) {
            return null;
        }

        customerRepository.saveCustomer(customer);
        return customer;
    }
}