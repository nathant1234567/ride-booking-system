package model;

/**
 * Class representing a user of the system. Users are currently hard coded in the repository.
 */
public class User {

    private String username;
    private String email;
    private String phoneNumber;

    /**
     * Constructor to create a new User instance. Users are currently hard coded in the repository.
     * @param username
     * @param email
     * @param phoneNumber
     */
    public User(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return username + " (" + email + ")";
    }
}