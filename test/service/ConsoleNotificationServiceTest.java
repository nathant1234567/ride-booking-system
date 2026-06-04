package service;

import model.Booking;
import model.User;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ConsoleNotificationServiceTest {

    @Test
    public void testSendNotification() {
        ConsoleNotificationService service = new ConsoleNotificationService();
        User user = new User("Alice", "alice@example.com", "123");
        Booking booking = new Booking(user, "London", "Canterbury", 50, 1, new Date(), new Date());
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            service.sendNotification(booking, "Test message");
            String output = outContent.toString();
            assertTrue(output.contains(">>> [NOTIFICATION to alice@example.com]: Test message"));
            assertTrue(output.contains("Details: alice@example.com to London (Pickup: Canterbury)"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testSendNotificationGuest() {
        ConsoleNotificationService service = new ConsoleNotificationService();
        Booking booking = new Booking(null, "London", "Canterbury", 50, 1, new Date(), new Date());
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        try {
            service.sendNotification(booking, "Test message");
            String output = outContent.toString();
            assertTrue(output.contains(">>> [NOTIFICATION to Guest]: Test message"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
