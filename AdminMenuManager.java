import java.util.Scanner;

public class AdminMenuManager {
    private JetInventory jetInventory;
    private UserManager userManager;
    private BookingManager bookingManager;
    private Scanner scanner;

    public AdminMenuManager(JetInventory jetInventory, UserManager userManager, BookingManager bookingManager) {
        this.jetInventory = jetInventory;
        this.userManager = userManager;
        this.bookingManager = bookingManager;
        this.scanner = new Scanner(System.in);
    }

    public void displayAdminMenu() {
        boolean admin_loop = true;
        while (admin_loop) {
            UserInterface.printSubHeader("\n===== Admin Dashboard =====");
            UserInterface.printMenuOption(1, "View All Users");
            UserInterface.printMenuOption(2, "View All Bookings");
            UserInterface.printMenuOption(3, "View All Jets");
            UserInterface.printMenuOption(4, "Add New Jet");
            UserInterface.printMenuOption(5, "Delete a Jet");
            UserInterface.printMenuOption(6, "Return to Main Menu");
            UserInterface.printPrompt("Enter your choice: ");

            int choice = InputValidator.getIntInput();

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
                    deleteJetMenu();
                    break;
                case 6:
                    admin_loop = false;
                    break;
                default:
                    UserInterface.printError("Invalid choice. Returning to main menu.");
            }
        }
    }

    private void addNewJetMenu() {
        UserInterface.printSubHeader("\n===== Add New Jet =====");

        // Create a new jet with default values
        Jet newJet = new Jet("", "", 0, true, 0.0);

        // Get and validate jet model
        String model = InputValidator.getStringInput("Enter jet model (e.g., Gulfstream G650): ", false);
        newJet.setModel(model);

        // Get and validate jet type
        String type;
        do {
            UserInterface.printSubHeader("Available jet types:");
            UserInterface.printMenuOption(1, "Ultra Long Range");
            UserInterface.printMenuOption(2, "Large Jet");
            UserInterface.printMenuOption(3, "Super Mid-Size");
            UserInterface.printMenuOption(4, "Mid-Size");
            UserInterface.printMenuOption(5, "Light Jet");
            UserInterface.printPrompt("Select jet type (enter the number): ");

            int typeChoice = InputValidator.getIntInput();
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
                    UserInterface.printError("Invalid type. Please try again.");
                    type = null;
            }
        } while (type == null);
        newJet.setType(type);

        // Get and validate seat capacity
        int seatCapacity;
        do {
            UserInterface.printPrompt("Enter seat capacity (1-20): ");
            seatCapacity = InputValidator.getIntInput();
            if (seatCapacity < 1 || seatCapacity > 20) {
                UserInterface.printError("Seat capacity must be between 1 and 20. Please try again.");
                seatCapacity = 0;
            }
        } while (seatCapacity == 0);
        newJet.setSeatCapacity(seatCapacity);

        // Get and validate hourly rate
        double hourlyRate;
        do {
            UserInterface.printPrompt("Enter hourly rental rate ($1000 - $20000): ");
            hourlyRate = InputValidator.getDoubleInput();
            if (hourlyRate < 1000 || hourlyRate > 20000) {
                UserInterface.printError("Hourly rate must be between $1000 and $20000. Please try again.");
                hourlyRate = 0;
            }
        } while (hourlyRate == 0);
        newJet.setHourlyRate(hourlyRate);

        // Add the jet to the inventory
        jetInventory.addJet(newJet);

        UserInterface.printSuccess("\nNew Jet Added Successfully!");
        System.out.println(newJet);

        // Optional: Ask if user wants to add another jet
        boolean addAnother = InputValidator.getYesNoInput("Would you like to add another jet?");
        if (addAnother) {
            addNewJetMenu();
        }
    }

    public void deleteJetMenu() {
        UserInterface.printHeader("==== Admin - Delete a Jet ====");

        jetInventory.displayAllJets();

        UserInterface.printPrompt("Enter the number of the jet to delete: ");
        int jetNumber = InputValidator.getIntInput();  // Get the jet number input

        if (jetNumber < 1 || jetNumber > jetInventory.getJetCount()) {
            UserInterface.printError("Invalid jet number. Please try again.");
            return;
        }

        Jet jetToDelete = jetInventory.getJetByIndex(jetNumber - 1);
        boolean jetRemoved = jetInventory.removeJet(jetToDelete);

        if (jetRemoved) {
            UserInterface.printSuccess("Jet model '" + jetToDelete.getModel() + "' removed successfully.");
        } else {
            UserInterface.printError("Failed to remove the jet. Please try again.");
        }

        boolean continueRemoval = InputValidator.getYesNoInput("Do you want to delete another jet?");
        if (continueRemoval) {
            deleteJetMenu();
        } else {
            UserInterface.printInfo("Returning to Admin Menu...");
        }
    }
}
