package repository;

import classes.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to save users.
 * Hardcoded at the moment to make testing easier.
 */
public class UserRepository {

    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User("Nathan Thompson", "njt38@kent.ac.uk", "987654321"));
        users.add(new User("Ibitola Omole", "dioo2@kent.ac.uk", "123456789"));
        users.add(new User("Jeremy Mensah", "jm2463@kent.ac.uk", "987654321"));
        users.add(new User("Maryjane Obi", "cmo30@kent.ac.uk", "123456789"));
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
