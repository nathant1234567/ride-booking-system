package service;

import classes.Booking;
import repository.BookingRepository;

import java.util.Calendar;
import java.util.Date;

public class BookingService {
    /**
     * Class to handle the logic to create bookings.
     * It includes booking creation, cancellation and trip management functions. 
     * @param booking
     */

    public static void saveBooking(Booking booking) {
        BookingRepository.addBooking(booking);
    }

    public static int bookingLengthCalculator(Booking booking, int luggageAmount) {
        double distance = booking.getLengthEstimate();
        double averageSpeed = 50.0;

        double duration = (distance / averageSpeed) * 60;

        double trafficMultiplier = getTrafficMultiplier(booking.getTime());
        duration *= trafficMultiplier;

        duration += (luggageAmount * 2);

        duration += 5;
        duration += booking.getNumberOfPassengers();

        return (int) Math.round(duration);
    }

    private static double getTrafficMultiplier(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 7 && hour < 10) {
            return 1.5;
        }
        else if (hour >= 16 && hour < 19) {
            return 1.5;
        }
        else if (hour >= 23 || hour < 5) {
            return 0.8;
        }
        else {
            return 1.0;
        }
    }
}

