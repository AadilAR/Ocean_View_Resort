package oceanviewresort.service;

import oceanviewresort.dao.UserDAO;
import oceanviewresort.dao.UserDAOImpl;
import oceanviewresort.model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    public User authenticate(String username, String password) {

        User user = userDAO.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    public boolean register(String username, String password, String role) {

        if (userDAO.findByUsername(username) != null) {
            return false; // Username exists
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        userDAO.addUser(user);
        return true;
    }
}
