import java.util.*;
import java.util.Scanner;

public class JetBrowsingManager {
    private JetInventory jetInventory;
    private UserManager userManager;
    private BookingManager bookingManager;
    private Scanner scanner;

    // Constructor to initialize JetBrowsingManager with dependencies
    public JetBrowsingManager(JetInventory jetInventory, UserManager userManager, BookingManager bookingManager) {
        this.jetInventory = jetInventory;
        this.userManager = userManager;
        this.bookingManager = bookingManager;
        this.scanner = new Scanner(System.in);
    }

    // Displays the menu for browsing jets and handles user choices
    public void browseJetsMenu() {
        UserInterface.printHeader("===== Available Jets =====");
        jetInventory.displayAllJets();
        UserInterface.printSubHeader("Options:");
        UserInterface.printMenuOption(1, "Filter Jets by Type");
        UserInterface.printMenuOption(2, "Filter Jets by Capacity");
        UserInterface.printMenuOption(3, "Filter Jets by Budget Range");
        UserInterface.printMenuOption(4, "Book a Jet (requires login)");
        UserInterface.printMenuOption(5, "Return to Main Menu");
        UserInterface.printPrompt("Enter your choice: ");

        int choice = InputValidator.getIntInput();

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
                UserInterface.printInfo("Please login first to book a jet.");
                UserMenuManager userMenu = new UserMenuManager(jetInventory, userManager, bookingManager);
                userMenu.userLoginMenu();
                break;
            case 5:
                // Return to main menu
                break;
            default:
                UserInterface.printError("Invalid choice. Returning to main menu.");
        }
    }

    // Filters jets based on the selected type
    public List<Jet> filterJetsByType() {
        UserInterface.printSubHeader("Available jet types:");
        UserInterface.printMenuOption(1, "Ultra Long Range");
        UserInterface.printMenuOption(2, "Large Jet");
        UserInterface.printMenuOption(3, "Super Mid-Size");
        UserInterface.printMenuOption(4, "Mid-Size");
        UserInterface.printMenuOption(5, "Light Jet");
        UserInterface.printPrompt("Select jet type (enter the number): ");

        int type_jet = InputValidator.getIntInput();
        String type = null;

        switch (type_jet) {
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
                UserInterface.printError("Invalid choice. Returning to main menu.");
                return java.util.Collections.emptyList(); // Return empty list if invalid choice
        }

        List<Jet> filteredJets = jetInventory.findJets(type, 0, null);

        UserInterface.printHeader("Filtered Jets");
        if (filteredJets.isEmpty()) {
            UserInterface.printError("No jets found matching your criteria.");
        } else {
            jetInventory.displayFilteredJets(filteredJets);
        }
        return filteredJets;
    }

    // Filters jets based on the minimum seat capacity
    public List<Jet> filterJetsByCapacity() {
        int minCapacity;

        while (true) {
            UserInterface.printPrompt("\nEnter minimum seat capacity required: ");
            String input = scanner.nextLine().trim();

            try {
                minCapacity = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                UserInterface.printError("Invalid input. Please enter a number.");
            }
        }

        List<Jet> filteredJets = jetInventory.findJets(null, minCapacity, null);

        UserInterface.printHeader("Filtered Jets");
        jetInventory.displayFilteredJets(filteredJets);
        return filteredJets;
    }

    // Filters jets based on the user's budget range
    public List<Jet> filterJetsByBudget() {
        UserInterface.printPrompt("\nEnter your minimum hourly budget: $");
        double minBudget = InputValidator.getDoubleInput();

        UserInterface.printPrompt("Enter your maximum hourly budget: $");
        double maxBudget = InputValidator.getDoubleInput();

        // Validate budget range
        if (minBudget > maxBudget) {
            UserInterface.printInfo("Minimum budget cannot be greater than maximum budget. Swapping values.");
            double temp = minBudget;
            minBudget = maxBudget;
            maxBudget = temp;
        }

        List<Jet> budgetJets = new ArrayList<>();
        for (Jet jet : jetInventory.findJets(null, 0, null)) {
            if (jet.getHourlyRate() >= minBudget && jet.getHourlyRate() <= maxBudget) {
                budgetJets.add(jet);
            }
        }

        UserInterface.printHeader("Jets in Budget Range $" + minBudget + " - $" + maxBudget + " per hour");
        jetInventory.displayFilteredJets(budgetJets);
        return budgetJets;
    }
}
