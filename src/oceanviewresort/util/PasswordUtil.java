package oceanviewresort.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    // ==========================================
    // Hash Password (SHA-256)
    // ==========================================
    public static String hashPassword(String password) {

        if (password == null) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // ==========================================
    // Verify Password
    // ==========================================
    public static boolean matches(String rawPassword, String storedHash) {

        if (rawPassword == null || storedHash == null) {
            return false;
        }

        String hashedInput = hashPassword(rawPassword);
        return hashedInput.equals(storedHash);
    }
}