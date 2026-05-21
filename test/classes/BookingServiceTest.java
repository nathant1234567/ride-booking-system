package service;

import classes.Booking;
import classes.User;
import org.junit.jupiter.api.Test;
import repository.BookingRepository;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingServiceTest {

    @Test
    void testSaveBooking() {

        User user = new User("Nathan", "test@test.com", "password123");

        Booking booking =
                new Booking(
                        user,
                        "London Heathrow (LHR)",
                        "Canterbury",
                        50,
                        2,
                        new Date(),
                        new Date()
                );

        BookingService.saveBooking(booking);

        assertTrue(
                BookingRepository.getBookings().contains(booking)
        );
    }
}

