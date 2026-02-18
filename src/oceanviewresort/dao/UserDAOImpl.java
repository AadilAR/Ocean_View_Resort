package oceanviewresort.dao;

import oceanviewresort.model.User;
import oceanviewresort.util.DBUtil;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    private static final String FIND_BY_USERNAME_SQL =
            "SELECT * FROM user WHERE username = ?";

    private static final String INSERT_USER_SQL =
            "INSERT INTO user (username, password, role, security_question, security_answer) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_PASSWORD_SQL =
            "UPDATE user SET password = ? WHERE user_id = ?";


    // ==========================================
    // Find By Username
    // ==========================================
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
                    return extractUser(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==========================================
    // Add User
    // ==========================================
    @Override
    public boolean addUser(User user) {

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER_SQL)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getSecurityQuestion());
            stmt.setString(5, user.getSecurityAnswer());

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

    // ==========================================
    // Update Password
    // ==========================================
    @Override
    public void updatePassword(int userId, String newPassword) {

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD_SQL)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // Utility Method (Clean Code)
    // ==========================================
    private User extractUser(ResultSet rs) throws SQLException {

        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setSecurityQuestion(rs.getString("security_question"));
        user.setSecurityAnswer(rs.getString("security_answer"));

        return user;
    }
}
