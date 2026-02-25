package oceanviewresort.dao;

import oceanviewresort.model.User;
import oceanviewresort.util.DBUtil;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    private static final String FIND_BY_USERNAME_SQL =
            "SELECT * FROM users WHERE username = ?";

    private static final String FIND_BY_EMAIL_SQL =
            "SELECT * FROM users WHERE email = ?";

    private static final String INSERT_USER_SQL =
            "INSERT INTO users (username, email, password, role, is_verified, verification_token) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PASSWORD_SQL =
            "UPDATE users SET password = ?, verification_token = NULL WHERE user_id = ?";

    private static final String VERIFY_USER_SQL =
            "UPDATE users SET is_verified = TRUE, verification_token = NULL WHERE user_id = ?";

    private static final String FIND_BY_TOKEN_SQL =
            "SELECT * FROM users WHERE verification_token = ?";

    private static final String SAVE_RESET_TOKEN_SQL =
            "UPDATE users SET verification_token = ? WHERE user_id = ?";

    // ==========================================
    // Find By Username
    // ==========================================
    @Override
    public User findByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBUtil.getInstance().getConnection();
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
    // Find By Email
    // ==========================================
    @Override
    public User findByEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EMAIL_SQL)) {

            stmt.setString(1, email.trim());

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

        if (user == null) {
            return false;
        }

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER_SQL)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setBoolean(5, user.isVerified());
            stmt.setString(6, user.getVerificationToken());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Username or email already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    // ==========================================
    // Save Reset Token
    // ==========================================
    @Override
    public boolean saveResetToken(int userId, String token) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_RESET_TOKEN_SQL)) {

            stmt.setString(1, token);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Update Password
    // ==========================================
    @Override
    public void updatePassword(int userId, String newPassword) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD_SQL)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // Verify User
    // ==========================================
    @Override
    public boolean verifyUser(int userId) {

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(VERIFY_USER_SQL)) {

            stmt.setInt(1, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Find By Verification Token
    // ==========================================
    @Override
    public User findByVerificationToken(String token) {

        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_TOKEN_SQL)) {

            stmt.setString(1, token.trim());

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
    // Utility Method
    // ==========================================
    private User extractUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setVerificationToken(rs.getString("verification_token"));

        return user;
    }
}