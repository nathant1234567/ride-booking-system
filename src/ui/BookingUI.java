package ui;

import classes.Booking;
import classes.Payment;
import classes.User;
import service.BookingService;
import classes.PriceBreakdown;
import repository.UserRepository;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class BookingUI extends JPanel {
    public BookingUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "New Booking", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16)));

        // User
        inputPanel.add(new JLabel("Select User:"));
        JComboBox<User> userComboBox = new JComboBox<>(UserRepository.getUsers().toArray(new User[0]));
        inputPanel.add(userComboBox);

        // Destination
        inputPanel.add(new JLabel("Select Destination:"));
        String[] destinations = {"London Heathrow (LHR)", "London Gatwick (LGW)", "London Stansted (STN)", "London Luton (LTN)", "London City (LCY)", "Central London", "Canary Wharf"};
        JComboBox<String> destinationComboBox = new JComboBox<>(destinations);
        inputPanel.add(destinationComboBox);

        // Pickup
        inputPanel.add(new JLabel("Pickup Location:"));
        JTextField pickupField = new JTextField();
        inputPanel.add(pickupField);

        // Length in km
        inputPanel.add(new JLabel("Estimated length of journey (km):"));
        JTextField lengthField = new JTextField();
        inputPanel.add(lengthField);

        // Passengers
        inputPanel.add(new JLabel("Number of Passengers:"));
        SpinnerNumberModel passengerModel = new SpinnerNumberModel(1, 1, 8, 1);
        JSpinner passengerSpinner = new JSpinner(passengerModel);
        inputPanel.add(passengerSpinner);

        // Luggage
        inputPanel.add(new JLabel("Number of Luggage Items:"));
        SpinnerNumberModel luggageModel = new SpinnerNumberModel(0, 0, 10, 1);
        JSpinner luggageSpinner = new JSpinner(luggageModel);
        inputPanel.add(luggageSpinner);

        // Date
        inputPanel.add(new JLabel("Date:"));
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        inputPanel.add(dateSpinner);

        // Time
        inputPanel.add(new JLabel("Time:"));
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        inputPanel.add(timeSpinner);


        inputPanel.add(new JLabel("Duration of journey estimate:"));

        JLabel durationResultLabel = new JLabel("[Input all fields!]");
        durationResultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        durationResultLabel.setForeground(new Color(70, 130, 180));
        inputPanel.add(durationResultLabel);

        // Continuously checks if something has been changed in the required boxes
        Runnable updateDuration = () -> {
            try {
                String lengthText = lengthField.getText().trim();
                if (lengthText.isEmpty()) {
                    durationResultLabel.setText("[Enter distance!]");
                    return;
                }
                int length = Integer.parseInt(lengthText);
                int passengers = (int) passengerSpinner.getValue();
                int luggage = (int) luggageSpinner.getValue();
                Date time = (Date) timeSpinner.getValue();

                Booking tempBooking = new Booking(null, null, null, length, passengers, luggage, null, time);
                int duration = BookingService.bookingLengthCalculator(tempBooking, luggage);
                durationResultLabel.setText(duration + " minutes");
            } catch (NumberFormatException e) {
                durationResultLabel.setText("[Invalid distance]");
            }
        };

        lengthField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateDuration.run(); }
            public void removeUpdate(DocumentEvent e) { updateDuration.run(); }
            public void insertUpdate(DocumentEvent e) { updateDuration.run(); }
        });
        passengerSpinner.addChangeListener(e -> updateDuration.run());
        luggageSpinner.addChangeListener(e -> updateDuration.run());
        timeSpinner.addChangeListener(e -> updateDuration.run());


        // Moves on to payment
        JButton saveButton = new JButton("Save Booking");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));

        saveButton.addActionListener(event -> {
            User user = (User) userComboBox.getSelectedItem();
            String destination = (String) destinationComboBox.getSelectedItem();
            String pickupLocation = pickupField.getText();
            int passengers = (int) passengerSpinner.getValue();
            int luggage = (int) luggageSpinner.getValue();
            Date date = (Date) dateSpinner.getValue();
            Date time = (Date) timeSpinner.getValue();

            if (pickupLocation.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a pickup location.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int lengthEstimate;
            try {
                lengthEstimate = Integer.parseInt(lengthField.getText().trim());
                if (lengthEstimate <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid journey length.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid journey length.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Booking booking = new Booking(user, destination, pickupLocation, lengthEstimate, passengers, luggage, date, time);
            BookingService.saveBooking(booking);
            // calculate price (with potential default discounts) and open payment screen with the breakdown
            PriceBreakdown breakdown = BookingService.calculatePriceWithDiscount(booking, null);
            JOptionPane.showMessageDialog(this, String.format("Estimated price: £%.2f - Loading payment screen...", breakdown.getFinalTotal()));

            Payment payment = new Payment();
            PaymentUI paymentUI = new PaymentUI(payment, breakdown, booking);
            paymentUI.setVisible(true);
        });

        add(inputPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }
}
