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

    public AmendBookingUI() {
        setLayout(new BorderLayout(10,10));

        bookingCombo = new JComboBox<>(BookingRepository.getBookings().toArray(new Booking[0]));
        add(bookingCombo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        JTextField pickup = new JTextField();
        JTextField destination = new JTextField();
        JTextField length = new JTextField();
        JSpinner passengers = new JSpinner(new SpinnerNumberModel(1,1,8,1));
        JSpinner luggage = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY));

        form.add(new JLabel("Pickup:")); form.add(pickup);
        form.add(new JLabel("Destination:")); form.add(destination);
        form.add(new JLabel("Length (km):")); form.add(length);
        form.add(new JLabel("Passengers:")); form.add(passengers);
        form.add(new JLabel("Luggage:")); form.add(luggage);
        form.add(new JLabel("Date:")); form.add(dateSpinner);
        form.add(new JLabel("Time:")); form.add(timeSpinner);

        add(form, BorderLayout.CENTER);

        JButton loadBtn = new JButton("Load Booking");
        JButton saveBtn = new JButton("Save Changes");
        JPanel south = new JPanel();
        south.add(loadBtn);
        south.add(saveBtn);
        add(south, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            Booking b = (Booking) bookingCombo.getSelectedItem();
            if (b == null) return;
            pickup.setText(b.getPickupLocation());
            destination.setText(b.getDestination());
            length.setText(String.valueOf(b.getLengthEstimate()));
            passengers.setValue(b.getNumberOfPassengers());
            luggage.setValue(b.getNumberOfLuggage());
            dateSpinner.setValue(b.getDate());
            timeSpinner.setValue(b.getTime());
        });

        saveBtn.addActionListener(e -> {
            Booking original = (Booking) bookingCombo.getSelectedItem();
            if (original == null) return;

            try {
                int len = Integer.parseInt(length.getText().trim());
                Booking updated = new Booking(original.getUser(), destination.getText().trim(), pickup.getText().trim(), len,
                        (int) passengers.getValue(), (int) luggage.getValue(), (Date) dateSpinner.getValue(), (Date) timeSpinner.getValue());

                boolean ok = BookingRepository.updateBooking(original, updated);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Booking updated");
                    // refresh combo
                    bookingCombo.removeAllItems();
                    for (Booking b : BookingRepository.getBookings()) bookingCombo.addItem(b);
                    // show new estimated price
                    PriceBreakdown pb = BookingService.calculatePriceWithDiscount(updated, null);
                    JOptionPane.showMessageDialog(this, String.format("New estimated price: £%.2f", pb.getFinalTotal()));
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update booking", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid length value", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

