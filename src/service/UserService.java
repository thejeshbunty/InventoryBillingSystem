// service/UserService.java
package service;

import dao.UserDAO;
import model.User;
import java.util.Optional;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    // ✅ Added DEBUG print to verify what DB is returning
    public Optional<User> getUserByUsername(String username) {
        Optional<User> userOpt = userDAO.findByUsername(username);

        // 🔎 Debug line to see the actual hash fetched from DB
        userOpt.ifPresent(u ->
            System.out.println("[DEBUG] DB returned user: " + u.getUsername()
                    + " | Hash: " + u.getPassword())
        );

        return userOpt;
    }

    // Update username
    public boolean updateUsername(User user, String newUsername) {
        try {
            userDAO.updateUsername(user.getUserId(), newUsername);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update password
    public boolean updatePassword(User user, String newPassword) {
        try {
            userDAO.updatePassword(user.getUserId(), newPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
