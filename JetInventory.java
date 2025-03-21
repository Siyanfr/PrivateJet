import java.util.*;

// This class maintains a collection of all jets available in the rental system
class JetInventory {
    // ANSI escape codes for text styles
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    // Foreground colors
    private static final String FG_RED = "\u001B[31m";
    private static final String FG_GREEN = "\u001B[32m";
    private static final String FG_YELLOW = "\u001B[33m";
    private static final String FG_CYAN = "\u001B[36m";

    // Bright foreground colors
    private static final String FG_BRIGHT_RED = "\u001B[91m";
    private static final String FG_BRIGHT_GREEN = "\u001B[92m";
    private static final String FG_BRIGHT_MAGENTA = "\u001B[95m";


    // A list that holds all jets in the inventory
    private List<Jet> availableJets;

    // This is a constructor - it creates a new empty inventory
    public JetInventory() {
        this.availableJets = new ArrayList<>();
    }

    // This adds a new jet to the inventory
    public void addJet(Jet jet) {
        availableJets.add(jet);
    }


    // This removes a jet from the inventory
    public boolean removeJet(Jet jet) {
        return availableJets.remove(jet);
    }

    // This finds jets that match specific criteria
    public List<Jet> findJets(String type, int minCapacity, Boolean mustBeAvailable) { // changed this
        List<Jet> matchingJets = new ArrayList<>();

        for (Jet jet : availableJets) {
            // Check if the jet meets all the specified criteria
            boolean typeMatches = type == null || type.isEmpty() || jet.getType().equalsIgnoreCase(type);
            boolean capacityMatches = jet.getSeatCapacity() >= minCapacity;
            boolean availabilityMatches = mustBeAvailable == null || !mustBeAvailable || jet.isAvailable();

            if (typeMatches && capacityMatches && availabilityMatches) {
                matchingJets.add(jet);
            }
        }

        return matchingJets;
    }

    // Helper method to style header text
    private String styleHeader(String text) {
        return BOLD + text + RESET;
    }

    // Helper method to style section dividers
    private String styleDivider() {
        return FG_CYAN + "=========================================================================================================" + RESET;
    }

    // Helper method to style row data based on availability
    private String styleRowData(Jet jet, int index) {
        String style;
        if (jet.isAvailable()) {
            style = (index % 2 == 0) ? FG_GREEN : FG_BRIGHT_GREEN;
        } else {
            style = (index % 2 == 0) ? FG_RED : FG_BRIGHT_RED;
        }
        return style;
    }

    // Helper method to style availability status
    private String styleAvailability(boolean isAvailable) {
        if (isAvailable) {
            return FG_BRIGHT_GREEN + BOLD + "Yes" + RESET;
        } else {
            return FG_BRIGHT_RED + BOLD + "No" + RESET;
        }
    }

    // Helper method to style price
    private String stylePrice(double price) {
        return FG_YELLOW + BOLD + String.format("$%.2f", price) + RESET;
    }

    // This displays all jets in the inventory
    public void displayAllJets() {
        // Print styled header
        System.out.println(styleDivider());
        System.out.printf("%s %-15s %s %-34s %s %-27s %s %-10s %s %-12s %s %-15s %s\n",
                styleHeader(""),
                styleHeader("No."),
                styleHeader(""),
                styleHeader("Model"),
                styleHeader(""),
                styleHeader("Type"),
                styleHeader(""),
                styleHeader("Capacity"),
                styleHeader(""),
                styleHeader("Available"),
                styleHeader(""),
                styleHeader("Rate ($/hour)"),
                RESET);
        System.out.println(styleDivider());

        // Print each jet with alternating row styles
        for (int i = 0; i < availableJets.size(); i++) {
            Jet jet = availableJets.get(i);
            String rowStyle = styleRowData(jet, i);

            System.out.printf("| %s%-5d | %-25s | %-20s | %-10d | %s | %-15s%s\n",
                    rowStyle,
                    (i+1),
                    jet.getModel(),
                    jet.getType(),
                    jet.getSeatCapacity(),
                    styleAvailability(jet.isAvailable()),
                    stylePrice(jet.getHourlyRate()),
                    RESET);
        }

        // Print footer with total count
        System.out.println(styleDivider());
        System.out.println(FG_BRIGHT_MAGENTA + BOLD +
                "Total Jets: " + availableJets.size() + RESET);
        System.out.println(styleDivider());
    }

    public void displayFilteredJets(List<Jet> filteredJets) {
        // Print styled header
        System.out.println(styleDivider());
        System.out.printf("%s %-15s %s %-34s %s %-27s %s %-10s %s %-12s %s %-15s %s\n",
                styleHeader(""),
                styleHeader("No."),
                styleHeader(""),
                styleHeader("Model"),
                styleHeader(""),
                styleHeader("Type"),
                styleHeader(""),
                styleHeader("Capacity"),
                styleHeader(""),
                styleHeader("Available"),
                styleHeader(""),
                styleHeader("Rate ($/hour)"),
                RESET);
        System.out.println(styleDivider());

        // Print each jet with alternating row styles
        for (int i = 0; i < filteredJets.size(); i++) {
            Jet jet = filteredJets.get(i);
            String rowStyle = styleRowData(jet, i);

            System.out.printf("| %s%-5d | %-25s | %-20s | %-10d | %s | %-15s%s\n",
                    rowStyle,
                    (i+1),
                    jet.getModel(),
                    jet.getType(),
                    jet.getSeatCapacity(),
                    styleAvailability(jet.isAvailable()),
                    stylePrice(jet.getHourlyRate()),
                    RESET);
        }

        // Print footer with total count
        System.out.println(styleDivider());
        System.out.println(FG_BRIGHT_MAGENTA + BOLD +
                "Total Jets: " + filteredJets.size() + RESET);
        System.out.println(styleDivider());
    }

    // This gets a jet by its index in the list
    public Jet getJetByIndex(int index) {
        if (index >= 0 && index < availableJets.size()) {
            return availableJets.get(index);
        }
        return null;
    }

    // This counts how many jets are in the inventory
    public int getJetCount() {
        return availableJets.size();
    }
}
