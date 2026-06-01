package service;

import classes.Booking;

/**
 * Interface for sending confirmation and update messages.
 */
public interface NotificationService {
    void sendNotification(Booking booking, String message);
}
