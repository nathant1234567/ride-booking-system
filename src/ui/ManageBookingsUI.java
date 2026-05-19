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

        setLayout(new BorderLayout());

        JLabel titleLabel =
                new JLabel("Manage Bookings");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        add(titleLabel, BorderLayout.NORTH);

        bookingsPanel = new JPanel();

        bookingsPanel.setLayout(
                new BoxLayout(
                        bookingsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        List<Booking> bookings =
                BookingRepository.getBookings();

        for (Booking booking : bookings) {

            JPanel bookingCard =
                    createBookingCard(booking);

            bookingsPanel.add(bookingCard);
        }

        JScrollPane scrollPane =
                new JScrollPane(bookingsPanel);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBookingCard(Booking booking) {

        JPanel card = new JPanel();

        card.setLayout(new BorderLayout());

        card.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        card.setMaximumSize(
                new Dimension(500, 120)
        );

        JLabel destinationLabel =
                new JLabel(
                        booking.getDestination()
                );

        destinationLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        JLabel pickupLabel =
                new JLabel(
                        "Pickup: " +
                                booking.getPickupLocation()
                );

        JLabel passengersLabel =
                new JLabel(
                        "Passengers: " +
                                booking.getNumberOfPassengers()
                );

        JPanel infoPanel = new JPanel();

        infoPanel.setLayout(
                new BoxLayout(
                        infoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        infoPanel.add(destinationLabel);

        infoPanel.add(Box.createVerticalStrut(5));

        infoPanel.add(pickupLabel);

        infoPanel.add(passengersLabel);

        JButton amendButton =
                new JButton("Amend");

        JButton cancelButton =
                new JButton("Cancel");

        amendButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Amend booking clicked"
            );
        });

        cancelButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Cancel booking clicked"
            );
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(amendButton);

        buttonPanel.add(cancelButton);

        card.add(infoPanel, BorderLayout.CENTER);

        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }
}
