import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "staffID INT AUTO_INCREMENT PRIMARY KEY," +
                "password VARCHAR(100)," +
                "email VARCHAR(100)" +
                ")"
            );

            System.out.println("Database ready.");

        } catch (Exception e) {
            System.out.println("Database initialization failed.");
            e.printStackTrace();
        }
    }
}
