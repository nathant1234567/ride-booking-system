package service;

import model.Booking;

/**
 * Interface for sending confirmation and update messages.
 */
public interface NotificationService {
    void sendNotification(Booking booking, String message);
}
