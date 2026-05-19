package ui;

import service.BookingService;
import javax.swing.*;

public class ManageBookingsUI extends JPanel {

    private JPanel bookingsPanel;

    public void loadBookings() {

        bookingsPanel.removeAll();

        List<Booking> bookings =
                BookingRepository.getBookings();

        for (Booking booking : bookings) {

            JPanel bookingCard =
                    createBookingCard(booking);

            bookingsPanel.add(bookingCard);
        }

        bookingsPanel.revalidate();

        bookingsPanel.repaint();
    }
    public ManageBookingsUI() {
        this.bookingService = bookingService;
    }
}
