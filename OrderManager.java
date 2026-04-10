import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final OrderRepository repository;
    private final OrderArchive archivedOrderRepository;

    public OrderManager(OrderRepository repository, OrderArchive archivedOrderRepository, PaymentRepository paymentRepository) {
        this.repository = repository;
        this.archivedOrderRepository = archivedOrderRepository;
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

        return order;
    }

    public boolean saveOrder(Order order) {
        if (order == null) {
            return false;
        }

        try {
            repository.saveOrder(order);
            return true;
        } catch (Exception e) {
            System.out.println("Error saving order: " + e.getMessage());
            return false;
        }
    }

    public List<Order> getAllOrders() {
        return repository.getAllOrders();
    }

    public List<Order> getActiveOrders() {
        List<Order> activeOrders = new ArrayList<>();

        for (Order order : repository.getAllOrders()) {
            if (order.getStatus() == Order.OrderStatus.PENDING) {
                activeOrders.add(order);
            }
        }

        return activeOrders;
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

        return repository.deleteOrder(orderID);
    }

    public List<Order> getOrdersWithApproachingDeadlines(int daysThreshold) {
        List<Order> upcomingOrders = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Order order : getActiveOrders()) {
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

        order.markAsCompleted();

        boolean archived = archivedOrderRepository.saveArchivedOrder(order, staffID);
        if (!archived) {
            return false;
        }

        return repository.deleteOrder(orderID);
    }

    public List<Order> getArchivedOrders() {
        return archivedOrderRepository.getAllArchivedOrders();
    }
}