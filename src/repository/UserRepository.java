package repository;

import classes.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User("Nathan Thompson", "njt38@kentac.uk", "987654321"));
        users.add(new User("John Doe", "john@example.com", "123456789"));
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
