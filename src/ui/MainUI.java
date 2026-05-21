package ui;
import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    /**
     * The main UI of the application. It is the parent of all the tabs which live in seperate files.
     */
    public MainUI() {
        setTitle("COMP5009 Rideshare app");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 460);
        setResizable(false);

        BookingUI bookingUI = new BookingUI();

        ManageBookingsUI manageBookingsUI = new ManageBookingsUI();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));


        tabbedPane.addTab("Booking", bookingUI); // do this for every page

        tabbedPane.addTab("Manage Bookings", manageBookingsUI);

        tabbedPane.addChangeListener(e -> {

            int selectedIndex =
                    tabbedPane.getSelectedIndex();

            String selectedTab =
                    tabbedPane.getTitleAt(selectedIndex);

            if (selectedTab.equals("Manage Bookings")) {

                manageBookingsUI.loadBookings();
            }
        });
        add(tabbedPane, BorderLayout.CENTER);
    }
}
