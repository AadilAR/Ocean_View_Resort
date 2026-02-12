package oceanviewresort.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ocean_view_resort"
                    + "?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "PassWord_123";

    // Prevent instantiation
    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly load MySQL JDBC driver (IMPORTANT for Tomcat)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
