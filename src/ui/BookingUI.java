package ui;

import classes.Booking;
import classes.User;
import logic.SaveBooking;
import repository.UserRepository;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

        inputPanel.add(new JLabel("Estimated length of journey (km):"));
        JTextField length = new JTextField();
        inputPanel.add(length);

        // Passengers
        inputPanel.add(new JLabel("Number of Passengers:"));
        SpinnerNumberModel passengerModel = new SpinnerNumberModel(1, 1, 8, 1);
        JSpinner passengerSpinner = new JSpinner(passengerModel);
        inputPanel.add(passengerSpinner);

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

        inputPanel.add(new JLabel("Length of journey estimate:"));
        inputPanel.add(new JLabel("[Input estimate length]"));


        JButton saveButton = new JButton("Save Booking");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));

        saveButton.addActionListener(event -> {
            User user = (User) userComboBox.getSelectedItem();
            String destination = (String) destinationComboBox.getSelectedItem();
            String pickupLocation = pickupField.getText();
            int lengthEstimate = Integer.parseInt(length.getText());
            int passengers = (int) passengerSpinner.getValue();
            Date date = (Date) dateSpinner.getValue();
            Date time = (Date) timeSpinner.getValue();

            if (pickupLocation.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a pickup location.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Booking booking = new Booking(user, destination, pickupLocation, lengthEstimate, passengers, date, time);
            SaveBooking.save(booking);
            JOptionPane.showMessageDialog(this, "Booking saved successfully!");
        });

        add(inputPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }
}
