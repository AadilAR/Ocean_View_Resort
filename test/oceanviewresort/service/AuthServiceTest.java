package oceanviewresort.service;

import oceanviewresort.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    /**
     * Validates that blank input does not authenticate.
     */
    @Test
    void testAuthenticate_BlankInput() {
        AuthService authService = new AuthService();

        User user = authService.authenticate("", "");

        assertNull(user);
    }

    /**
     * Validates authentication fails for wrong password.
     */
    @Test
    void testAuthenticate_InvalidPassword() {
        AuthService authService = new AuthService();

        User user = authService.authenticate("admin", "wrongPassword");

        assertNull(user);
    }

    /**
     * Validates successful login for verified user.
     */
    @Test
    void testAuthenticate_ValidUser() {
        AuthService authService = new AuthService();

        User user = authService.authenticate("admin", "admin123");

        assertNotNull(user);
    }
}