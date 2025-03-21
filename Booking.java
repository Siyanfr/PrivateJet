import java.util.*;

class Booking {
    // These variables store information about each booking
    private User user;             // Which customer made this booking
    private Jet jet;               // Which jet was booked
    private String departure;      // Where the flight will take off from
    private String destination;    // Where the flight will land
    private String tripType;       // Whether it's one-way or round-trip
    private String bookingDate;    // When the booking was made
    private String flightDate;     // When the flight will happen
    private int flightDuration;    // How long the flight will last (in hours)
    private double totalCost;      // How much the booking costs in total
    private double change;

    // This is a constructor - it creates a new Booking with all the necessary information
    public Booking(User user, Jet jet, String departure, String destination,
                   String tripType, String bookingDate, String flightDate, int flightDuration, double change) {
        this.user = user;
        this.jet = jet;
        this.departure = departure;
        this.destination = destination;
        this.tripType = tripType;
        this.bookingDate = bookingDate;
        this.flightDate = flightDate;
        this.flightDuration = flightDuration;
        this.change = change;

        // Calculate the total cost based on jet hourly rate and flight duration
        calculateTotalCost();

        // Make the jet unavailable since it's now booked
        jet.setAvailable(false);
    }

    // These "getter" methods allow other parts of the program to access the booking's information
    public User getUser() {
        return user;
    }

    public Jet getJet() {
        return jet;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getTripType() {
        return tripType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    // These "setter" methods allow other parts of the program to change the booking's information
    public void setUser(User user) {
        this.user = user;
    }

    public void setJet(Jet jet) {
        this.jet = jet;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
        calculateTotalCost();
    }

    // The Proceeding Methods are simple codes that are required in the booking process
    private void calculateTotalCost() {
        double multiplier = tripType.equalsIgnoreCase("Round-trip") ? 2.0 : 1.0;
        this.totalCost = jet.getHourlyRate() * flightDuration * multiplier;
    }

    public void cancelBooking() {
        jet.setAvailable(true);
    }

    public void displayBookingDetails() {
        UserInterface.printSubHeader("\n----- Booking Details -----");
        UserInterface.printInfo("User: " + user.getUsername());
        UserInterface.printInfo("Jet: " + jet.getModel() + " (" + jet.getType() + ")");
        UserInterface.printInfo("Route: " + departure + " to " + destination);
        UserInterface.printInfo("Trip Type: " + tripType);
        UserInterface.printInfo("Booking Date: " + bookingDate);
        UserInterface.printInfo("Flight Date: " + flightDate);
        UserInterface.printInfo("Flight Duration: " + flightDuration + " hours");
        UserInterface.printInfo("Total Cost: $" + String.format("%.2f", totalCost));
        UserInterface.printInfo("Change: $" + String.format("%.2f", change));
        UserInterface.printSubHeader("--------------------------");
    }
}
