package ui;

import model.Booking;
import repository.BookingRepository;

// --- PACKAGES LOADED FOR EXTENDED WORKFLOW HANDLERS ---
import service.BookingService;
// -----------------------------------------------------------

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

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
        card.setMaximumSize(new Dimension(450, 180));
        card.setPreferredSize(new Dimension(450, 180));

        JLabel destinationLabel = new JLabel(booking.getDestination());
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel pickupLabel = new JLabel("Pickup: " + booking.getPickupLocation());
        JLabel passengersLabel = new JLabel("Passengers: " + booking.getNumberOfPassengers());

        JLabel userLabel = new JLabel("User: " + booking.getUser().getUsername());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        JLabel dateLabel = new JLabel(
                "Date: " + dateFormat.format(booking.getDate())
        );
        JLabel timeLabel = new JLabel(
                "Time: " + timeFormat.format(booking.getTime())
        );

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JButton amendButton = new JButton("Amend");
        JButton cancelButton = new JButton("Cancel");

        infoPanel.add(userLabel);
        infoPanel.add(destinationLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(pickupLabel);
        infoPanel.add(passengersLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(timeLabel);

        infoPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.add(amendButton);
        buttonPanel.add(cancelButton);
        infoPanel.add(buttonPanel);

        // --- STEP 1: LAUNCH THE RESTRICTED DATE/TIME BOOKING FORM IMMEDIATELY FIRST ---
        amendButton.addActionListener(e -> {
            PaymentUI.openQuickAmendDialog(this, booking);
        });

        // --- STEP 2: CANCEL MECHANISM ENFORCING DISTINCT CANCELLATION FEE LOGIC ---
        cancelButton.addActionListener(e -> {

            Date currentDate = new Date();

            long difference =
                    booking.getDate().getTime()
                            - currentDate.getTime();

            long hoursUntilBooking =
                    difference / (1000 * 60 * 60);

            if (hoursUntilBooking < 24) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bookings cannot be cancelled within 24 hours."
                );

                return;
            }


            double cancellationFee = BookingService.calculateCancellationFee(booking);

            String msg = String.format("""
                                       Are you sure you'd like to cancel this booking?
                                       A penalty cancellation fee of \u00a3%.2f will be applied.""",
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
                BookingService.cancelBooking(booking);
                bookingsPanel.remove(card);
                bookingsPanel.revalidate();
                bookingsPanel.repaint();
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
            }
        });

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(amendButton);
        buttonPanel.add(cancelButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }
}
