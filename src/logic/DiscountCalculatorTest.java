package logic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DiscountCalculatorTest {

    @Test
    public void testOffPeakDiscount() {
        // Checking to make sure the 20% discount works during the day
        double result = DiscountCalculator.calculateDiscount(100.0, 12);
        // Added 0.001 as the required delta for floating-point comparison
        assertEquals(80.0, result, 0.001);
    }

    @Test
    public void testLateNightDiscount() {
        // Testing the late night hours to see if 15% is taken off
        double result = DiscountCalculator.calculateDiscount(100.0, 23);
        assertEquals(85.0, result, 0.001);
    }

    @Test
    public void testNoDiscount() {
        // Making sure normal hours don't get any discount applied
        double result = DiscountCalculator.calculateDiscount(100.0, 8);
        assertEquals(100.0, result, 0.001);
    }
}