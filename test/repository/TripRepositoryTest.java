package repository;

import model.Trip;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TripRepositoryTest {

    @Before
    public void setUp() {
        TripRepository.clear();
    }

    @Test
    public void testAddAndGetTrips() {
        Trip trip = new Trip("T1", "Heathrow", new Date(), new Date(), "Standard");
        TripRepository.addTrip(trip);
        
        List<Trip> trips = TripRepository.getTrips();
        assertEquals(1, trips.size());
        assertEquals(trip, trips.get(0));
    }

    @Test
    public void testFindMatchingTripExact() {
        Date now = new Date();
        Trip trip = new Trip("T1", "London", now, now, "Standard");
        TripRepository.addTrip(trip);
        
        Optional<Trip> result = TripRepository.findMatchingTrip("London", now, now, "Standard");
        assertTrue(result.isPresent());
        assertEquals(trip, result.get());
    }

    @Test
    public void testFindMatchingTripCloseTime() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        
        cal.add(Calendar.MINUTE, 20);
        Date time1 = cal.getTime();
        
        Trip trip = new Trip("T1", "London", date, time1, "Standard");
        TripRepository.addTrip(trip);
        
        // Search with exact date
        Optional<Trip> result = TripRepository.findMatchingTrip("London", date, date, "Standard");
        assertTrue("Should find trip within 30 min window", result.isPresent());
    }

    @Test
    public void testFindMatchingTripOutsideTimeWindow() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        
        cal.add(Calendar.MINUTE, 31);
        Date time1 = cal.getTime();
        
        Trip trip = new Trip("T1", "London", date, time1, "Standard");
        TripRepository.addTrip(trip);
        
        Optional<Trip> result = TripRepository.findMatchingTrip("London", date, date, "Standard");
        assertFalse("Should not find trip outside 30 min window", result.isPresent());
    }

    @Test
    public void testFindMatchingTripDifferentDestination() {
        Date now = new Date();
        Trip trip = new Trip("T1", "London", now, now, "Standard");
        TripRepository.addTrip(trip);
        
        Optional<Trip> result = TripRepository.findMatchingTrip("Paris", now, now, "Standard");
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindMatchingTripDifferentVehicleType() {
        Date now = new Date();
        Trip trip = new Trip("T1", "London", now, now, "Standard");
        TripRepository.addTrip(trip);

        Optional<Trip> result = TripRepository.findMatchingTrip("London", now, now, "Minibus");
        assertFalse("Should not find trip with different vehicle type", result.isPresent());
    }
}
