import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "staffID VARCHAR(50) PRIMARY KEY," +
                "password VARCHAR(100) NOT NULL," +
                "loggedIn BOOLEAN NOT NULL DEFAULT FALSE" +
                ")"
            );

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS orders (" + 
                ")"
            );

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS payments (" + 
                ")"
            );

            System.out.println("Database ready.");

        } catch (Exception e) {
            System.out.println("Database initialization failed.");
            e.printStackTrace();
        }
    }
}
