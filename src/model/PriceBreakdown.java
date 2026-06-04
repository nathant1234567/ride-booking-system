package model;

/**
 * Class to represent the price breakdown of a trip, including base price, fees, discounts, and final total.
 */
public class PriceBreakdown {

    private double base;
    private double passengerFee;
    private double luggageFee;
    private double subtotal;
    private double discountPercent;
    private double discountAmount;
    private double finalTotal;
    private String discountDescription;

    /**
     * Constructor for the PriceBreakdown class, which encapsulates the cost components of a trip.
     *
     * @param base                The base price of the trip.
     * @param passengerFee        The total passenger-related fees for the trip.
     * @param luggageFee          The total luggage-related fees for the trip.
     * @param subtotal            The total cost before applying discounts.
     * @param discountPercent     The percentage of discount applied.
     * @param discountAmount      The total amount discounted from the subtotal.
     * @param finalTotal          The final total cost after applying all discounts.
     * @param discountDescription A description of the discount, if applicable.
     */
    public PriceBreakdown(double base, double passengerFee, double luggageFee, double subtotal,
                          double discountPercent, double discountAmount, double finalTotal, String discountDescription) {
        this.base = base;
        this.passengerFee = passengerFee;
        this.luggageFee = luggageFee;
        this.subtotal = subtotal;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalTotal = finalTotal;
        this.discountDescription = discountDescription;
    }

    public double getBase() { return base; }
    public double getPassengerFee() { return passengerFee; }
    public double getLuggageFee() { return luggageFee; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountPercent() { return discountPercent; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalTotal() { return finalTotal; }
    public String getDiscountDescription() { return discountDescription; }

    /**
     * Converts the instance of the PriceBreakdown class into a string representation,
     * summarizing the subtotal, discount percentage, discount amount, and final total.
     *
     * @return A formatted string containing the subtotal, discount details, and final total.
     */
    @Override
    public String toString() {
        return String.format("Subtotal: £%.2f, Discounts: %.2f%% (£%.2f), Total: £%.2f",
                subtotal, discountPercent * 100.0, discountAmount, finalTotal);
    }
}

