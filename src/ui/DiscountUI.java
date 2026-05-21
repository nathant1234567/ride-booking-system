package ui;

import logic.DiscountCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiscountUI {
    public static void launch(JTextField paymentAmountField, JButton sourceButton) {
        JFrame frame = new JFrame("Discount Calculator");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel priceLabel = new JLabel(" Base Price (£):");
        JTextField priceField = new JTextField(paymentAmountField.getText());
        priceField.setEditable(false);


        JLabel timeLabel = new JLabel(" Time of Day:");

        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }
        JComboBox<String> timeDropdown = new JComboBox<>(hours);

        JButton calcButton = new JButton("Calculate");
        JLabel resultLabel = new JLabel(" Final Price: ");

        JButton applyButton = new JButton("Apply to Payment");
        applyButton.setEnabled(false);
        final double[] savedPrice = {0.0};


        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(timeLabel);
        frame.add(timeDropdown);
        frame.add(new JLabel(""));
        frame.add(calcButton);
        frame.add(new JLabel(""));
        frame.add(resultLabel);

        frame.add(new JLabel(""));
        frame.add(applyButton);


        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    int time = Integer.parseInt((String) timeDropdown.getSelectedItem());

                    savedPrice[0] = DiscountCalculator.calculateDiscount(price, time);
                    resultLabel.setText(" Final Price: £" + String.format("%.2f", savedPrice[0]));

                    applyButton.setEnabled(true);
                } catch (NumberFormatException ex) {
                    resultLabel.setText(" Error: Invalid price format");
                }
            }
        });

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentAmountField.setText(String.format("%.2f", savedPrice[0]));

                sourceButton.setEnabled(false);

                frame.dispose();
            }
        });



        frame.setVisible(true);
    }
}

//add apply button to the ui after the user calculates then clicks apply then there
// then the price of their current booking changes to the discounted price

//or

// when the user clicks and selects different prices from the drop it


//there should be a button on the payment ui where users calculate the amount they get off and then
// clicks 'apply discount' and then the original amount changes to the discounted price