package oceanviewresort.dao;

import oceanviewresort.model.User;
import oceanviewresort.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    private static final String FIND_BY_USERNAME_SQL =
            "SELECT user_id, username, password, role FROM user WHERE username = ?";

    private static final String INSERT_USER_SQL =
            "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";

    @Override
    public User findByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USERNAME_SQL)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean addUser(User user) {

        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {

            if (e.getMessage().contains("Duplicate")) {
                System.out.println("Username already exists!");
            } else {
                e.printStackTrace();
            }

            return false;
        }
    }

}
