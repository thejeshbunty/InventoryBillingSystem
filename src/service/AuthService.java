package service;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import model.User;

public class AuthService {
    private UserService userService = new UserService();

    // Authenticate user: return Optional<User> if username & password match
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    // Verify password of a user
    public boolean verifyPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    // Change username (basic check for alphanumeric + underscore)
    public boolean changeUsername(User user, String newUsername) {
        if (newUsername == null || newUsername.isEmpty()) return false;
        if (!newUsername.matches("[A-Za-z0-9_]+")) return false;
        return userService.updateUsername(user, newUsername);
    }

    // Change password (store hash in DB)
    public boolean changePassword(User user, String newPassword) {
        if (newPassword == null || newPassword.length() < 4) return false;
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return userService.updatePassword(user, hashed);
    }
}
