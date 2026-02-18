package oceanviewresort.dao;

import oceanviewresort.model.User;

public interface UserDAO {

    /**
     * Find a user by username.
     * @param username the username
     * @return User object if found, otherwise null
     */
    User findByUsername(String username);

    /**
     * Save a new user to the database.
     * @param user the user entity
     * @return true if saved successfully, false otherwise
     */
    boolean addUser(User user);

    void updatePassword(int userId, String newPassword);
}
