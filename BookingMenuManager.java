import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingMenuManager {
    private JetInventory jetInventory;
    private UserManager userManager;
    private BookingManager bookingManager;
    private Scanner scanner;
    private JetBrowsingManager jetBrowsingManager;

    public BookingMenuManager(JetInventory jetInventory, UserManager userManager, BookingManager bookingManager) {
        this.jetInventory = jetInventory;
        this.userManager = userManager;
        this.bookingManager = bookingManager;
        this.scanner = new Scanner(System.in);
        this.jetBrowsingManager = new JetBrowsingManager(jetInventory, userManager, bookingManager);
    }

    public void bookJetMenu(User user) {
        List<Jet> filteredJets = new ArrayList<>();
        int filterChoice;
        while (true) {
            UserInterface.printSubHeader("\n===== Jet Filtering Options =====");
            UserInterface.printMenuOption(1, "Filter by Jet Type");
            UserInterface.printMenuOption(2, "Filter by Passenger Capacity");
            UserInterface.printMenuOption(3, "Filter by Budget Range");
            UserInterface.printMenuOption(4, "Show All Available Jets");
            UserInterface.printPrompt("Choose a filtering method: ");

            filterChoice = InputValidator.getIntInput();

            switch (filterChoice) {
                case 1:
                    filteredJets = jetBrowsingManager.filterJetsByType();
                    break;
                case 2:
                    filteredJets = jetBrowsingManager.filterJetsByCapacity();
                    break;
                case 3:
                    filteredJets = jetBrowsingManager.filterJetsByBudget();
                    break;
                case 4:
                    filteredJets = jetInventory.findJets(null, 0, null);
                    jetInventory.displayFilteredJets(filteredJets);
                    break;
                default:
                    UserInterface.printError("Invalid choice. Input Only 1-4");
                    continue;
            }
            break;
        }

        // Display filtered jets
        if (filteredJets.isEmpty()) {
            UserInterface.printError("No jets found matching your criteria.");
            return;
        }

        // Jet selection process
        UserInterface.printPrompt("Enter the number of the jet you want to book (0 to cancel): ");
        int jetIndex = InputValidator.getIntInput() - 1;

        if (jetIndex < 0 || jetIndex >= filteredJets.size()) {
            UserInterface.printError("Booking cancelled or invalid selection.");
            return;
        }

        Jet selectedJet = filteredJets.get(jetIndex);

        if (!selectedJet.isAvailable()) {
            UserInterface.printError("Sorry, this jet is not available for booking.");
            return;
        }

        // Get booking details
        String departure = InputValidator.doNotAcceptInt("Enter departure location (e.g., JFK, LAX): ", false);
        String destination = InputValidator.doNotAcceptInt("Enter destination location: ", false);

        String tripType = "";
        do {
            UserInterface.printPrompt("Enter trip type (1 for One-way, 2 for Round-trip): ");
            int tripChoice = InputValidator.getIntInput();

            switch (tripChoice) {
                case 1:
                    tripType = "One-way";
                    break;
                case 2:
                    tripType = "Round-trip";
                    break;
                default:
                    UserInterface.printError("Invalid choice. Please enter 1 or 2.");
            }
        } while (tripType.isEmpty());

        String flightDate = InputValidator.getStringInput("Enter flight date (YYYY-MM-DD): ", false);

        int flightDuration;
        do {
            UserInterface.printPrompt("Enter estimated flight duration in hours: ");
            flightDuration = InputValidator.getIntInput();

            if (flightDuration <= 0) {
                UserInterface.printError("Flight duration must be greater than 0. Please try again.");
            }
        } while (flightDuration <= 0);

        // Payment Process
        double totalCost = selectedJet.getHourlyRate() * flightDuration * (tripType.equalsIgnoreCase("Round-trip") ? 2.0 : 1.0);
        UserInterface.printHeader("The total cost for this booking is: $" + String.format("%.2f", totalCost));

        double userBudget;
        do {
            UserInterface.printPrompt("Enter your available budget: ");
            userBudget = InputValidator.getDoubleInput();

            if (userBudget < totalCost) {
                UserInterface.printError("Insufficient funds. Please enter a valid budget.");
            }
        } while (userBudget < totalCost);

        double change = userBudget - totalCost;

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
                flightDuration,
                change
        );

        if (newBooking != null) {
            selectedJet.setAvailable(false);
            UserInterface.printSuccess("Booking successful!");
            newBooking.displayBookingDetails();
        } else {
            UserInterface.printError("Booking failed. Please try again later.");
        }
    }
}
