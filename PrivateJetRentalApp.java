import java.util.Scanner;
import java.util.*;

public class PrivateJetRentalApp {

    public static void main(String[] args) {
        // Initialize the system
        JetInventory jetInventory = new JetInventory();
        UserManager userManager = new UserManager();
        BookingManager bookingManager = new BookingManager();

        // Load data
        boolean dataLoaded = FileManager.loadJetsFromCSV(jetInventory);
        if (!dataLoaded) {
            // Load default jets if CSV read fails
            loadDefaultJets(jetInventory);
        }

        // Start the application
        MainMenuManager mainMenu = new MainMenuManager(jetInventory, userManager, bookingManager);
        mainMenu.displayMainMenu();
    }

    private static void loadDefaultJets(JetInventory jetInventory) {
        jetInventory.addJet(new Jet("Gulfstream G650", "Ultra Long Range", 18, true, 12000.0));
        jetInventory.addJet(new Jet("Cessna Citation X", "Super Mid-Size", 8, true, 5500.0));
        jetInventory.addJet(new Jet("Bombardier Global 7500", "Ultra Long Range", 19, true, 15000.0));
        jetInventory.addJet(new Jet("Embraer Phenom 300", "Light Jet", 7, true, 3000.0));
        jetInventory.addJet(new Jet("Dassault Falcon 8X", "Large Jet", 14, true, 9500.0));
    }
}
