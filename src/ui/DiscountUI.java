package ui;

import logic.DiscountCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiscountUI {
    public static void main(String[] args) {
        // Initialise the window frame
        JFrame frame = new JFrame("Discount Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        // Create UI components
        JLabel priceLabel = new JLabel(" Base Price (£):");
        JTextField priceField = new JTextField();

        JLabel timeLabel = new JLabel(" Time of Day:");

        // Generate an array of hours (00 to 23) for the drop-down
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }
        JComboBox<String> timeDropdown = new JComboBox<>(hours);

        JButton calcButton = new JButton("Calculate");
        JLabel resultLabel = new JLabel(" Final Price: ");

        // Add components to the frame
        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(timeLabel);
        frame.add(timeDropdown);
        frame.add(new JLabel("")); // Empty spacer
        frame.add(calcButton);
        frame.add(new JLabel("")); // Empty spacer
        frame.add(resultLabel);

        // Add logic to the button
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    // Fetch the selected hour from the drop-down
                    int time = Integer.parseInt((String) timeDropdown.getSelectedItem());

                    double finalPrice = DiscountCalculator.calculateDiscount(price, time);
                    resultLabel.setText(" Final Price: £" + String.format("%.2f", finalPrice));
                } catch (NumberFormatException ex) {
                    resultLabel.setText(" Error: Enter valid base price");
                }
            }
        });

        // Display the window
        frame.setVisible(true);
    }
}

//add apply button to the ui after the user calculates then clicks apply then there
// then the price of their current booking changes to the discounted price

//or

// when the user clicks and selects different prices from the drop it


//there should be a button on the payment ui where users calculate the amount they get off and then
// clicks 'apply discount' and then the original amount changes to the discounted price