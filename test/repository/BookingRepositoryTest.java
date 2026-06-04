package repository;

import model.Booking;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class BookingRepositoryTest {

    @Before
    public void setUp() {
        BookingRepository.clear();
    }

    @Test
    public void testAddBooking() {
        User user = new User("Test User", "test@example.com", "pass");
        Booking booking = new Booking(user, "Dest", "Pick", 10, 2, new Date(), new Date());
        
        BookingRepository.addBooking(booking);
        
        List<Booking> bookings = BookingRepository.getBookings();
        assertEquals(1, bookings.size());
        assertEquals(booking, bookings.get(0));
    }

    @Test
    public void testRemoveBooking() {
        User user = new User("Test User", "test@example.com", "pass");
        Booking booking = new Booking(user, "Dest", "Pick", 10, 2, new Date(), new Date());
        BookingRepository.addBooking(booking);
        
        BookingRepository.removeBooking(booking);
        
        assertTrue(BookingRepository.getBookings().isEmpty());
    }

    @Test
    public void testUpdateBookingSuccess() {
        User user = new User("Test User", "test@example.com", "pass");
        Booking original = new Booking(user, "Dest 1", "Pick 1", 10, 2, new Date(), new Date());
        Booking updated = new Booking(user, "Dest 2", "Pick 2", 20, 3, new Date(), new Date());
        
        BookingRepository.addBooking(original);
        boolean result = BookingRepository.updateBooking(original, updated);
        
        assertTrue(result);
        List<Booking> bookings = BookingRepository.getBookings();
        assertEquals(1, bookings.size());
        assertEquals("Dest 2", bookings.get(0).getDestination());
    }

    @Test
    public void testUpdateBookingFailure() {
        User user = new User("Test User", "test@example.com", "pass");
        Booking original = new Booking(user, "Dest 1", "Pick 1", 10, 2, new Date(), new Date());
        Booking updated = new Booking(user, "Dest 2", "Pick 2", 20, 3, new Date(), new Date());
        
        // Don't add original to repo
        boolean result = BookingRepository.updateBooking(original, updated);
        
        assertFalse(result);
        assertTrue(BookingRepository.getBookings().isEmpty());
    }

    @Test
    public void testGetBookingsReturnsCopy() {
        User user = new User("Test User", "test@example.com", "pass");
        Booking booking = new Booking(user, "Dest", "Pick", 10, 2, new Date(), new Date());
        BookingRepository.addBooking(booking);
        
        List<Booking> bookings1 = BookingRepository.getBookings();
        List<Booking> bookings2 = BookingRepository.getBookings();
        
        assertNotSame(bookings1, bookings2);
        assertEquals(bookings1, bookings2);
    }
}
