package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class DiscountCalculatorTest {

    // Fed the calculator a base price of 100 and a time of 12(noon) to verify the daytime discount logic
    // caught the hour within the 10:00 to 15:00 window and correctly brought the price down to 80.

    @Test
    public void testOffPeakDiscount() {
        double result = DiscountCalculator.calculateDiscount(100.0, 12);
        // For this it uses 0.001 as the delta to allow a tiny margin of error so Java floating point
        // rounding did not cause the test to falsely fail.
        assertEquals(80.0, result, 0.001);
    }

    // Then passed in 23 (11:00 PM) to validate the cross midnight logic and confirm that the OR condition
    // in the DiscountCalculator file was triggered correctly bringing a 100 ticket down to 85
    @Test
    public void testLateNightDiscount() {
        double result = DiscountCalculator.calculateDiscount(100.0, 23);
        assertEquals(85.0, result, 0.001);
    }

    @Test
    public void testNoDiscount() {
        // Passed in 8 (8:00 AM) to prove that a time outside both discount windows was correctly
        // ignored by the system and left the base price completely untouched
        double result = DiscountCalculator.calculateDiscount(100.0, 8);
        assertEquals(100.0, result, 0.001);
    }
}
