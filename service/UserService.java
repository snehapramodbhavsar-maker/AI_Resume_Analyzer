package service;

import dao.UserDAO;
import model.User;

/**
 * UserService.java
 * -------------------------------------------------
 * Service layer for user-related operations.
 * Acts as middleware between UI and DAO.
 * -------------------------------------------------
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user after validating inputs.
     * @return message indicating result
     */
    public String register(String username, String password, String email) {
        if (username == null || username.trim().isEmpty())
            return "❌ Username cannot be empty.";
        if (password == null || password.length() < 4)
            return "❌ Password must be at least 4 characters.";
        if (userDAO.usernameExists(username.trim()))
            return "❌ Username '" + username + "' already exists. Choose another.";

        User user = new User(username.trim(), password, email.trim());
        boolean success = userDAO.registerUser(user);
        return success ? "✅ Registration successful! You can now login."
                       : "❌ Registration failed. Please try again.";
    }

    /**
     * Attempt login and return the User if successful.
     */
    public User login(String username, String password) {
        if (username == null || password == null) return null;
        return userDAO.login(username.trim(), password);
    }
}
