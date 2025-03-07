import java.util.*;

// This class maintains a collection of all jets available in the rental system
class JetInventory {
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
    
    // ADDED to remove jet from the inventory
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
    public List<Jet> findJets(String type, int minCapacity, boolean mustBeAvailable) {
        List<Jet> matchingJets = new ArrayList<>();

        for (Jet jet : availableJets) {
            // Check if the jet meets all the specified criteria
            boolean typeMatches = type == null || type.isEmpty() || jet.getType().equalsIgnoreCase(type);
            boolean capacityMatches = jet.getSeatCapacity() >= minCapacity;
            boolean availabilityMatches = !mustBeAvailable || jet.isAvailable();

            if (typeMatches && capacityMatches && availabilityMatches) {
                matchingJets.add(jet);
            }
        }

        return matchingJets;
    }

    // This displays all jets in the inventory
    public void displayAllJets() {
       // Print header with formatting
        System.out.printf("%-5s %-25s %-20s %-10s %-12s %-15s\n",
                "No.", "Model", "Type", "Capacity", "Available", "Rate ($/hour)");
        System.out.println("----------------------------------------------------------------------");

        for (int i = 0; i < availableJets.size(); i++) {
            Jet jet = availableJets.get(i);
            System.out.printf("%-5d %-25s %-20s %-10d %-12s $%-15.2f\n",
                    (i+1),
                    jet.getModel(),
                    jet.getType(),
                    jet.getSeatCapacity(),
                    (jet.isAvailable() ? "Yes" : "No"),
                    jet.getHourlyRate());
        }
        System.out.println("----------------------------------------------------------------------");
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
