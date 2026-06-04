package model;

// --- REMOVE JUPITER PACKAGES AND ADD JUNIT 4 IMPORTS ---
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals(user, booking.getUser(), "User should match");
        assertEquals(destination, booking.getDestination(), "Destination should match");
        assertEquals(pickup, booking.getPickupLocation(), "Pickup location should match");
        assertEquals(passengers, booking.getNumberOfPassengers(), "Number of passengers should match");
        assertEquals(date, booking.getDate(), "Date should match");
        assertEquals(time, booking.getTime(), "Time should match");
    }

    @Test
    public void testSetters() { // Must be public void
        User user = new User("John Doe", "john@example.com", "987654321");
        Booking booking = new Booking(user, "Old Dest", "Old Pickup", 56, 1, 5, new Date(), new Date());

        String newDest = "Central London";

        booking.setDestination(newDest);

        // Fixed for JUnit 4: The custom error message string goes FIRST
        assertEquals(newDest, booking.getDestination(),
                "Destination should be updated via setter");
    }
}
