import java.util.*;

class User {
    // These variables store information about each user
    private String username;       // The user's login name
    private String email;          // The user's email address for notifications
    private String phoneNumber;    // The user's contact number
    private List<String> pastTrips; // A record of trips this user has taken before
    private List<Booking> bookings; // A list of current jet bookings

    // This is a constructor - it creates a new User with their basic information
    public User(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        // Create empty lists to store the user's trips and bookings
        this.pastTrips = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    // These "getter" methods allow other parts of the program to access the user's information
    public String getUsername() {
        return username;
    }

    // These "setter" methods allow other parts of the program to change the user's information
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getPastTrips() {
        return pastTrips;
    }

    // This adds a completed trip to the user's history
    public void addPastTrip(String trip) {
        this.pastTrips.add(trip);
    }

    // This adds a new booking to the user's current bookings
    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    // This gets all of the user's current bookings
    public List<Booking> getBookings() {
        return bookings;
    }

    // This cancels a booking by removing it from the list
    public boolean cancelBooking(Booking booking) {
        return this.bookings.remove(booking);
    }

    // This displays the user's profile information
    public void displayProfile() {
        System.out.println("User Profile:");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Past Trips: " + pastTrips.size());
        System.out.println("Current Bookings: " + bookings.size());
    }
}
