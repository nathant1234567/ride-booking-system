package repository;

import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to save users.
 */
public class UserRepository {

    private static List<User> users = new ArrayList<>();

    static {
        loadDefaults();
    }

    public static void loadDefaults() {
        users.clear();
        users.add(new User("Nathan Thompson", "njt38@kent.ac.uk", "987654321"));
        users.add(new User("Ibitola Omole", "dioo2@kent.ac.uk", "123456789"));
        users.add(new User("Jeremy Mensah", "jm2463@kent.ac.uk", "987654321"));
        users.add(new User("Maryjane Obi", "cmo30@kent.ac.uk", "123456789"));
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void clear() {
        users.clear();
    }
}
