package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip {
    private String id;
    private String destination;
    private Date date;
    private Date time;
    private String vehicleType; // e.g., "Standard", "Executive", "Van"
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

    public void addBooking(Booking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
            recalculateDuration();
        }
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        recalculateDuration();
    }

    private void recalculateDuration() {
        // Simple logic: base duration (first booking) + 10 mins for each extra pickup
        if (bookings.isEmpty()) {
            this.totalDuration = 0;
            return;
        }
        
        int baseDuration = bookings.get(0).getLengthEstimate(); // Simplified
        this.totalDuration = baseDuration + ((bookings.size() - 1) * 10);
    }

    public int getTotalDuration() { return totalDuration; }

    @Override
    public String toString() {
        return "Trip " + id + " to " + destination + " [" + vehicleType + "] - " + bookings.size() + " passengers";
    }
}
