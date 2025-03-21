import java.util.Scanner;
import java.util.*;

public class UserMenuManager {
    private JetInventory jetInventory;
    private UserManager userManager;
    private BookingManager bookingManager;
    private Scanner scanner;

    // Constructor to initialize UserMenuManager with dependencies
    public UserMenuManager(JetInventory jetInventory, UserManager userManager, BookingManager bookingManager) {
        this.jetInventory = jetInventory;
        this.userManager = userManager;
        this.bookingManager = bookingManager;
        this.scanner = new Scanner(System.in);
    }

    // Displays the user login menu and handles login logic
    public void userLoginMenu() {
        UserInterface.printHeader("User Login");
        String username;
        do {
            System.out.print("\nEnter username: ");
            username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                UserInterface.printError("Username cannot be empty. Please try again.");
            }
        } while (username.isEmpty());

        // Special admin access
        if (username.equals("A@dMin")) {
            UserInterface.printPrompt("Enter admin password: ");
            String password = scanner.nextLine().trim();

            // Simple password check (in a real system, use secure authentication)
            if (password.equals("admin123")) {
                UserInterface.printSuccess("Admin login successful!");
                AdminMenuManager adminMenu = new AdminMenuManager(jetInventory, userManager, bookingManager);
                adminMenu.displayAdminMenu();
                return;
            } else {
                UserInterface.printError("Incorrect admin credentials.");
                return;
            }
        }

        User user = userManager.findUserByUsername(username);

        if (user != null) {
            UserInterface.printSuccess("Login successful!");
            userDashboard(user);
        } else {
            if (InputValidator.getYesNoInput("User not found. Would you like to register?")) {
                registerUserMenu();
            }
        }
    }

    // Displays the user registration menu and handles user registration
    public void registerUserMenu() {
        UserInterface.printHeader("User Registration");

        // Get and validate username
        String username = InputValidator.getStringInput("Enter username (or 'b' to go back): ", false);
        if (username.equalsIgnoreCase("b")) {
            UserInterface.printInfo("Returning to the main menu...");
            return;
        }

        if (username.length() < 4) {
            UserInterface.printError("Username must be at least 4 characters long. Please try again.");
            registerUserMenu();
            return;
        }

        if (userManager.findUserByUsername(username) != null) {
            UserInterface.printError("Username already taken. Please try another.");
            registerUserMenu();
            return;
        }

        // Get and validate email
        String email = InputValidator.getEmail();
        if (email.equalsIgnoreCase("b")) {
            UserInterface.printInfo("Returning to the main menu...");
            return;
        }

        if (userManager.findUserByEmail(email) != null) {
            UserInterface.printError("Email already registered. Please try another.");
            registerUserMenu();
            return;
        }

        // Get and validate phone number
        String phoneNumber = InputValidator.getPhoneNumber();
        if (phoneNumber.equalsIgnoreCase("b")) {
            UserInterface.printInfo("Returning to the main menu...");
            return;
        }

        // Create and add the new user
        User newUser = new User(username, email, phoneNumber);
        userManager.addUser(newUser);

        UserInterface.printSuccess("Registration successful! You can now log in.");
    }

    // Displays the user dashboard with options for the logged-in user
    public void userDashboard(User user) {
        boolean userLoggedIn = true;

        while (userLoggedIn) {
            UserInterface.printHeader("===== User Dashboard =====");
            UserInterface.printSubHeader("Welcome, " + user.getUsername() + "!");
            UserInterface.printMenuOption(1, "View My Profile");
            UserInterface.printMenuOption(2, "Book a Jet");
            UserInterface.printMenuOption(3, "View My Bookings");
            UserInterface.printMenuOption(4, "Cancel a Booking");
            UserInterface.printMenuOption(5, "Logout");
            UserInterface.printPrompt("Enter your choice: ");

            int choice = InputValidator.getIntInput();

            switch (choice) {
                case 1:
                    user.displayProfile();
                    break;
                case 2:
                    BookingMenuManager bookingMenu = new BookingMenuManager(jetInventory, userManager, bookingManager);
                    bookingMenu.bookJetMenu(user);
                    break;
                case 3:
                    viewUserBookings(user);
                    break;
                case 4:
                    cancelUserBooking(user);
                    break;
                case 5:
                    userLoggedIn = false;
                    UserInterface.printSuccess("Logged out successfully.");
                    break;
                default:
                    UserInterface.printError("Invalid choice. Please try again.");
            }
        }
    }

    // Displays the user's bookings and allows viewing details
    private void viewUserBookings(User user) {
        List<Booking> userBookings = bookingManager.findBookingsByUser(user);

        if (userBookings.isEmpty()) {
            UserInterface.printError("You have no current bookings.");
            return;
        }

        UserInterface.printSubHeader("\n===== Your Bookings =====");
        for (int i = 0; i < userBookings.size(); i++) {
            Booking booking = userBookings.get(i);
            UserInterface.printInfo((i + 1) + ". From " + booking.getDeparture() +
                    " to " + booking.getDestination() +
                    " on " + booking.getFlightDate() +
                    " (" + booking.getJet().getModel() + ")");
        }
        int bookingIndex = -1;
        boolean validInput = false;

        while (!validInput) {
            UserInterface.printPrompt("Enter booking number to view details (0 to go back): ");
            bookingIndex = InputValidator.getIntInput() - 1;

            if (bookingIndex == -1) {

                return;
            } else if (bookingIndex >= 0 && bookingIndex < userBookings.size()) {

                validInput = true;
                userBookings.get(bookingIndex).displayBookingDetails();
            } else {

                UserInterface.printError("Invalid booking number. Please try again.");
            }
        }
    }
    // Allows the user to cancel a booking
    private void cancelUserBooking(User user) {
        List<Booking> userBookings = bookingManager.findBookingsByUser(user);

        if (userBookings.isEmpty()) {
            UserInterface.printError("You have no current bookings to cancel.");
            return;
        }

        UserInterface.printSubHeader("\n===== Cancel Booking =====");
        for (int i = 0; i < userBookings.size(); i++) {
            Booking booking = userBookings.get(i);
            UserInterface.printInfo((i + 1) + ". From " + booking.getDeparture() +
                    " to " + booking.getDestination() +
                    " on " + booking.getFlightDate() +
                    " (" + booking.getJet().getModel() + ")");
        }

        UserInterface.printPrompt("Enter booking number to cancel (0 to go back): ");
        int bookingIndex = InputValidator.getIntInput() - 1;

        if (bookingIndex >= 0 && bookingIndex < userBookings.size()) {
            Booking bookingToCancel = userBookings.get(bookingIndex);

            boolean confirmCancel = InputValidator.getYesNoInput("Are you sure you want to cancel this booking?");
            if (confirmCancel) {

                boolean cancelled = bookingManager.cancelBooking(bookingToCancel);

                if (cancelled) {
                    UserInterface.printSuccess("Booking successfully cancelled.");
                } else {
                    UserInterface.printError("Failed to cancel booking. Please try again.");
                }
            } else {
                UserInterface.printError("Cancellation aborted.");
            }
        }
    }
}
