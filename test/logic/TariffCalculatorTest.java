package logic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TariffCalculatorTest {

    // Passed in a standard car going 10 miles with 2 bags on a Monday at 16:00 to verify
    // the baseline math. This checked that it correctly calculated 25 for the distance and 4
    // for the bags without accidentally triggering any of the multipliers.
    @Test
    public void testStandardWeekdayNoDiscount() {
        double estimate = TariffCalculator.calculateEstimate("Standard", 2, 10.0, "Monday", 16);

        // For this it uses 0.001 as the delta to allow a tiny margin of error so Java floating point
        // rounding did not cause the test to falsely fail.
        assertEquals(29.00, estimate, 0.001);
    }

    // Fed in an Executive car going 20 miles with 0 bags on a Saturday at 2am to confirm
    // all the multipliers stacked correctly and brought the 50 base fare down to exactly 76.50.
    @Test
    public void testExecutiveWeekendLateNight() {
        double estimate = TariffCalculator.calculateEstimate("Executive", 0, 20.0, "Saturday", 2);

        assertEquals(76.50, estimate, 0.001);
    }

    @Test
    public void testMinibusWeekday() {
        // Minibus multiplier 2.0
        // 10 miles * 2.5 * 2.0 = 50.0
        // 16:00 (No discount)
        double estimate = TariffCalculator.calculateEstimate("Minibus", 0, 10.0, "Monday", 16);
        assertEquals(50.0, estimate, 0.001);
    }

    @Test
    public void testStandardWeekendSurge() {
        // Standard multiplier 1.0
        // 10 miles * 2.5 = 25.0
        // Saturday (Surge 1.2) -> 25 * 1.2 = 30.0
        // 16:00 (No discount)
        double estimate = TariffCalculator.calculateEstimate("Standard", 0, 10.0, "Saturday", 16);
        assertEquals(30.0, estimate, 0.001);
    }

    @Test
    public void testOffPeakDiscount() {
        // 10 miles * 2.5 = 25.0
        // 12:00 (Off-peak 0.8) -> 25 * 0.8 = 20.0
        double estimate = TariffCalculator.calculateEstimate("Standard", 0, 10.0, "Monday", 12);
        assertEquals(20.0, estimate, 0.001);
    }
    }