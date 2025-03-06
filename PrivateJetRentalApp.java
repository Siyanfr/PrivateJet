import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;

// This is the main class that runs the private jet rental application
public class PrivateJetRentalApp {
    // These are the managers that handle different parts of the system
    private static JetInventory jetInventory;
    private static UserManager userManager;
    private static BookingManager bookingManager;
    private static Scanner scanner;
    private static User currentUser;

    // This is the entry point of the program
    public static void main(String[] args) {
        // Initialize the system
        initializeSystem();
        loadJetsFromCSV();
        displayMainMenu();
    }

    private static void loadJetsFromCSV() {
        String csvFile = "jets_data.csv";
        System.out.println("Attempting to load jets from: " + new File(csvFile).getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Print out the current working directory
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Skip header
            String headerLine = br.readLine();
            System.out.println("Header line: " + headerLine);

            String line;
            int jetCount = 0;
            while ((line = br.readLine()) != null) {
                System.out.println("Processing line: " + line);

                String[] jetData = line.split(",");

                // Ensure we have all required data
                if (jetData.length >= 4) {
                    try {
                        Jet jet = new Jet(
                                jetData[0],                     // model
                                jetData[1],                     // type
                                Integer.parseInt(jetData[2]),   // seat capacity
                                true,                           // availability
                                Double.parseDouble(jetData[3])  // hourly rate
                        );
                        jetInventory.addJet(jet);
                        jetCount++;
                        System.out.println("Added jet: " + jet);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing jet data: " + Arrays.toString(jetData));
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Insufficient data in line: " + line);
                }
            }

            System.out.println("Total jets loaded: " + jetCount);
        } catch (IOException e) {
            System.out.println("Error reading jet data from CSV: " + e.getMessage());
            e.printStackTrace();

            // Load some default jets if CSV read fails
            loadDefaultJets();
        }
    }

    private static void loadDefaultJets() {
        jetInventory.addJet(new Jet("Gulfstream G650", "Ultra Long Range", 18, true, 12000.0));
        jetInventory.addJet(new Jet("Cessna Citation X", "Super Mid-Size", 8, true, 5500.0));
        // Add more default jets as needed
    }

    // This sets up the basic system components
    private static void initializeSystem() {
        jetInventory = new JetInventory();
        userManager = new UserManager();
        bookingManager = new BookingManager();
        scanner = new Scanner(System.in);
    }


    // This displays the main menu and handles user input
    private static void displayMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== Private Jet Rental Application =====");
            System.out.println("1. Browse Available Jets");
            System.out.println("2. User Login");
            System.out.println("3. Register New User");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

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
        System.out.println("3. Filter Jets by Budget Range");
        System.out.println("4. Book a Jet (requires login)");
        System.out.println("5. Return to Main Menu");
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
                filterJetsByBudget();
                break;
            case 4:
                System.out.println("Please login first to book a jet.");
                userLoginMenu();
                break;
            case 5:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    // This filters jets by their type
    private static List<Jet> filterJetsByType() {
        System.out.println("\nAvailable jet types: Ultra Long Range, Large Jet, Super Mid-Size, Light Jet");
        System.out.print("Enter jet type to filter by: ");
        String type = scanner.nextLine();

        List<Jet> filteredJets = jetInventory.findJets(type, 0, true);

        System.out.println("\n----- Filtered Jets -----");
        if (filteredJets.isEmpty()) {
            System.out.println("No jets found matching your criteria.");
        } else {
            for (int i = 0; i < filteredJets.size(); i++) {
                System.out.println((i + 1) + ". " + filteredJets.get(i));
            }
        }
        return filteredJets;
    }

    // This filters jets by their passenger capacity
    private static List<Jet> filterJetsByCapacity() {
        System.out.print("\nEnter minimum seat capacity required: ");
        int minCapacity = getIntInput();

        List<Jet> filteredJets = jetInventory.findJets(null, minCapacity, true);

        System.out.println("\n----- Filtered Jets -----");
        if (filteredJets.isEmpty()) {
            System.out.println("No jets found matching your criteria.");
        } else {
            for (int i = 0; i < filteredJets.size(); i++) {
                System.out.println((i + 1) + ". " + filteredJets.get(i));
            }
        }
        return filteredJets;
    }

    private static List<Jet> filterJetsByBudget() {
        System.out.print("\nEnter your minimum hourly budget: $");
        double minBudget = getDoubleInput();

        System.out.print("Enter your maximum hourly budget: $");
        double maxBudget = getDoubleInput();

        // Validate budget range
        if (minBudget > maxBudget) {
            System.out.println("Minimum budget cannot be greater than maximum budget. Swapping values.");
            double temp = minBudget;
            minBudget = maxBudget;
            maxBudget = temp;
        }

        List<Jet> budgetJets = new ArrayList<>();
        for (Jet jet : jetInventory.findJets(null, 0, true)) {
            if (jet.getHourlyRate() >= minBudget && jet.getHourlyRate() <= maxBudget) {
                budgetJets.add(jet);
            }
        }

        // Display filtered jets
        System.out.println("\n----- Jets in Budget Range $" + minBudget + " - $" + maxBudget + " per hour -----");
        if (budgetJets.isEmpty()) {
            System.out.println("No jets found in your specified budget range.");
        } else {
            for (int i = 0; i < budgetJets.size(); i++) {
                System.out.println((i + 1) + ". " + budgetJets.get(i));
            }
            System.out.println("Total jets found: " + budgetJets.size());
        }

        return budgetJets;
    }

    // Helper method to get double input
    private static double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
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

        // Special admin access
        if (username.equals("A@dMin")) {
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine().trim();

            // Simple password check (in a real system, use secure authentication)
            if (password.equals("admin123")) {
                System.out.println("Admin login successful!");
                adminMenu();
                return;
            } else {
                System.out.println("Incorrect admin credentials.");
                return;
            }
        }

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
        // New filtering options menu
        System.out.println("\n===== Jet Filtering Options =====");
        System.out.println("1. Filter by Jet Type");
        System.out.println("2. Filter by Passenger Capacity");
        System.out.println("3. Filter by Budget Range");
        System.out.println("4. Show All Available Jets");
        System.out.print("Choose a filtering method: ");

        int filterChoice = getIntInput();
        List<Jet> filteredJets = new ArrayList<>();

        switch (filterChoice) {
            case 1:
                filteredJets = filterJetsByType();
                break;
            case 2:
                filteredJets = filterJetsByCapacity();
                break;
            case 3:
                filteredJets = filterJetsByBudget();
                break;
            case 4:
                filteredJets = jetInventory.findJets(null, 0, true);
                break;
            default:
                System.out.println("Invalid choice. Showing all available jets.");
                filteredJets = jetInventory.findJets(null, 0, true);
        }

        // Display filtered jets
        if (filteredJets.isEmpty()) {
            System.out.println("No jets found matching your criteria.");
            return;
        }

        System.out.println("\n===== Available Jets =====");
        for (int i = 0; i < filteredJets.size(); i++) {
            System.out.println((i + 1) + ". " + filteredJets.get(i));
        }

        // Jet selection process remains the same as in the original method
        System.out.print("Enter the number of the jet you want to book (0 to cancel): ");
        int jetIndex = getIntInput() - 1;

        if (jetIndex < 0 || jetIndex >= filteredJets.size()) {
            System.out.println("Booking cancelled or invalid selection.");
            return;
        }

        Jet selectedJet = filteredJets.get(jetIndex);

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
            System.out.println((i + 1) + ". From " + booking.getDeparture() +
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
            System.out.println((i + 1) + ". From " + booking.getDeparture() +
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
        System.out.println("4. Add New Jet");
        System.out.println("5. Delete a Jet"); // ADDED Option to delete jets
        System.out.println("6. Return to Main Menu");
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
                addNewJetMenu();
                break;
            case 5:
                deleteJetMenu(); // ADDED Calls delete jet method
                break;
            case 6:
                // Return to main menu
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    // This method handles adding a new jet to the system
    private static void addNewJetMenu() {
        System.out.println("\n===== Add New Jet =====");

        // Create a new jet with default values
        Jet newJet = new Jet("", "", 0, true, 0.0);

        // Get and validate jet model
        String model;
        do {
            System.out.print("Enter jet model (e.g., Gulfstream G650): ");
            model = scanner.nextLine().trim();
            if (model.isEmpty()) {
                System.out.println("Jet model cannot be empty. Please try again.");
            }
        } while (model.isEmpty());
        newJet.setModel(model);

        // Get and validate jet type
        String type;
        do {
            System.out.println("Available jet types:");
            System.out.println("1. Ultra Long Range");
            System.out.println("2. Large Jet");
            System.out.println("3. Super Mid-Size");
            System.out.println("4. Mid-Size");
            System.out.println("5. Light Jet");
            System.out.print("Select jet type (enter the number): ");

            int typeChoice = getIntInput();
            switch (typeChoice) {
                case 1:
                    type = "Ultra Long Range";
                    break;
                case 2:
                    type = "Large Jet";
                    break;
                case 3:
                    type = "Super Mid-Size";
                    break;
                case 4:
                    type = "Mid-Size";
                    break;
                case 5:
                    type = "Light Jet";
                    break;
                default:
                    System.out.println("Invalid type. Please try again.");
                    type = null;
            }
        } while (type == null);
        newJet.setType(type);

        // Get and validate seat capacity
        int seatCapacity;
        do {
            System.out.print("Enter seat capacity (1-20): ");
            seatCapacity = getIntInput();
            if (seatCapacity < 1 || seatCapacity > 20) {
                System.out.println("Seat capacity must be between 1 and 20. Please try again.");
                seatCapacity = 0;
            }
        } while (seatCapacity == 0);
        newJet.setSeatCapacity(seatCapacity);

        // Get and validate hourly rate
        double hourlyRate;
        do {
            System.out.print("Enter hourly rental rate ($1000 - $20000): ");
            hourlyRate = getDoubleInput();
            if (hourlyRate < 1000 || hourlyRate > 20000) {
                System.out.println("Hourly rate must be between $1000 and $20000. Please try again.");
                hourlyRate = 0;
            }
        } while (hourlyRate == 0);
        newJet.setHourlyRate(hourlyRate);

        // Add the jet to the inventory
        jetInventory.addJet(newJet);

        System.out.println("\nNew Jet Added Successfully!");
        System.out.println(newJet);

        // Optional: Ask if user wants to add another jet
        System.out.print("\nWould you like to add another jet? (y/n): ");
        String addAnother = scanner.nextLine().trim();
        if (addAnother.equalsIgnoreCase("y")) {
            addNewJetMenu();
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

    private static void deleteJetMenu() { // ADDED: New method to delete jets
        System.out.println("\n===== Delete a Jet =====");
        jetInventory.displayAllJets();
        System.out.print("Enter the model of the jet to delete: ");
        String modelToDelete = scanner.nextLine();

        System.out.print("Are you sure you want to delete " + modelToDelete + "? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y")) {
            boolean removed = jetInventory.removeJetModel(modelToDelete);
            if (removed) {
                System.out.println("Jet successfully removed.");
            } else {
                System.out.println("Jet model not found.");
            }
        }
    }
}
