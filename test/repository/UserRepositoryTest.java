package repository;

import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {

    @Before
    public void setUp() {
        UserRepository.clear();
    }

    @Test
    public void testAddAndGetUsers() {
        User user = new User("Nathan", "njt38@kent.ac.uk", "1234567890");
        UserRepository.addUser(user);
        
        List<User> users = UserRepository.getUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void testClear() {
        UserRepository.addUser(new User("Nathan", "njt38@kent.ac.uk", "1234567890"));
        UserRepository.clear();
        assertTrue(UserRepository.getUsers().isEmpty());
    }

    @Test
    public void testLoadDefaults() {
        UserRepository.loadDefaults();
        List<User> users = UserRepository.getUsers();
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("njt38@kent.ac.uk")));
    }
}
