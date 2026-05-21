import ui.MainUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI frame = new MainUI();
            frame.setVisible(true);
        });
    }
}
