package classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

class BookingTest {
    @Test
    void testSettersAndUpdates() {
        Booking booking = new Booking(null, "Old", "Old", 10, 1, 1, new Date(), new Date());

        booking.setDestination("Destination");
        assertEquals("Destination", booking.getDestination(), "Setter should update destination field");

        booking.setNumberOfPassengers(8);
        assertEquals(8, booking.getNumberOfPassengers(), "Setter should update passenger count");
    }

    @Test
    void testTripAssignment() {
        Booking booking = new Booking(null, "LHR", "Kent", 10, 1, 1, new Date(), new Date());
        Trip trip = new Trip("T1", "LHR");

        booking.setTrip(trip);

        assertNotNull(booking.getTrip(), "Booking should hold a trip");
        assertEquals("T1", booking.getTrip().getId(), "Assigned trip ID should match");
    }
}

