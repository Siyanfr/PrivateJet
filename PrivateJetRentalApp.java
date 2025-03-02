import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// This is the main class that runs the private jet rental application
public class PrivateJetRentalApp {
    // These are the managers that handle different parts of the system
    private static JetInventory jetInventory;
    private static UserManager userManager;
    private static BookingManager bookingManager;
    private static Scanner scanner;

    // This is the entry point of the program
    public static void main(String[] args) {
        // Initialize the system
        initializeSystem();

        // Add some example data to work with
        loadSampleData();

        // Start the main menu
        displayMainMenu();
    }

    // This sets up the basic system components
    private static void initializeSystem() {
        jetInventory = new JetInventory();
        userManager = new UserManager();
        bookingManager = new BookingManager();
        scanner = new Scanner(System.in);
    }

    // This adds some example jets and users to the system
    private static void loadSampleData() {
        // Add some sample jets
        jetInventory.addJet(new Jet("Gulfstream G650", "Ultra Long Range", 18, true, 12000.0));
        jetInventory.addJet(new Jet("Cessna Citation X", "Super Mid-Size", 8, true, 5500.0));
        jetInventory.addJet(new Jet("Embraer Phenom 300", "Light Jet", 6, true, 3200.0));
        jetInventory.addJet(new Jet("Bombardier Global 7500", "Ultra Long Range", 14, true, 15000.0));
        jetInventory.addJet(new Jet("Dassault Falcon 8X", "Large Jet", 16, true, 9800.0));

        // Add a sample user
        User user1 = new User("john_doe", "john@example.com", "123-456-7890");
        user1.addPastTrip("New York to Los Angeles on 2023-09-10");
        userManager.addUser(user1);

        // Add another sample user
        User user2 = new User("alice_smith", "alice@example.com", "987-654-3210");
        userManager.addUser(user2);

        // Create a sample booking
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = today.format(formatter);
        String futureDate = today.plusDays(7).format(formatter);

        Booking booking1 = bookingManager.createBooking(
                user1,
                jetInventory.getJetByIndex(0),
                "JFK",
                "LAX",
                "One-way",
                currentDate,
                futureDate,
                5
        );
    }

    // This displays the main menu and handles user input
    private static void displayMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== Private Jet Rental Application =====");
            System.out.println("1. Browse Available Jets");
            System.out.println("2. User Login");
            System.out.println("3. Register New User");
            System.out.println("4. View All Bookings (Admin)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    browseJetsMenu();
                    break;
                case 2:
                    userLoginMenu();
                    break;
                case 3:
                    registerUserMenu();
                    break;
                case 4:
                    adminMenu();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using our Private Jet Rental Application!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // This displays the available jets
    private static void browseJetsMenu() {
        System.out.println("\n===== Available Jets =====");
        jetInventory.displayAllJets();

        System.out.println("\nOptions:");
        System.out.println("1. Filter Jets by Type");
        System.out.println("2. Filter Jets by Capacity");
        System.out.println("3. Book a Jet (requires login)");
        System.out.println("4. Return to Main Menu");
        System.out.print("Enter your choice: ");
        System.out.println();

        int choice = getIntInput();

        switch (choice) {
            case 1:
                filterJetsByType();
                break;
            case 2:
                filterJetsByCapacity();
                break;
            case 3:
                System.out.println("Please login first to book a jet.");
                userLoginMenu();
                break;
            case 4:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    // This filters jets by their type
    private static void filterJetsByType() {
        System.out.println("\nAvailable jet types: Ultra Long Range, Large Jet, Super Mid-Size, Light Jet");
        System.out.print("Enter jet type to filter by: ");
        String type = scanner.nextLine();

        List<Jet> filteredJets = jetInventory.findJets(type, 0, true);

        System.out.println("\n----- Filtered Jets -----");
        if (filteredJets.isEmpty()) {
            System.out.println("No jets found matching your criteria.");
        } else {
            for (int i = 0; i < filteredJets.size(); i++) {
                System.out.println((i+1) + ". " + filteredJets.get(i));
            }
        }
    }

    // This filters jets by their passenger capacity
    private static void filterJetsByCapacity() {
        System.out.print("\nEnter minimum seat capacity required: ");
        int minCapacity = getIntInput();

        List<Jet> filteredJets = jetInventory.findJets(null, minCapacity, true);

        System.out.println("\n----- Filtered Jets -----");
        if (filteredJets.isEmpty()) {
            System.out.println("No jets found matching your criteria.");
        } else {
            for (int i = 0; i < filteredJets.size(); i++) {
                System.out.println((i+1) + ". " + filteredJets.get(i));
            }
        }
    }

    // This handles user login
    private static void userLoginMenu() {
        String username;
        do {
            System.out.print("\nEnter username: ");
            username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
            }
        } while (username.isEmpty());

        User user = userManager.findUserByUsername(username);

        if (user != null) {
            System.out.println("Login successful!");
            userDashboard(user);
        } else {
            System.out.println("User not found. Would you like to register? (y/n)");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("y")) {
                registerUserMenu();
            }
        }
    }

    // This registers a new user with input validation
    private static void registerUserMenu() {
        System.out.println("\n===== User Registration =====");

        // Get and validate username
        String username;
        boolean validUsername = false;
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }

            if (username.length() < 4) {
                System.out.println("Username must be at least 4 characters long. Please try again.");
                continue;
            }

            // Check if username already exists
            if (userManager.findUserByUsername(username) != null) {
                System.out.println("Username already taken. Please try another.");
                continue;
            }

            validUsername = true;
        } while (!validUsername);

