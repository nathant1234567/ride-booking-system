package model;

// --- REMOVE JUPITER PACKAGES AND ADD JUNIT 4 IMPORTS ---
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
// ------------------------------------------------------

import java.util.Date;

public class TripTest { // Class must be public in JUnit 4

    @Test
    public void testTripCreation() { // Must be public void
        Date now = new Date();
        Trip trip = new Trip("trip-123", "Central London", now, now, "Standard");

        // Fixed for JUnit 4: The custom error message string goes FIRST
        assertEquals("trip-123", trip.getId(), "ID should match constructor input");
        assertEquals("Central London", trip.getDestination(), "Destination should match");
        assertEquals("Standard", trip.getVehicleType(), "Vehicle type should match");
        assertTrue(trip.getBookings().isEmpty(), "New trip should have no bookings initially");
    }

    @Test
    public void testAddAndRemoveBooking() { // Must be public void
        Date now = new Date();
        Trip trip = new Trip("trip-123", "Central London", now, now, "Standard");
        User user = new User("Test", "test@test.com", "123");
        Booking booking = new Booking(user, "Central London", "Pickup", 10, 1, 1, new Date(), new Date());

        trip.addBooking(booking);

        assertEquals(1, trip.getBookings().size(), "Trip should contain 1 booking");
        assertTrue(trip.getBookings().contains(booking), "Trip contains the specific booking object");

        trip.removeBooking(booking);

        assertTrue(trip.getBookings().isEmpty(), "Trip should be empty after removal");
    }
}
