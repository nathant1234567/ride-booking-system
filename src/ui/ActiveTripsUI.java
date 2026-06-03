package ui;

import model.Trip;
import repository.TripRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ActiveTripsUI extends JFrame {
    /**
     * Displayed generated trips
     * grg
     */

    private JPanel tripsPanel;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ActiveTripsUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Active Dispatch Trips");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);

        tripsPanel = new JPanel();
        tripsPanel.setLayout(new BoxLayout(tripsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(tripsPanel);
        tripsPanel.add(scrollPane,  BorderLayout.CENTER);
    }

    public void loadTrips() {
        tripsPanel.removeAll();

        List<Trip> trips = TripRepository.getTrips();
        boolean hasActiveTrips = false;

        for (Trip trip : trips) {
            if (trip.getBookings().isEmpty()) continue;
            hasActiveTrips = true;
            tripsPanel.add(createTripCard(trip));
        }
        if (hasActiveTrips) {
            JLabel emptyLabel = new JLabel("No active vehicles dispatche at this time");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(new EmptyBorder(20, 0, 0, 0));
            tripsPanel.add(emptyLabel);
        }
        tripsPanel.revalidate();
        tripsPanel.repaint();
    }

    private JPanel createTripCard(Trip trip) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)
        ));
        card.setMaximumSize(new Dimension(600, 200));
        card.setBackground(Color.WHITE);

        boolean isShared = trip.getBookings().size() > 1;

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel idLabel = new JLabel("ID");
    }
}
