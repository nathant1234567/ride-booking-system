package service;

import model.Booking;
import javax.swing.*;
import java.awt.*;

/**
 * Implementation of NotificationService that displays messages in a dedicated Swing window.
 */
public class WindowNotificationService implements NotificationService {
    private final JTextArea textArea;
    private final JFrame frame;

    public WindowNotificationService() {
        frame = new JFrame("Notifications");
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen (will be moved by user)
        // Set location to the right of the screen or something?
        // Let's just center it for now, the user can move it.
        
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void sendNotification(Booking booking, String message) {
        String email = (booking.getUser() != null && booking.getUser().getEmail() != null) 
                       ? booking.getUser().getEmail() : "Guest";
        
        StringBuilder sb = new StringBuilder();
        sb.append(">>> [NOTIFICATION to ").append(email).append("]: ").append(message).append("\n");
        sb.append("    Details: ").append(booking).append("\n");
        sb.append("--------------------------------------------------\n");
        
        String logEntry = sb.toString();
        
        SwingUtilities.invokeLater(() -> {
            textArea.append(logEntry);
            textArea.setCaretPosition(textArea.getDocument().getLength());
            if (!frame.isVisible()) {
                frame.setVisible(true);
            }
            frame.toFront();
        });
    }
}
