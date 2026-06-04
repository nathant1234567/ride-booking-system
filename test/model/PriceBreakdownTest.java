package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PriceBreakdownTest {

    @Test
    public void testPriceBreakdownConstructorAndGetters() {
        PriceBreakdown pb = new PriceBreakdown(100.0, 5.0, 10.0, 115.0, 0.1, 11.5, 103.5, "Discount");
        
        assertEquals(100.0, pb.getBase());
        assertEquals(5.0, pb.getPassengerFee());
        assertEquals(10.0, pb.getLuggageFee());
        assertEquals(115.0, pb.getSubtotal());
        assertEquals(0.1, pb.getDiscountPercent());
        assertEquals(11.5, pb.getDiscountAmount());
        assertEquals(103.5, pb.getFinalTotal());
        assertEquals("Discount", pb.getDiscountDescription());
    }

    @Test
    public void testToString() {
        PriceBreakdown pb = new PriceBreakdown(100.0, 5.0, 10.0, 115.0, 0.1, 11.5, 103.5, "Discount");
        // Subtotal: £115.00, Discounts: 10.00% (£11.50), Total: £103.50
        String expected = "Subtotal: £115.00, Discounts: 10.00% (£11.50), Total: £103.50";
        assertEquals(expected, pb.toString());
    }
}
