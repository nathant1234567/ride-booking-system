package service;

import classes.Booking;
import classes.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class BookingServiceTest {

    private User testUser;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        testUser = new User("Nathan", "njt38@kent.ac.uk", "1234567890");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        futureDate = cal.getTime();
    }

    @Test
    void testDurationCalculationLogic() {
        return;
    }

    @Test
    void test24HourCancellationRule() {
        return;
    }
}
