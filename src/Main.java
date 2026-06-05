import service.BookingService;
import service.WindowNotificationService;
import ui.MainUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookingService.setNotificationService(new WindowNotificationService());
            MainUI frame = new MainUI();
            frame.setVisible(true);
        });
    }
}
