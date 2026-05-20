package repository;

import classes.Trip;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripRepository {
    private static List<Trip> trips = new ArrayList<>();

    public static void addTrip(Trip trip) {
        trips.add(trip);
    }

    public static List<Trip> getTrips() {
        return new ArrayList<>(trips);
    }

    public static Optional<Trip> findTripByDestinationAndTime(String destination) {
        // for now find a trip to the same destination
        return trips.stream()
                .filter(t -> t.getDestination().equals(destination))
                .findFirst();
    }
}
