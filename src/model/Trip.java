package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class representing a trip, which can have multiple bookings. The trip has a destination, date, time, and vehicle type.
 */
public class Trip {

    private String id;
    private String destination;
    private Date date;
    private Date time;
    private String vehicleType;
    private List<Booking> bookings;
    private int totalDuration;

    /**
     * Constructs a new Trip instance with the specified id, destination, date, time, and vehicle type.
     * Initialises an empty list of bookings.
     *
     * @param id the unique identifier for the trip
     * @param destination the destination of the trip
     * @param date the date of the trip
     * @param time the time of the trip
     * @param vehicleType the type of vehicle assigned for the trip (e.g., "Van", "Executive", or "Standard")
     */
    public Trip(String id, String destination, Date date, Date time, String vehicleType) {
        this.id = id;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.vehicleType = vehicleType;
        this.bookings = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getDestination() { return destination; }
    public Date getDate() { return date; }
    public Date getTime() { return time; }
    public String getVehicleType() { return vehicleType; }
    public List<Booking> getBookings() { return bookings; }

    /**
     * Dynamic capacity checks based on vehicle type
     * @return
     */
    public int getMaxCapacity() {
        if ("Van".equalsIgnoreCase(vehicleType)) return 8;
        if ("Executive".equalsIgnoreCase(vehicleType)) return 3;
        return 4; // Default Standard capacity
    }

    /**
     * Calculates the total number of passengers across all bookings for the trip.
     *
     * @return the total number of passengers from all associated bookings
     */
    public int getPassengerCount() {
        return bookings.stream().mapToInt(Booking::getNumberOfPassengers).sum();
    }

    /**
     * Attempts to add a booking to the trip. Ensures that the total number of passengers across all bookings
     * does not exceed the maximum capacity for the trip. Recalculates the total trip duration upon successful addition.
     *
     * @param booking the booking to be added to the trip
     * @return true if the booking is successfully added; false if the booking already exists or adding it would exceed the trip's maximum capacity
     */
    public boolean addBooking(Booking booking) {
        if (getPassengerCount() + booking.getNumberOfPassengers() > getMaxCapacity()) {
            return false; // trip is full
        }
        if (!bookings.contains(booking)) {
            bookings.add(booking);
            recalculateDuration();
            return true;
        }
        return false;
    }

    /**
     * Removes a booking from the trip. Recalculates the total trip duration upon successful removal.
     * @param booking
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        recalculateDuration();
    }

    /**
     * Recalculates the total trip duration based on the length estimates of all bookings. If there are no bookings, sets the duration to 0.
     */
    private void recalculateDuration() {
        if (bookings.isEmpty()) {
            this.totalDuration = 0;
            return;
        }
        int baseDuration = bookings.get(0).getLengthEstimate();
        this.totalDuration = baseDuration + ((bookings.size() - 1) * 10);
    }

    /**
     * Returns the total duration of the trip in minutes.
     * @return
     */
    public int getTotalDuration() { return totalDuration; }

    @Override
    public String toString() {
        return "Trip " + id + " to " + destination + " [" + vehicleType + "] - " + getPassengerCount() + " passengers";
    }
}
