package oceanviewresort.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ocean_view_resort" +
                    "?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "PassWord_123";

    // ✅ Eager Singleton Instance (Thread-safe)
    private static final DBUtil INSTANCE = new DBUtil();

    // Private constructor prevents external instantiation
    private DBUtil() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    // Global access point
    public static DBUtil getInstance() {
        return INSTANCE;
    }

    // Get database connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}