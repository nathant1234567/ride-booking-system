package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip {
    /**
     * Class representing a trip, which can have multiple bookings. The trip has a destination, date, time, and vehicle type.
     */
    private String id;
    private String destination;
    private Date date;
    private Date time;
    private String vehicleType;
    private List<Booking> bookings;
    private int totalDuration;

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

    // Dynamic capacity checks based on vehicle type ---
    public int getMaxCapacity() {
        if ("Van".equalsIgnoreCase(vehicleType)) return 8;
        if ("Executive".equalsIgnoreCase(vehicleType)) return 3;
        return 4; // Default Standard capacity
    }

    public int getPassengerCount() {
        return bookings.stream().mapToInt(Booking::getNumberOfPassengers).sum();
    }

    // Updated to return boolean to inform the service if the trip was too full
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

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        recalculateDuration();
    }

    private void recalculateDuration() {
        if (bookings.isEmpty()) {
            this.totalDuration = 0;
            return;
        }
        int baseDuration = bookings.get(0).getLengthEstimate();
        this.totalDuration = baseDuration + ((bookings.size() - 1) * 10);
    }

    public int getTotalDuration() { return totalDuration; }

    @Override
    public String toString() {
        return "Trip " + id + " to " + destination + " [" + vehicleType + "] - " + getPassengerCount() + " passengers";
    }
}
