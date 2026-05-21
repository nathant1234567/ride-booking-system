package ui;

import classes.Booking;
import repository.BookingRepository;

// --- PACKAGES LOADED FOR EXTENDED WORKFLOW HANDLERS ---
import classes.PriceBreakdown;
import classes.Payment;
import service.BookingService;
// -----------------------------------------------------------

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ManageBookingsUI extends JPanel {

    private JPanel bookingsPanel;

    // --- TRACK STATIC SYSTEM INSTANCE ---
    private static ManageBookingsUI activeInstance;

    // --- ACCESSIBILITY HELPER FOR REFRESHES ---
    public static ManageBookingsUI getInstance() {
        return activeInstance;
    }

    public void loadBookings() {
        bookingsPanel.removeAll();

        List<Booking> bookings = BookingRepository.getBookings();

        for (Booking booking : bookings) {
            JPanel bookingCard = createBookingCard(booking);
            bookingsPanel.add(bookingCard);
        }

        bookingsPanel.revalidate();
        bookingsPanel.repaint();
    }

    public ManageBookingsUI() {
        // --- CAPTURE RUNTIME CONTEXT PULL ---
        activeInstance = this;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);

        bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));

        List<Booking> bookings = BookingRepository.getBookings();

        for (Booking booking : bookings) {
            JPanel bookingCard = createBookingCard(booking);
            bookingsPanel.add(bookingCard);
        }

        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBookingCard(Booking booking) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(500, 120));

        JLabel destinationLabel = new JLabel(booking.getDestination());
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel pickupLabel = new JLabel("Pickup: " + booking.getPickupLocation());
        JLabel passengersLabel = new JLabel("Passengers: " + booking.getNumberOfPassengers());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(destinationLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(pickupLabel);
        infoPanel.add(passengersLabel);

        JButton amendButton = new JButton("Amend");
        JButton cancelButton = new JButton("Cancel");

        // --- STEP 1: LAUNCH THE RESTRICTED DATE/TIME BOOKING FORM IMMEDIATELY FIRST ---
        amendButton.addActionListener(e -> {
            PaymentUI.openQuickAmendDialog(this, booking);
        });

        // --- STEP 2: CANCEL MECHANISM ENFORCING DISTINCT CANCELLATION FEE LOGIC ---
        cancelButton.addActionListener(e -> {
            // Calculate specific cancellation fee using your backend business rule
            double cancellationFee = BookingService.calculateCancellationFee(booking);

            String msg = String.format(
                    "Are you sure you'd like to cancel this booking?\n" +
                            "A penalty cancellation fee of £%.2f will be applied.",
                    cancellationFee
            );

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    msg,
                    "Cancel Booking",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                BookingRepository.removeBooking(booking);
                bookingsPanel.remove(card);
                bookingsPanel.revalidate();
                bookingsPanel.repaint();
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(amendButton);
        buttonPanel.add(cancelButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }
}
