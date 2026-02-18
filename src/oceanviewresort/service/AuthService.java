package oceanviewresort.service;

import oceanviewresort.dao.UserDAO;
import oceanviewresort.dao.UserDAOImpl;
import oceanviewresort.model.User;

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

        if (user != null && password.equals(user.getPassword())) {
            return user;
        }

        return null;
    }

    // ====================================
    // REGISTER
    // ====================================
    public boolean register(String username,
                            String password, String role,
                            String securityQuestion,
                            String securityAnswer) {

        if (isBlank(username) || isBlank(password)
                || isBlank(role)
                || isBlank(securityQuestion) || isBlank(securityAnswer)) {
            return false;
        }

        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setRole(role.trim().toUpperCase());
        user.setSecurityQuestion(securityQuestion.trim());
        user.setSecurityAnswer(securityAnswer.trim());

        return userDAO.addUser(user);
    }

    // ====================================
    // UTILITY
    // ====================================
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
