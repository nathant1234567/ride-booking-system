package repository;

import classes.Booking;
import classes.User;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BookingRepositoryTest {
@Test
void testAddAndGetBookings() {
    User user = new User("User", "test@test.com", "123");
    Booking booking = new Booking(user, "Destination", "Pickup", 10, 1, 1, new Date(), new Date());

    int initialSize = BookingRepository.getBookings().size();

    BookingRepository.addBooking(booking);

    List<Booking> currentBookings = BookingRepository.getBookings();
    assertEquals(initialSize + 1, currentBookings.size(), "Repository size should increase by 1");
    assertTrue(currentBookings.contains(booking), "Repository should contain the new booking");

    BookingRepository.removeBooking(booking);
    assertEquals(initialSize, BookingRepository.getBookings().size(), "Size should return to normal after removal");
    }
}