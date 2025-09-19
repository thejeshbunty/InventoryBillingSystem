package service;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import model.User;

public class AuthService {
    private UserService userService = new UserService();

    // Authenticate user: return Optional<User> if username & password match
    public Optional<User> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()));
    }

    // Verify password of a user
    public boolean verifyPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    // Change username (basic check for alphanumeric + underscore)
    public boolean changeUsername(User user, String newUsername) {
        if (newUsername == null || newUsername.isEmpty()) return false;
        // The regex check is good, ensures username is clean and consistent.
        if (!newUsername.matches("[A-Za-z0-9_]+")) return false;
        return userService.updateUsername(user, newUsername);
    }

    // Change password (store hash in DB)
    public boolean changePassword(User user, String newPassword) {
        // Enforce a stronger password policy: min length 8, at least one digit and one special character.
        // This regex ensures better security.
        if (newPassword == null || newPassword.length() < 8 ||
            !newPassword.matches(".*\\d.*") || !newPassword.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        }

        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return userService.updatePassword(user, hashed);
    }
}