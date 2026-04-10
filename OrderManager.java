import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final List<Order> activeOrders;
    private final OrderRepository repository;
    private final OrderArchive archivedOrderRepository;
    private final PaymentRepository paymentRepository;

    public OrderManager(OrderRepository repository,
                        OrderArchive archivedOrderRepository,
                        PaymentRepository paymentRepository) {
        this.repository = repository;
        this.archivedOrderRepository = archivedOrderRepository;
        this.paymentRepository = paymentRepository;
        this.activeOrders = new ArrayList<>(repository.getAllOrders());
    }

    public Order createOrder(String customerID, String customerName, List<OrderItem> details, LocalDate deliveryDate) {
        if (customerID == null || customerID.isBlank()
                || customerName == null || customerName.isBlank()
                || details == null || details.isEmpty()
                || deliveryDate == null) {
            return null;
        }

        String orderID = "ORD" + (repository.getAllOrders().size()
                + archivedOrderRepository.getAllArchivedOrders().size() + 1);

        Order order = new Order(orderID, LocalDate.now(), deliveryDate, customerID, customerName);

        for (OrderItem item : details) {
            order.addItem(item);
        }

        if (!order.validateOrderData()) {
            return null;
        }

        repository.saveOrder(order);
        activeOrders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return repository.getAllOrders();
    }

    public List<Order> getActiveOrders() {
        return repository.getAllOrders();
    }

    public Order findOrderById(String orderID) {
        return repository.findOrderById(orderID);
    }

    public boolean editOrder(String orderID, String newCustomerName, LocalDate newDeliveryDate, List<OrderItem> newItems) {
        Order order = repository.findOrderById(orderID);

        if (order == null) {
            return false;
        }

        if (newCustomerName != null && !newCustomerName.isBlank()) {
            order.setCustomerName(newCustomerName);
        }

        if (newDeliveryDate != null) {
            order.setDeliveryDate(newDeliveryDate);
        }

        if (newItems != null && !newItems.isEmpty()) {
            order.setItems(newItems);
        }

        if (!order.validateOrderData()) {
            return false;
        }

        return repository.updateOrder(order);
    }

    public boolean deleteOrder(String orderID) {
        Order order = repository.findOrderById(orderID);

        if (order == null) {
            return false;
        }

        activeOrders.remove(order);
        return repository.deleteOrder(orderID);
    }

    public List<Order> getOrdersWithApproachingDeadlines(int daysThreshold) {
        List<Order> upcomingOrders = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Order order : repository.getAllOrders()) {
            LocalDate deliveryDate = order.getDeliveryDate();

            if (deliveryDate != null
                    && !deliveryDate.isBefore(today)
                    && !deliveryDate.isAfter(today.plusDays(daysThreshold))) {
                upcomingOrders.add(order);
            }
        }

        return upcomingOrders;
    }

    public boolean completeOrder(String orderID, String staffID) {
        Order order = repository.findOrderById(orderID);

        if (order == null) {
            return false;
        }

        order.setStatus(Order.OrderStatus.COMPLETED);

        boolean archived = archivedOrderRepository.saveArchivedOrder(order, staffID);
        if (!archived) {
            return false;
        }

        boolean deletedFromActive = repository.deleteOrder(orderID);
        if (!deletedFromActive) {
            return false;
        }

        activeOrders.remove(order);
        return true;
    }

    public List<Order> getArchivedOrders() {
        return archivedOrderRepository.getAllArchivedOrders();
    }

    public Payment getPayment(String orderID) {
        Order order = repository.findOrderById(orderID);

        if (order == null) {
            return null;
        }

        return paymentRepository.findPaymentByOrderID(orderID);
    }

    public boolean updatePayment(String orderID, PaymentInfo status, PaymentInfo method) {
        Order order = repository.findOrderById(orderID);

        if (order == null || status == null || method == null) {
            return false;
        }

        Payment payment = paymentRepository.findPaymentByOrderID(orderID);

        if (payment == null) {
            String paymentID = "PAY" + (paymentRepository.getAllPayments().size() + 1);
            payment = new Payment(paymentID, orderID, status, method);
            paymentRepository.savePayment(payment);
            return true;
        }

        payment.updateStatus(status);
        payment.updateMethod(method);
        return paymentRepository.updatePayment(payment);
    }

    public boolean validatePayment(PaymentInfo status, PaymentInfo method) {
        return status != null && method != null;
    }
}
