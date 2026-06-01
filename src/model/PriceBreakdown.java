package model;

public class PriceBreakdown {
    /**
     * Class to represent the price breakdown of a trip, including base price, fees, discounts, and final total.
     */
    private double base;
    private double passengerFee;
    private double luggageFee;
    private double subtotal;
    private double discountPercent;
    private double discountAmount;
    private double finalTotal;
    private String discountDescription;

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

    @Override
    public String toString() {
        return String.format("Subtotal: £%.2f, Discounts: %.2f%% (£%.2f), Total: £%.2f",
                subtotal, discountPercent * 100.0, discountAmount, finalTotal);
    }
}

