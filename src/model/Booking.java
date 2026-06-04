package model;

import java.util.Date;

public class Booking {
    /**
     * Class representing a booking request made by the user which includes fields like destination, timing
     * and luggage.
     */
    private User user;
    private String destination;
    private String pickupLocation;
    private int lengthEstimate;
    private int numberOfPassengers;
    private int numberOfLuggage;
    private Date date;
    private Date time;
    private String vehicleType;
    private Trip trip;

    public Booking(User user, String destination, String pickupLocation, int lengthEstimate, int numberOfPassengers, int numberOfLuggage, Date date, Date time, String vehicleType) {
        this.user = user;
        this.destination = destination;
        this.pickupLocation = pickupLocation;
        this.lengthEstimate = lengthEstimate;
        this.numberOfPassengers = numberOfPassengers;
        this.numberOfLuggage = numberOfLuggage;
        this.date = date;
        this.time = time;
        this.vehicleType = vehicleType;
    }

    // Backwards-compatible constructors
    public Booking(User user, String destination, String pickupLocation, int lengthEstimate, int numberOfPassengers, int numberOfLuggage, Date date, Date time) {
        this(user, destination, pickupLocation, lengthEstimate, numberOfPassengers, numberOfLuggage, date, time, "Standard");
    }

    public Booking(User user, String destination, String pickupLocation, int lengthEstimate, int numberOfPassengers, Date date, Date time) {
        this(user, destination, pickupLocation, lengthEstimate, numberOfPassengers, 0, date, time, "Standard");
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public int getLengthEstimate() { return lengthEstimate; }
    public void setLengthEstimate(int lengthEstimate) { this.lengthEstimate = lengthEstimate; }

    public int getNumberOfPassengers() { return numberOfPassengers; }
    public void setNumberOfPassengers(int numberOfPassengers) { this.numberOfPassengers = numberOfPassengers; }

    public int getNumberOfLuggage() { return numberOfLuggage; }
    public void setNumberOfLuggage(int numberOfLuggage) { this.numberOfLuggage = numberOfLuggage; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    // --- NEW CLEAN DISPLAY STRING FOR THE JCOMBOBOX DROPDOWN ---
    @Override
    public String toString() {
        String identifier = "Guest";
        if (this.user != null) {
            // Pulls the user's email address context dynamically to keep things simple and secure
            identifier = this.user.getEmail() != null ? this.user.getEmail() : "User";
        }

        // Formats a clean, readable text sequence for your selection rows
        return String.format("%s to %s (Pickup: %s)",
                identifier,
                this.destination,
                this.pickupLocation
        );
    }
}
