package service;

import model.Booking;
import model.PriceBreakdown;
import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;



public class BookingServiceTest {

    @Test
    public void testSaveBooking() {
        User user = new User("Nathan", "test@test.com", "password123");
        Booking booking = new Booking(user, "London Heathrow (LHR)", "Canterbury", 50, 2, new Date(), new Date());

        BookingService.saveBooking(booking);

        assertTrue(repository.BookingRepository.getBookings().contains(booking));
    }

    @Test
    public void testCalculateAmendmentFee() {
        Booking booking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 50, 1, 0, new Date(), new Date());
        double fee = BookingService.calculateAmendmentFee(booking);

        assertEquals(2.50, fee, 0.001);
    }

    @Test
    public void testCalculateCancellationFee() {
        Booking booking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 10, 1, 0, new Date(), new Date());
        double fee = BookingService.calculateCancellationFee(booking);

        assertEquals(6.70, fee, 0.001);
    }

    @Test
    public void testCheckTripAccommodationFailsOnOverload() {
        Booking heavyBooking = new Booking(null, "London Heathrow (LHR)", "Canterbury", 20, 9, 0, new Date(), new Date());
        boolean canAccommodate = BookingService.checkTripAccommodation(heavyBooking, new Date(), new Date());

        assertFalse(canAccommodate);
    }

    @Test
    public void testCalculatePrice() {
        Booking booking = new Booking(null, "Dest", "Pick", 10, 2, 1, new Date(), new Date());
        double price = BookingService.calculatePrice(booking);
        assertEquals(20.5, price, 0.001);
    }

    @Test
    public void testCalculatePriceMinimumFare() {
        // Very short booking
        Booking booking = new Booking(null, "Dest", "Pick", 1, 1, 0, new Date(), new Date());
        double price = BookingService.calculatePrice(booking);
        assertEquals(5.0, price, 0.001);
    }

    @Test
    public void testCalculatePriceWithDiscountLoyalty() {
        User kentUser = new User("Kent", "user@kent.ac.uk", "123");
        // Time 12:00 (Off-peak)
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.OCTOBER, 23, 12, 0); // Monday
        Date date = cal.getTime();
        
        Booking booking = new Booking(kentUser, "London", "Canterbury", 100, 1, 0, date, date);

        PriceBreakdown pb = BookingService.calculatePriceWithDiscount(booking, null);
        assertTrue(pb.getDiscountDescription().contains("Loyalty 5%"));
        assertEquals(0.05, pb.getDiscountPercent(), 0.001);
        assertEquals(pb.getSubtotal() * 0.05, pb.getDiscountAmount(), 0.01);
    }

    @Test
    public void testCalculatePriceWithDiscountGroupAndPromo() {
        User user = new User("Normal", "user@example.com", "123");
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.OCTOBER, 23, 12, 0);
        Date date = cal.getTime();

        Booking booking = new Booking(user, "London", "Canterbury", 100, 5, 0, date, date);
        PriceBreakdown pb = BookingService.calculatePriceWithDiscount(booking, "SAVE10");
        
        assertEquals(0.20, pb.getDiscountPercent(), 0.001);
        assertTrue(pb.getDiscountDescription().contains("Group 10%"));
        assertTrue(pb.getDiscountDescription().contains("Promo SAVE10 10%"));
    }

    @Test
    public void testBookingLengthCalculator() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 12); // No traffic multiplier (1.0)
        Date time = cal.getTime();
        
        Booking booking = new Booking(null, "London", "Canterbury", 50, 2, 1, new Date(), time);

        int duration = BookingService.bookingLengthCalculator(booking, 1);
        assertEquals(69, duration);
    }

    @Test
    public void testBookingLengthCalculatorRushHour() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8); // Rush hour (1.5)
        Date time = cal.getTime();

        Booking booking = new Booking(null, "London", "Canterbury", 50, 2, 0, new Date(), time);

        int duration = BookingService.bookingLengthCalculator(booking, 0);
        assertEquals(97, duration);
    }
}
