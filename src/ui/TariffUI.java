package ui;


import logic.TariffCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TariffUI {


    // Built this launch method so the estimator window could be opened independently
    // of the main dashboard, letting users browse tariffs without starting a real booking.
    public static void launch() {
        JFrame frame = new JFrame("Browse Services and Tariffs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new GridLayout(7, 2, 10, 10));


        JLabel vehicleLabel = new JLabel(" Vehicle Type:");
        String[] vehicles = {"Standard", "Executive", "Minibus"};
        JComboBox<String> vehicleDropdown = new JComboBox<>(vehicles);


        JLabel luggageLabel = new JLabel(" Luggage Amount:");
        SpinnerModel luggageModel = new SpinnerNumberModel(0, 0, 10, 1);
        JSpinner luggageSpinner = new JSpinner(luggageModel);


        JLabel distanceLabel = new JLabel(" Distance to Airport (miles):");
        JTextField distanceField = new JTextField("10.0");


        JLabel dayLabel = new JLabel(" Day of the Week:");
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        JComboBox<String> dayDropdown = new JComboBox<>(days);


        JLabel timeLabel = new JLabel(" Time of Day (Hour):");
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }
        JComboBox<String> timeDropdown = new JComboBox<>(hours);


        JButton calcButton = new JButton("Get Estimate");
        JLabel resultLabel = new JLabel(" Estimated Cost: ");


        frame.add(vehicleLabel);
        frame.add(vehicleDropdown);
        frame.add(luggageLabel);
        frame.add(luggageSpinner);
        frame.add(distanceLabel);
        frame.add(distanceField);
        frame.add(dayLabel);
        frame.add(dayDropdown);
        frame.add(timeLabel);
        frame.add(timeDropdown);
        frame.add(new JLabel("")); // Spacer
        frame.add(calcButton);
        frame.add(new JLabel("")); // Spacer
        frame.add(resultLabel);


        // Added an action listener to grab the inputs and pass them to the TariffCalculator.
        // Also wrapped the distance parsing in a try-catch block as a precaution so the
        // app throws a clean error instead of crashing if a user types text instead of a number.

        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String vehicle = (String) vehicleDropdown.getSelectedItem();
                    int luggage = (Integer) luggageSpinner.getValue();
                    double distance = Double.parseDouble(distanceField.getText());
                    String day = (String) dayDropdown.getSelectedItem();
                    int time = Integer.parseInt((String) timeDropdown.getSelectedItem());


                    double estimate = TariffCalculator.calculateEstimate(vehicle, luggage, distance, day, time);


                    resultLabel.setText(" Estimated Cost: £" + String.format("%.2f", estimate));
                } catch (NumberFormatException ex) {
                    resultLabel.setText(" Error: Invalid distance format");
                }
            }
        });


        frame.setVisible(true);




    }


    public static void main(String[] args) {
        launch();
    }
}

