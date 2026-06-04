package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        User user = new User("testuser", "test@example.com", "1234567890");
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    public void testUserSetters() {
        User user = new User("testuser", "test@example.com", "1234567890");
        user.setUsername("newuser");
        user.setEmail("new@example.com");
        user.setPhoneNumber("0987654321");
        
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("0987654321", user.getPhoneNumber());
    }

    @Test
    public void testToString() {
        User user = new User("testuser", "test@example.com", "1234567890");
        assertEquals("testuser (test@example.com)", user.toString());
    }
}
