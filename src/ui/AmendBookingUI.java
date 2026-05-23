package ui;

import classes.Booking;
import classes.PriceBreakdown;
import repository.BookingRepository;
import service.BookingService;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AmendBookingUI extends JPanel {

    private JComboBox<Booking> bookingCombo;

    // Helper method to populate fields automatically
    private void populateFields(JTextField pickup, JTextField destination, JTextField length,
                                JSpinner passengers, JSpinner luggage, JSpinner dateSpinner, JSpinner timeSpinner) {
        Booking b = (Booking) bookingCombo.getSelectedItem();
        if (b == null) return;
        pickup.setText(b.getPickupLocation());
        destination.setText(b.getDestination());
        length.setText(String.valueOf(b.getLengthEstimate()));
        passengers.setValue(b.getNumberOfPassengers());
        luggage.setValue(b.getNumberOfLuggage());
        dateSpinner.setValue(b.getDate());
        timeSpinner.setValue(b.getTime());
    }

    public AmendBookingUI() {
        setLayout(new BorderLayout(10,10));

        bookingCombo = new JComboBox<>(BookingRepository.getBookings().toArray(new Booking[0]));
        add(bookingCombo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));

        JTextField pickup = new JTextField();
        pickup.setEditable(false);
        JTextField destination = new JTextField();
        destination.setEditable(false);
        JTextField length = new JTextField();
        length.setEditable(false);

        JSpinner passengers = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        passengers.setEnabled(false);
        JSpinner luggage = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        luggage.setEnabled(false);

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY));

        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));

        form.add(new JLabel("Pickup:")); form.add(pickup);
        form.add(new JLabel("Destination:")); form.add(destination);
        form.add(new JLabel("Length (km/Duration):")); form.add(length);
        form.add(new JLabel("Passengers:")); form.add(passengers);
        form.add(new JLabel("Luggage:")); form.add(luggage);
        form.add(new JLabel("New Date:")); form.add(dateSpinner);
        form.add(new JLabel("New Time:")); form.add(timeSpinner);

        add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Changes");
        JPanel south = new JPanel();
        south.add(saveBtn);
        add(south, BorderLayout.SOUTH);

        // --- AUTOMATION 1: Populate fields immediately when window opens ---
        populateFields(pickup, destination, length, passengers, luggage, dateSpinner, timeSpinner);

        // --- AUTOMATION 2: Automatically update fields if user changes dropdown item ---
        bookingCombo.addActionListener(e -> {
            populateFields(pickup, destination, length, passengers, luggage, dateSpinner, timeSpinner);
        });

        saveBtn.addActionListener(e -> {
            Booking original = (Booking) bookingCombo.getSelectedItem();
            if (original == null) return;

            try {
                Date chosenDate = (Date) dateSpinner.getValue();
                Date chosenTime = (Date) timeSpinner.getValue();

                Booking bookingClone = new Booking(
                        original.getUser(), original.getDestination(), original.getPickupLocation(),
                        original.getLengthEstimate(), original.getNumberOfPassengers(), original.getNumberOfLuggage(),
                        original.getDate(), original.getTime()
                );

                boolean canAccommodate = BookingService.checkTripAccommodation(bookingClone, chosenDate, chosenTime);
                if (!canAccommodate) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot amend: The selected trip time cannot accommodate additional capacity changes.",
                            "Accommodation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Booking updated = new Booking(
                        original.getUser(),
                        original.getDestination(),
                        original.getPickupLocation(),
                        bookingClone.getLengthEstimate(),
                        original.getNumberOfPassengers(),
                        original.getNumberOfLuggage(),
                        chosenDate,
                        chosenTime
                );

                PriceBreakdown pb = BookingService.calculatePriceWithDiscount(updated, null);
                double amendmentFee = BookingService.calculateAmendmentFee(updated);

                JOptionPane.showMessageDialog(this, String.format(
                        "Booking amendment validated!\n" +
                                "New Base Price: £%.2f\n" +
                                "Amendment Processing Fee: £%.2f\n" +
                                "Redirecting to payment to finalize changes.",
                        pb.getFinalTotal(), amendmentFee
                ));

                Window topWindow = SwingUtilities.getWindowAncestor(this);
                if (topWindow != null) {
                    topWindow.dispose();
                }

                classes.Payment paymentContext = new classes.Payment();
                PaymentUI paymentWindow = new PaymentUI(paymentContext, pb, updated, original);
                paymentWindow.setLocationRelativeTo(this);
                paymentWindow.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error processing changes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
