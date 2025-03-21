import java.io.*;
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

    // File Management of Users
    public void saveUsersToCSV(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User  user : users) {
                writer.write(user.getUsername() + "," + user.getEmail() + "," + user.getPhoneNumber());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    public void loadUsersFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    User user = new User(data[0].trim(), data[1].trim(), data[2].trim());
                    addUser (user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // This finds a user by their username
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // This finds a user by their email
    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
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
        UserInterface.printSubHeader("----- Registered Users -----");
        for (User user : users) {
            UserInterface.printInfo("Username: " + user.getUsername() + ", Email: " + user.getEmail());
        }
        UserInterface.printSubHeader("----------------------------");
    }
}
