package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class BookingUI extends JPanel {
    public BookingUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Booking Details", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16)));

        JTextField pickupField = new JTextField("Input something");

        inputPanel.add(new JLabel("Test")); inputPanel.add(pickupField);

        add(inputPanel, BorderLayout.CENTER);
    }
}