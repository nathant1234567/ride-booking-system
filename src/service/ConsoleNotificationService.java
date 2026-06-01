package service;

import model.Booking;

/**
 * Implementation of NotificationService that prints messages to the console.
 */
public class ConsoleNotificationService implements NotificationService {
    @Override
    public void sendNotification(Booking booking, String message) {
        String email = (booking.getUser() != null && booking.getUser().getEmail() != null) 
                       ? booking.getUser().getEmail() : "Guest";
        System.out.println(">>> [NOTIFICATION to " + email + "]: " + message);
        System.out.println("    Details: " + booking);
    }
}
