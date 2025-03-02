import java.util.*;

// This class represents a private jet that customers can rent
class Jet {
    // These variables store information about each jet
    private String model;         // The name/model of the jet (e.g., Gulfstream G650)
    private String type;          // The category of the jet (e.g., Ultra Long Range)
    private int seatCapacity;     // How many passengers can fit in the jet
    private boolean isAvailable;   // Whether the jet can be booked (true) or not (false)
    private double hourlyRate;    // How much it costs per hour to rent this jet

    // This is a constructor - it creates a new Jet with all its information
    public Jet(String model, String type, int seatCapacity, boolean isAvailable, double hourlyRate) {
        this.model = model;
        this.type = type;
        this.seatCapacity = seatCapacity;
        this.isAvailable = isAvailable;
        this.hourlyRate = hourlyRate;
    }

    // These "getter" methods allow other parts of the program to access the jet's information
    public String getModel() {
        return model;
    }

    // These "setter" methods allow other parts of the program to change the jet's information
    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    // This converts the jet's information into a readable text format
    @Override

    public String toString() {
        return "Model: " + model + ", Type: " + type + ", Capacity: " + seatCapacity +
                ", Available: " + (isAvailable ? "Yes" : "No") + ", Rate: $" + hourlyRate + "/hour";
    }
}