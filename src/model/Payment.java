package model;

/**
 * The Payment class is responsible for handling the payment processing logic
 * for transactions related to bookings. It verifies the validity of inputs
 * such as payment amounts and methods, and delegates the payment processing
 * to the appropriate method based on the selected payment option.
 */
public class Payment {

    /**
     * Main method used to handle the payment logic for our bookings.
     * Checks if the inputs make sense and routes the payment to card or bank.
     *
     * @param amount the total cost of the trip
     * @param method how the user wants to pay (card or bank)
     * @return true if payment goes through, false if something is wrong
     */
    public boolean processPayment(double amount, String method) {

        // Section check: don't allow free or negative payments
        if (amount <= 0) {
            return false;
        }

        // Error check: make sure the method isn't null or empty spacing
        if (method == null || method.trim().isEmpty()) {
            return false;
        }

        // Figure out what the user clicked in the UI dropdown menu
        switch (method.toLowerCase()) {

            case "card":
                return processCard(amount);

            case "bank":
                return processBank(amount);

            default:
                // Catch false statements in case an invalid string gets passed
                return false;
        }
    }

    // Print out to simulate card processing overhead
    private boolean processCard(double amount) {
        System.out.println("Processing CARD payment: £" + amount);
        return true;
    }

    // Print out to simulate bank transfer tracking
    private boolean processBank(double amount) {
        System.out.println("Processing BANK transfer: £" + amount);
        return true;
    }
}
