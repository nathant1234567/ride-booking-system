package classes;

import java.util.Date;

public class Booking {
    private User user;
    private String destination;
    private String pickupLocation;
    private int lengthEstimate;
    private int numberOfPassengers;
    private Date date;
    private Date time;

    public Booking(User user, String destination, String pickupLocation, int lengthEstimate, int numberOfPassengers, Date date, Date time) {
        this.user = user;
        this.destination = destination;
        this.pickupLocation = pickupLocation;
        this.lengthEstimate = lengthEstimate;
        this.numberOfPassengers = numberOfPassengers;
        this.date = date;
        this.time = time;
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

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }

    @Override
    public String toString() {
        return "Booking{" +
                "user=" + user +
                ", destination='" + destination + '\'' +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", lengthEstimate=" + lengthEstimate +
                ", numberOfPassengers=" + numberOfPassengers +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
