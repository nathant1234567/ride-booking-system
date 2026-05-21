package classes;

import java.util.Date;

/**
 * Simple Trip class that represents an actual assigned trip based on a booking.
 */
public class Trip {
    private static int counter = 1;

    private int id;
    private Booking booking;
    private String status; // e.g., "Scheduled", "In Progress", "Completed", "Cancelled"
    private double price;
    private Date createdAt;

    public Trip(Booking booking, double price) {
        this.id = counter++;
        this.booking = booking;
        this.price = price;
        this.status = "Scheduled";
        this.createdAt = new Date();
    }

    public int getId() { return id; }
    public Booking getBooking() { return booking; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Date getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", booking=" + booking +
                ", status='" + status + '\'' +
                ", price=" + price +
                '}';
    }
}

