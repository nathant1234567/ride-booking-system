package classes;

import java.util.Date;

public class Booking {
    private User user;
    private String destination;
    private String pickupLocation;
    private int lengthEstimate;
    private int numberOfPassengers;
    private int numberOfLuggage;
    private Date date;
    private Date time;
    private Trip trip;

    public Booking(User user, String destination, String pickupLocation, int lengthEstimate, int numberOfPassengers, int amountOfLuggage, Date date, Date time) {
        this.user = user;
        this.destination = destination;
        this.pickupLocation = pickupLocation;
        this.lengthEstimate = lengthEstimate;
        this.numberOfPassengers = numberOfPassengers;
        this.numberOfLuggage = numberOfLuggage;
        this.date = date;
        this.time = time;
    }

    // Backwards-compatible constructor (no luggage specified)
    public Booking(User user, String destination, String pickupLocation, int lengthEstimate, int numberOfPassengers, Date date, Date time) {
        this(user, destination, pickupLocation, lengthEstimate, numberOfPassengers, 0, date, time);
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

    @Override
    public String toString() {
        return "Booking{" +
                "user=" + user +
                ", destination='" + destination + '\'' +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", lengthEstimate=" + lengthEstimate +
                ", numberOfPassengers=" + numberOfPassengers +
                ", numberOfLuggage=" + numberOfLuggage +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
