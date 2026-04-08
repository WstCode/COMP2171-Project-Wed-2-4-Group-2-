import java.sql.Connection;

public class DatabaseService {

    public static Connection initializeDatabase() {
        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            System.out.println("Warning: Could not connect to database. Running in file mode.");
            return null;
        }

        System.out.println("Connected to database.");

        DatabaseInitializer.initialize(conn);

        return conn;
    }
}
