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
}
