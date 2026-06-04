package service;

import model.Booking;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BookingRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class NotificationTest {

    private List<String> sentMessages = new ArrayList<>();
    private NotificationService mockNotificationService = (booking, message) -> {
        String email = (booking.getUser() != null && booking.getUser().getEmail() != null) 
                       ? booking.getUser().getEmail() : "Guest";
        sentMessages.add(email + ": " + message);
    };

    @BeforeEach
    public void setUp() {
        sentMessages.clear();
        BookingService.setNotificationService(mockNotificationService);
        List<Booking> existing = BookingRepository.getBookings();
        for (Booking b : existing) {
            BookingRepository.removeBooking(b);
        }
    }

    @Test
    public void testNewBookingNotification() {
        User user = new User("Alice", "alice@example.com", "pass");
        Booking booking = new Booking(user, "LHR", "Canterbury", 50, 2, new Date(), new Date());
        
        BookingService.saveBooking(booking);
        
        assertTrue("Alice should receive a confirmation message",
            sentMessages.contains("alice@example.com: Booking confirmation: Your booking has been successfully created."));
    }

    @Test
    public void testAffectedUsersNotificationOnNewBooking() {
        User alice = new User("Alice", "alice@example.com", "pass");
        Date date = new Date();
        // Alice has an existing booking
        Booking booking1 = new Booking(alice, "LHR", "Canterbury", 50, 1, date, date);
        BookingRepository.addBooking(booking1);
        
        sentMessages.clear();

        User bob = new User("Bob", "bob@example.com", "pass");
        Booking booking2 = new Booking(bob, "LHR", "Canterbury", 50, 1, date, date);
        BookingService.saveBooking(booking2);

        assertTrue("Alice should be notified that someone joined her trip", sentMessages.contains("alice@example.com: A new passenger has joined your trip. Trip duration may have changed."));
    }

    @Test
    public void testAffectedUsersNotificationOnCancellation() {
        User alice = new User("Alice", "alice@example.com", "pass");
        User bob = new User("Bob", "bob@example.com", "pass");
        Date date = new Date();
        
        Booking booking1 = new Booking(alice, "LHR", "Canterbury", 50, 1, date, date);
        Booking booking2 = new Booking(bob, "LHR", "Canterbury", 50, 1, date, date);
        
        BookingRepository.addBooking(booking1);
        BookingRepository.addBooking(booking2);
        
        sentMessages.clear();

        BookingService.cancelBooking(booking2);

        assertTrue("Alice should be notified that someone left her trip", sentMessages.contains("alice@example.com: A passenger has left your trip. Trip duration may have changed."));
        assertTrue("Bob should receive a cancellation confirmation",
            sentMessages.contains("bob@example.com: Cancellation confirmation: Your booking has been cancelled."));
    }

    @Test
    public void testAffectedUsersNotificationOnAmendment() {
        User alice = new User("Alice", "alice@example.com", "pass");
        User bob = new User("Bob", "bob@example.com", "pass");
        Date date = new Date();
        
        Booking booking1 = new Booking(alice, "LHR", "Canterbury", 50, 1, date, date);
        Booking booking2 = new Booking(bob, "LHR", "Canterbury", 50, 1, date, date);
        
        BookingRepository.addBooking(booking1);
        BookingRepository.addBooking(booking2);
        
        sentMessages.clear();

        Booking updatedBob = new Booking(bob, "LHR", "Canterbury", 50, 2, date, date);
        BookingService.updateBooking(booking2, updatedBob);

        assertTrue("Alice should be notified of the amendment on her trip",
            sentMessages.contains("alice@example.com: The trip schedule or duration has changed due to an amendment by another passenger."));
        assertTrue("Bob should receive an amendment confirmation",
            sentMessages.contains("bob@example.com: Amendment confirmation: Your booking has been successfully updated."));
    }

    @Test
    public void testNotificationWithDifferentPickupsOnSameTrip() {
        User alice = new User("Alice", "alice@example.com", "pass");
        User bob = new User("Bob", "bob@example.com", "pass");
        Date date = new Date();
        Date time = new Date();
        
        // Alice from Canterbury West, Bob from Canterbury East
        // Both going to London on the same trip
        Booking booking1 = new Booking(alice, "London", "Canterbury West", 50, 1, date, time);
        BookingService.saveBooking(booking1);
        
        sentMessages.clear();

        Booking booking2 = new Booking(bob, "London", "Canterbury East", 50, 1, date, time);
        BookingService.saveBooking(booking2);

        assertTrue("Alice should be notified even though Bob has a different pickup location",
            sentMessages.contains("alice@example.com: A new passenger has joined your trip. Trip duration may have changed."));
    }
}
