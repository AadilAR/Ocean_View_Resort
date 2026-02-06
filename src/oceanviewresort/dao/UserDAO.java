package oceanviewresort.dao;

import oceanviewresort.model.User;

public interface UserDAO {

    User findByUsername(String username);

    void addUser(User user);
}