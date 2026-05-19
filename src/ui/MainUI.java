package ui;
import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    public MainUI() {
        setTitle("COMP5009 Rideshare app");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        BookingUI bookingUI = new BookingUI();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));



        tabbedPane.addTab("Booking", bookingUI); // do this for every page




        add(tabbedPane, BorderLayout.CENTER);
    }
}
