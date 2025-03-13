import java.util.*;

// This class maintains a collection of all jets available in the rental system
class JetInventory {
    // ANSI escape codes for text styles
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    // Foreground colors
    private static final String FG_BLACK = "\u001B[30m";
    private static final String FG_RED = "\u001B[31m";
    private static final String FG_GREEN = "\u001B[32m";
    private static final String FG_YELLOW = "\u001B[33m";
    private static final String FG_BLUE = "\u001B[34m";
    private static final String FG_MAGENTA = "\u001B[35m";
    private static final String FG_CYAN = "\u001B[36m";
    private static final String FG_WHITE = "\u001B[37m";

    // Bright foreground colors
    private static final String FG_BRIGHT_BLACK = "\u001B[90m";
    private static final String FG_BRIGHT_RED = "\u001B[91m";
    private static final String FG_BRIGHT_GREEN = "\u001B[92m";
    private static final String FG_BRIGHT_YELLOW = "\u001B[93m";
    private static final String FG_BRIGHT_BLUE = "\u001B[94m";
    private static final String FG_BRIGHT_MAGENTA = "\u001B[95m";
    private static final String FG_BRIGHT_CYAN = "\u001B[96m";
    private static final String FG_BRIGHT_WHITE = "\u001B[97m";

    // Background colors
    private static final String BG_BLACK = "\u001B[40m";
    private static final String BG_RED = "\u001B[41m";
    private static final String BG_GREEN = "\u001B[42m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String BG_BLUE = "\u001B[44m";
    private static final String BG_MAGENTA = "\u001B[45m";
    private static final String BG_CYAN = "\u001B[46m";
    private static final String BG_WHITE = "\u001B[47m";

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

    public boolean removeJetModel(String model) {
        Iterator<Jet> iterator = availableJets.iterator();
        while (iterator.hasNext()) {
            Jet jet = iterator.next();
            if (jet.getModel().equalsIgnoreCase(model)) {
                iterator.remove();
                return true; // Successfully removed
            }
        }
        return false; // Jet model not found
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
