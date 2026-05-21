package service;

import classes.Booking;
import classes.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BookingRepository;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingServiceTest {

    private User testUser;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        testUser = new User("Nathan", "njt38@kent.ac.uk", "1234567890");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        futureDate = cal.getTime();
    }

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

    @Test
    void testDurationCalculationLogic() {
        return;
    }

    @Test
    void test24HourCancellationRule() {
        return;
    }
}
