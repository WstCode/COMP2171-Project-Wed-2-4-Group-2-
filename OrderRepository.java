import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final Connection conn;
    private final OrderFileBackup backup;

    public OrderRepository(Connection conn) {
        this.conn = conn;
        this.backup = new OrderFileBackup("orders.txt");
    }

    public void saveOrder(Order order) {
        String orderSQL = """
            INSERT INTO orders 
            (orderID, orderDate, status, deliveryDate, customerID, pickupTime, completedDateTime, paymentID, customerName)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        String itemSQL = """
            INSERT INTO order_items (orderID, itemName, quantity, unitPrice)
            VALUES (?, ?, ?, ?)
        """;

        try {
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Connection unavailable");
            }

            conn.setAutoCommit(false);

            try (
                PreparedStatement orderStmt = conn.prepareStatement(orderSQL);
                PreparedStatement itemStmt = conn.prepareStatement(itemSQL)
            ) {
                
                orderStmt.setString(1, order.getOrderID());
                orderStmt.setDate(2, Date.valueOf(order.getOrderDate()));
                orderStmt.setString(3, order.getStatus().name());
                orderStmt.setDate(4, Date.valueOf(order.getDeliveryDate()));
                orderStmt.setString(5, order.getCustomerID());

                orderStmt.setTimestamp(6, null); // pickupTime
                orderStmt.setTimestamp(7, null); // completedDateTime
                orderStmt.setString(8, null);    // paymentID

                orderStmt.setString(9, order.getCustomerName());

                orderStmt.executeUpdate();

                for (OrderItem item : order.getItems()) {
                    itemStmt.setString(1, order.getOrderID());
                    itemStmt.setString(2, item.getItemName());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getUnitPrice());
                    itemStmt.executeUpdate();
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            System.out.println("⚠️ DB failed → saving to file backup");
            backup.saveOrder(order);
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        String orderSQL = "SELECT * FROM orders";
        String itemSQL = "SELECT * FROM order_items WHERE orderID = ?";

        try (
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(orderSQL)
        ) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getString("orderID"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getDate("deliveryDate").toLocalDate(),
                        rs.getString("customerID"),
                        rs.getString("customerName")
                );

                order.setStatus(
                        Order.OrderStatus.valueOf(rs.getString("status"))
                );

                // 🔹 Load items
                try (PreparedStatement itemStmt = conn.prepareStatement(itemSQL)) {
                    itemStmt.setString(1, order.getOrderID());
                    ResultSet itemRS = itemStmt.executeQuery();

                    while (itemRS.next()) {
                        order.addItem(new OrderItem(
                                itemRS.getString("itemName"),
                                itemRS.getInt("quantity"),
                                itemRS.getDouble("unitPrice")
                        ));
                    }
                }

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public Order findOrderById(String orderID) {
        String orderSQL = "SELECT * FROM orders WHERE orderID = ?";
        String itemSQL = "SELECT * FROM order_items WHERE orderID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(orderSQL)) {
            stmt.setString(1, orderID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Order order = new Order(
                        orderID,
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getDate("deliveryDate").toLocalDate(),
                        rs.getString("customerID"),
                        rs.getString("customerName")
                );

                order.setStatus(
                        Order.OrderStatus.valueOf(rs.getString("status"))
                );

                try (PreparedStatement itemStmt = conn.prepareStatement(itemSQL)) {
                    itemStmt.setString(1, orderID);
                    ResultSet itemRS = itemStmt.executeQuery();

                    while (itemRS.next()) {
                        order.addItem(new OrderItem(
                                itemRS.getString("itemName"),
                                itemRS.getInt("quantity"),
                                itemRS.getDouble("unitPrice")
                        ));
                    }
                }

                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteOrder(String orderID) {
        String sql = "DELETE FROM orders WHERE orderID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
