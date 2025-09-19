
// service/UserService.java
package service;

import dao.UserDAO;
import java.util.Optional;
import model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public Optional<User> getUserByUsername(String username) {
        Optional<User> userOpt = userDAO.findByUsername(username);

        
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
