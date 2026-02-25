package oceanviewresort.service;

import oceanviewresort.dao.UserDAO;
import oceanviewresort.dao.UserDAOImpl;
import oceanviewresort.model.User;
import oceanviewresort.util.EmailUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    // ====================================
    // LOGIN
    // ====================================
    public User authenticate(String username, String password) {

        if (isBlank(username) || isBlank(password)) {
            return null;
        }

        User user = userDAO.findByUsername(username.trim());

        if (user == null) {
            return null;
        }

        // Block login if not verified
        if (!user.isVerified()) {
            System.out.println("User not verified.");
            return null;
        }

        // ✅ Secure password check using BCrypt
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    // ====================================
    // REGISTER (WITH EMAIL VERIFICATION)
    // ====================================
    public boolean register(String username,
                            String email,
                            String password,
                            String role) {

        if (isBlank(username) || isBlank(email)
                || isBlank(password) || isBlank(role)) {
            return false;
        }

        // Check duplicate username
        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        // Check duplicate email
        if (userDAO.findByEmail(email) != null) {
            return false;
        }

        // Generate verification token
        String token = UUID.randomUUID().toString();

        // ✅ Hash password before saving
        String hashedPassword = BCrypt.hashpw(password.trim(), BCrypt.gensalt());

        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPassword(hashedPassword);  // 🔐 store hash, not plain text
        user.setRole(role.trim().toUpperCase());
        user.setVerified(false);
        user.setVerificationToken(token);

        boolean saved = userDAO.addUser(user);

        if (saved) {
            System.out.println("User saved. Sending verification email...");
            EmailUtil.sendVerificationEmail(email, token);
        }

        return saved;
    }

    // ====================================
    // VERIFY ACCOUNT
    // ====================================
    public boolean verifyAccount(String token) {

        if (isBlank(token)) {
            return false;
        }

        User user = userDAO.findByVerificationToken(token);

        if (user == null) {
            return false;
        }

        return userDAO.verifyUser(user.getUserId());
    }

    // ====================================
    // Utility
    // ====================================
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}