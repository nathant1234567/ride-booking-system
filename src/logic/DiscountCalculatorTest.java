//package logic;
//
//import org.testng.annotations.Test;
//import static org.junit.Assert.assertEquals;
//
//public class DiscountCalculatorTest {
//
//    @Test
//    public void testOffPeakDiscount() {
//        double result = DiscountCalculator.calculateDiscount(100.0, 12);
//        assertEquals(80.0, result, 0.001);
//    }
//
//    @Test
//    public void testLateNightDiscount() {
//        double result = DiscountCalculator.calculateDiscount(100.0, 23);
//        assertEquals(85.0, result, 0.001);
//    }
//
//    @Test
//    public void testNoDiscount() {
//        double result = DiscountCalculator.calculateDiscount(100.0, 8);
//        assertEquals(100.0, result, 0.001);
//    }
//}