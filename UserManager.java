import java.util.*;

// This class manages all users registered in the application
class UserManager {
    // A list that holds all registered users
    private List<User> users;

    // This is a constructor - it creates a new empty user manager
    public UserManager() {
        this.users = new ArrayList<>();
    }

    // This adds a new user to the system
    public void addUser(User user) {
        users.add(user);
    }

    // This finds a user by their username
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    // This finds a user by their email
    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null; // User not found
    }

    // This removes a user from the system
    public boolean removeUser(User user) {
        return users.remove(user);
    }

    // This counts how many users are registered
    public int getUserCount() {
        return users.size();
    }

    // This displays all registered users
    public void displayAllUsers() {
        System.out.println("\n----- Registered Users -----");
        for (User user : users) {
            System.out.println("Username: " + user.getUsername() + ", Email: " + user.getEmail());
        }
        System.out.println("----------------------------");
    }
}
