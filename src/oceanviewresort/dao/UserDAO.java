package oceanviewresort.dao;

import oceanviewresort.model.User;

public interface UserDAO {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean addUser(User user);

    boolean verifyUser(int userId);

    User findByVerificationToken(String token);

    boolean saveResetToken(int userId, String token);

    void updatePassword(int userId, String newPassword);


}