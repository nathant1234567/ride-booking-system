package classes;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private String id;
    private String destination;
    private List<Booking> bookings;
    private int totalDuration;

    public Trip(String id, String destination) {
        this.id = id;
        this.destination = destination;
        this.bookings = new ArrayList<>();
    }

    public String getId() { return id; }

    public String getDestination() { return destination; }

    public List<Booking> getBookings() { return bookings; }

    public void addBooking(Booking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public int getTotalDuration() { return totalDuration; }
    public void setTotalDuration(int totalDuration) { this.totalDuration = totalDuration; }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", destination='" + destination + '\'' +
                ", passengers=" + bookings.size() +
                ", totalDuration=" + totalDuration +
                '}';
    }
}
