package model;

// --- REMOVE JUPITER PACKAGES AND ADD JUNIT 4 IMPORTS ---
import org.junit.Test;
import static org.junit.Assert.*;
// ------------------------------------------------------

import java.util.Date;

public class BookingTest { // Class must be public in JUnit 4

    @Test
    public void testBookingConstructorAndGetters() { // Must be public void
        User user = new User("Nathan Thompson", "njt38@kent.ac.uk", "0123456789");
        String destination = "London Heathrow (LHR)";
        String pickup = "Canterbury";
        int lengthEstimate = 100;
        int passengers = 2;
        int amountOfLuggage = 4;
        Date date = new Date();
        Date time = new Date();

        Booking booking = new Booking(user, destination, pickup, lengthEstimate, passengers, amountOfLuggage, date, time);

        // Fixed for JUnit 4: The custom error message string goes FIRST
        assertEquals("User should match", user, booking.getUser());
        assertEquals("Destination should match", destination, booking.getDestination());
        assertEquals("Pickup location should match", pickup, booking.getPickupLocation());
        assertEquals("Number of passengers should match", passengers, booking.getNumberOfPassengers());
        assertEquals("Date should match", date, booking.getDate());
        assertEquals("Time should match", time, booking.getTime());
    }

    @Test
    public void testSetters() { // Must be public void
        User user = new User("John Doe", "john@example.com", "987654321");
        Booking booking = new Booking(user, "Old Dest", "Old Pickup", 56, 1, 5, new Date(), new Date());

        String newDest = "Central London";

        booking.setDestination(newDest);

        // Fixed for JUnit 4: The custom error message string goes FIRST
        assertEquals("Destination should be updated via setter", newDest, booking.getDestination());
    }
}
