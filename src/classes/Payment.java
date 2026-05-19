package classes;

public class Payment {

    public boolean processPayment(double amount, String method) {

        if (amount <= 0) {
            return false;
        }

        if (method == null || method.trim().isEmpty()) {
            return false;
        }

        switch (method.toLowerCase()) {

            case "card":
                return processCard(amount);

            case "bank":
                return processBank(amount);

            default:
                return false;
        }
    }

    private boolean processCard(double amount) {
        System.out.println("Processing CARD payment: £" + amount);
        return true;
    }

    private boolean processBank(double amount) {
        System.out.println("Processing BANK transfer: £" + amount);
        return true;
    }
}