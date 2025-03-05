import java.util.*;

// This class manages all jet bookings in the system
class BookingManager {
    // A list that holds all bookings
    private List<Booking> bookings;

    // This is a constructor - it creates a new empty booking manager
    public BookingManager() {
        this.bookings = new ArrayList<>();
    }

    // This creates a new booking in the system
    public Booking createBooking(User user, Jet jet, String departure, String destination,
                                 String tripType, String bookingDate, String flightDate, int flightDuration) {

        // Only create booking if the jet is available
        if (jet.isAvailable()) {
            Booking newBooking = new Booking(user, jet, departure, destination,
                    tripType, bookingDate, flightDate, flightDuration);

            // Add the booking to both the global list and user's personal list
            bookings.add(newBooking);
            user.addBooking(newBooking);

            return newBooking;
        }

        return null; // Booking failed because jet is not available
    }

    // This cancels an existing booking
    public boolean cancelBooking(Booking booking) {
        if (bookings.contains(booking)) {
            booking.cancelBooking(); // Make the jet available again
            booking.getUser().cancelBooking(booking); // Remove from user's bookings
            return bookings.remove(booking); // Remove from global list
        }
        return false;
    }

    // This finds all bookings for a specific user
    public List<Booking> findBookingsByUser(User user) {
        List<Booking> userBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getUser().equals(user)) {
                userBookings.add(booking);
            }
        }

        return userBookings;
    }

    // This finds all bookings for a specific jet
    public List<Booking> findBookingsByJet(Jet jet) {
        List<Booking> jetBookings = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getJet().equals(jet)) {
                jetBookings.add(booking);
            }
        }

        return jetBookings;
    }

    // This counts how many bookings exist in the system
    public int getBookingCount() {
        return bookings.size();
    }

    // This displays all bookings in the system
    public void displayAllBookings() {
        System.out.println("\n----- All Bookings -----");
        for (Booking booking : bookings) {
            System.out.println("User: " + booking.getUser().getUsername() +
                    ", Jet: " + booking.getJet().getModel() +
                    ", From: " + booking.getDeparture() +
                    ", To: " + booking.getDestination() +
                    ", Date: " + booking.getFlightDate());
        }
        System.out.println("-----------------------");
    }
}
