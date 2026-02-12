package oceanviewresort.service;

import oceanviewresort.dao.UserDAO;
import oceanviewresort.dao.UserDAOImpl;
import oceanviewresort.model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    // ---------------------------------
    // Authenticate (Login)
    // ---------------------------------
    public User authenticate(String username, String password) {

        if (isBlank(username) || isBlank(password)) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user != null && password.equals(user.getPassword())) {
            return user;
        }

        return null;
    }

    // ---------------------------------
    // Register (Signup)
    // ---------------------------------
    public boolean register(String username, String password, String role) {

        if (isBlank(username) || isBlank(password) || isBlank(role)) {
            return false;
        }

        // Check if user already exists
        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setRole(role.trim().toUpperCase());

        userDAO.addUser(user);

        return true;
    }

    // ---------------------------------
    // Forgot Password
    // ---------------------------------
    public String getPasswordByUsername(String username) {

        if (isBlank(username)) {
            return null;
        }

        User user = userDAO.findByUsername(username.trim());

        if (user != null) {
            return user.getPassword();
        }

        return null;
    }

    // ---------------------------------
    // Utility Method
    // ---------------------------------
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}