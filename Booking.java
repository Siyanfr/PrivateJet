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

    // This is a constructor - it creates a new Booking with all the necessary information
    public Booking(User user, Jet jet, String departure, String destination,
                   String tripType, String bookingDate, String flightDate, int flightDuration) {
        this.user = user;
        this.jet = jet;
        this.departure = departure;
        this.destination = destination;
        this.tripType = tripType;
        this.bookingDate = bookingDate;
        this.flightDate = flightDate;
        this.flightDuration = flightDuration;

        // Calculate the total cost based on jet hourly rate and flight duration
        calculateTotalCost();

        // Make the jet unavailable since it's now booked
        jet.setAvailable(false);
    }

    // These "getter" methods allow other parts of the program to access the booking's information
    public User getUser() {
        return user;
    }

    // These "setter" methods allow other parts of the program to change the booking's information
    public void setUser(User user) {
        this.user = user;
    }

    public Jet getJet() {
        return jet;
    }

    public void setJet(Jet jet) {
        this.jet = jet;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
        // Recalculate cost if duration changes
        calculateTotalCost();
    }

    public double getTotalCost() {
        return totalCost;
    }

    // This calculates how much the booking will cost
    private void calculateTotalCost() {
        // Basic calculation: hourly rate × duration
        // For round trips, we double the duration
        double multiplier = tripType.equalsIgnoreCase("Round-trip") ? 2.0 : 1.0;
        this.totalCost = jet.getHourlyRate() * flightDuration * multiplier;
    }

    // This cancels the booking and makes the jet available again
    public void cancelBooking() {
        jet.setAvailable(true);
    }

    // This displays all the booking details
    public void displayBookingDetails() {
        System.out.println("\n----- Booking Details -----");
        System.out.println("User: " + user.getUsername());
        System.out.println("Jet: " + jet.getModel() + " (" + jet.getType() + ")");
        System.out.println("Route: " + departure + " to " + destination);
        System.out.println("Trip Type: " + tripType);
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Flight Date: " + flightDate);
        System.out.println("Flight Duration: " + flightDuration + " hours");
        System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("--------------------------");
    }
}