        // Get and validate email
        String email;
        boolean validEmail = false;
        do {
            System.out.print("Enter email: ");
            email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email cannot be empty. Please try again.");
                continue;
            }

            if (email.length() < 4) {
                System.out.println("Email must be at least 4 characters long. Please try again.");
                continue;
            }

            if (!email.contains("@") || !email.matches(".*@.*\\..*")) {
                System.out.println("Email must contain '@' and a domain (e.g., @something.com). Please try again.");
                continue;
            }

            // Check if email already exists
            if (userManager.findUserByEmail(email) != null) {
                System.out.println("Email already registered. Please try another.");
                continue;
            }

            validEmail = true;
        } while (!validEmail);

        // Get and validate phone number
        String phoneNumber;
        boolean validPhone = false;
        do {
            System.out.print("Enter phone number: ");
            phoneNumber = scanner.nextLine().trim();

            if (phoneNumber.isEmpty()) {
                System.out.println("Phone number cannot be empty. Please try again.");
                continue;
            }

            // Remove any non-digit characters for validation
            String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

            if (digitsOnly.length() != 11) {
                System.out.println("Phone number must be exactly 11 digits long. Please try again.");
                continue;
            }

            // Keep the formatted phone number for storage
            phoneNumber = digitsOnly;
            validPhone = true;
        } while (!validPhone);

        // Create and add the new user
        User newUser = new User(username, email, phoneNumber);
        userManager.addUser(newUser);

        System.out.println("Registration successful! You can now login.");
    }
    // This displays the user's dashboard after login
    private static void userDashboard(User user) {
        boolean userLoggedIn = true;

        while (userLoggedIn) {
            System.out.println("\n===== User Dashboard =====");
            System.out.println("Welcome, " + user.getUsername() + "!");
            System.out.println("1. View My Profile");
            System.out.println("2. Book a Jet");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    user.displayProfile();
                    break;
                case 2:
                    bookJetMenu(user);
                    break;
                case 3:
                    viewUserBookings(user);
                    break;
                case 4:
                    cancelUserBooking(user);
                    break;
                case 5:
                    userLoggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // This handles the jet booking process with input validation
    private static void bookJetMenu(User user) {
        // Display available jets
        System.out.println("\n===== Book a Jet =====");
        jetInventory.displayAllJets();

        // Select a jet
        System.out.print("Enter the number of the jet you want to book (0 to cancel): ");
        int jetIndex = getIntInput() - 1;

        if (jetIndex < 0 || jetIndex >= jetInventory.getJetCount()) {
            System.out.println("Booking cancelled or invalid selection.");
            return;
        }

        Jet selectedJet = jetInventory.getJetByIndex(jetIndex);

        if (!selectedJet.isAvailable()) {
            System.out.println("Sorry, this jet is not available for booking.");
            return;
        }

        // Get and validate booking details
        String departure;
        do {
            System.out.print("Enter departure location (e.g., JFK, LAX): ");
            departure = scanner.nextLine().trim();

            if (departure.isEmpty()) {
                System.out.println("Departure location cannot be empty. Please try again.");
            }
        } while (departure.isEmpty());

        String destination;
        do {
            System.out.print("Enter destination location: ");
            destination = scanner.nextLine().trim();

            if (destination.isEmpty()) {
                System.out.println("Destination location cannot be empty. Please try again.");
            }
        } while (destination.isEmpty());

        String tripType;
        do {
            System.out.print("Enter trip type (One-way/Round-trip): ");
            tripType = scanner.nextLine().trim();

            if (tripType.isEmpty()) {
                System.out.println("Trip type cannot be empty. Please try again.");
            }
        } while (tripType.isEmpty());

        String flightDate;
        do {
            System.out.print("Enter flight date (YYYY-MM-DD): ");
            flightDate = scanner.nextLine().trim();

            if (flightDate.isEmpty()) {
                System.out.println("Flight date cannot be empty. Please try again.");
            }
            // Additional date validation could be added here
        } while (flightDate.isEmpty());

        int flightDuration;
        do {
            System.out.print("Enter estimated flight duration in hours: ");
            flightDuration = getIntInput();

            if (flightDuration <= 0) {
                System.out.println("Flight duration must be greater than 0. Please try again.");
            }
        } while (flightDuration <= 0);

        // Get current date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String bookingDate = today.format(formatter);

        // Create the booking
        Booking newBooking = bookingManager.createBooking(
                user,
                selectedJet,
                departure,
                destination,
                tripType,
                bookingDate,
                flightDate,
                flightDuration
        );

        if (newBooking != null) {
            System.out.println("Booking successful!");
            newBooking.displayBookingDetails();
        } else {
            System.out.println("Booking failed. Please try again later.");
        }
    }

    // This displays all bookings for a user
    private static void viewUserBookings(User user) {
        List<Booking> userBookings = bookingManager.findBookingsByUser(user);

        if (userBookings.isEmpty()) {
            System.out.println("You have no current bookings.");
            return;
        }

        System.out.println("\n===== Your Bookings =====");
        for (int i = 0; i < userBookings.size(); i++) {
            Booking booking = userBookings.get(i);
            System.out.println((i+1) + ". From " + booking.getDeparture() +
                    " to " + booking.getDestination() +
                    " on " + booking.getFlightDate() +
                    " (" + booking.getJet().getModel() + ")");
        }

        System.out.print("Enter booking number to view details (0 to go back): ");
        int bookingIndex = getIntInput() - 1;

        if (bookingIndex >= 0 && bookingIndex < userBookings.size()) {
            userBookings.get(bookingIndex).displayBookingDetails();
        }
    }

    // This cancels a booking for a user
    private static void cancelUserBooking(User user) {
        List<Booking> userBookings = bookingManager.findBookingsByUser(user);

        if (userBookings.isEmpty()) {
            System.out.println("You have no current bookings to cancel.");
            return;
        }

        System.out.println("\n===== Cancel Booking =====");
        for (int i = 0; i < userBookings.size(); i++) {
            Booking booking = userBookings.get(i);
            System.out.println((i+1) + ". From " + booking.getDeparture() +
                    " to " + booking.getDestination() +
                    " on " + booking.getFlightDate() +
                    " (" + booking.getJet().getModel() + ")");
        }

        System.out.print("Enter booking number to cancel (0 to go back): ");
        int bookingIndex = getIntInput() - 1;

        if (bookingIndex >= 0 && bookingIndex < userBookings.size()) {
            Booking bookingToCancel = userBookings.get(bookingIndex);

            System.out.println("Are you sure you want to cancel this booking? (y/n)");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                boolean cancelled = bookingManager.cancelBooking(bookingToCancel);

                if (cancelled) {
                    System.out.println("Booking successfully cancelled.");
                } else {
                    System.out.println("Failed to cancel booking. Please try again.");
                }
            } else {
                System.out.println("Cancellation aborted.");
            }
        }
    }

    // This provides admin access to view all bookings
    private static void adminMenu() {
        System.out.println("\n===== Admin Dashboard =====");
        System.out.println("1. View All Users");
        System.out.println("2. View All Bookings");
        System.out.println("3. View All Jets");
        System.out.println("4. Return to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                userManager.displayAllUsers();
                break;
            case 2:
                bookingManager.displayAllBookings();
                break;
            case 3:
                jetInventory.displayAllJets();
                break;
            case 4:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    // This helper method gets integer input from the user
    private static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}