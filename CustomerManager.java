public class CustomerManager {
    private CustomerRepository customerRepository;

    public CustomerManager(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String customerID, String name, String email, String phoneNumber, String address) {
        if (customerID == null || customerID.isBlank()
                || name == null || name.isBlank()
                || email == null || email.isBlank()
                || phoneNumber == null || phoneNumber.isBlank()
                || address == null || address.isBlank()) {
            return null;
        }

        if (customerRepository.customerExists(customerID)) {
            System.out.println("Customer ID already exists.");
            return null;
        }

        Customer customer = new Customer(customerID, name, email, phoneNumber, address);

        if (!customer.validateContactDetails()) {
            return null;
        }

        customerRepository.saveCustomer(customer);
        return customer;
    }

    public Customer findCustomerById(String customerID) {
        return customerRepository.findCustomerById(customerID);
}
}