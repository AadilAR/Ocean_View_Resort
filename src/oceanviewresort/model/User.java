package oceanviewresort.model;

public class User {

    private int userId;
    private String username;
    private String email;
    private String password;
    private String role;

    // Email verification
    private boolean verified;
    private String verificationToken;

    // =============================
    // Constructors
    // =============================

    public User() {
    }

    public User(int userId,
                String username,
                String email,
                String password,
                String role,
                boolean verified,
                String verificationToken) {

        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.verified = verified;
        this.verificationToken = verificationToken;
    }

    // =============================
    // Getters & Setters
    // =============================

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // ⚠️ Ideally hash before storing
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}