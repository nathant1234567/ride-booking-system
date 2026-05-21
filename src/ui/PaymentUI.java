package ui;

import javax.swing.*;
import java.awt.*;
import classes.Booking;
import classes.Payment;
import classes.PriceBreakdown;
import repository.BookingRepository;
import service.BookingService;

public class PaymentUI extends JFrame {

    private JTextField amountField;
    private JComboBox<String> methodBox;
    private JTextArea outputArea;

    private Payment payment;
    private PriceBreakdown breakdown;
    private Booking booking;

    public PaymentUI(Payment payment, PriceBreakdown breakdown, Booking booking) {

        this.payment = payment;
        this.breakdown = breakdown;
        this.booking = booking;

        setTitle("Payment UI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Amount (£):"));
        amountField = new JTextField(10);
        amountField.setText(String.format("%.2f", breakdown.getFinalTotal()));
        amountField.setEditable(false);
        add(amountField);

        add(new JLabel("Method:"));
        methodBox = new JComboBox<>(new String[]{"card", "bank"});
        add(methodBox);

        JButton payButton = new JButton("Pay");
        add(payButton);

        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        JButton discountButton = new JButton("Apply Discount");
        add(discountButton);

        discountButton.addActionListener(e -> {
            DiscountUI.launch(amountField, discountButton);
        });

        JButton amendBtn = new JButton("Amend this Booking");
        amendBtn.setEnabled(false);
        add(amendBtn);

        amendBtn.addActionListener(e -> openQuickAmendDialog());

        payButton.addActionListener(e -> handlePayment(amendBtn));
    }

    private void handlePayment(JButton amendBtn) {

        String amountText = amountField.getText().trim();

        if (amountText.isEmpty()) {
            outputArea.setText("Please enter an amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            String method = (String) methodBox.getSelectedItem();

            if (method == null) {
                outputArea.setText("Please select a payment method.");
                return;
            }

            boolean success = payment.processPayment(amount, method);

            if (success) {
                StringBuilder sb = new StringBuilder();
                sb.append("Payment Successful!\n");
                sb.append(String.format("Base: £%.2f\n", this.breakdown.getBase()));
                sb.append(String.format("Passengers fee: £%.2f\n", this.breakdown.getPassengerFee()));
                sb.append(String.format("Luggage fee: £%.2f\n", this.breakdown.getLuggageFee()));
                sb.append(String.format("Subtotal: £%.2f\n", this.breakdown.getSubtotal()));
                if (this.breakdown.getDiscountAmount() > 0) {
                    sb.append(String.format("Discounts (%s): -£%.2f\n", this.breakdown.getDiscountDescription(), this.breakdown.getDiscountAmount()));
                }
                sb.append(String.format("Total charged: £%.2f\n", amount));
                sb.append("Method: " + method + "\n");

                outputArea.setText(sb.toString());
                amendBtn.setEnabled(true);
            } else {
                outputArea.setText("Payment Failed. Invalid input or method.");
            }

        } catch (NumberFormatException e) {
            outputArea.setText("Invalid amount. Please enter a valid number.");
        }
    }

    private void openQuickAmendDialog() {
        if (booking == null) return;

        JDialog dialog = new JDialog(this, "Amend Booking #" + System.identityHashCode(booking), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField pickup = new JTextField(booking.getPickupLocation());
        JTextField destination = new JTextField(booking.getDestination());
        JTextField length = new JTextField(String.valueOf(booking.getLengthEstimate()));
        JSpinner passengers = new JSpinner(new SpinnerNumberModel(booking.getNumberOfPassengers(), 1, 8, 1));
        JSpinner luggage = new JSpinner(new SpinnerNumberModel(booking.getNumberOfLuggage(), 0, 10, 1));
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(booking.getDate(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel(booking.getTime(), null, null, java.util.Calendar.HOUR_OF_DAY));

        form.add(new JLabel("Pickup:"));
        form.add(pickup);
        form.add(new JLabel("Destination:"));
        form.add(destination);
        form.add(new JLabel("Length (km):"));
        form.add(length);
        form.add(new JLabel("Passengers:"));
        form.add(passengers);
        form.add(new JLabel("Luggage:"));
        form.add(luggage);
        form.add(new JLabel("Date:"));
        form.add(dateSpinner);
        form.add(new JLabel("Time:"));
        form.add(timeSpinner);

        dialog.add(new JScrollPane(form), BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(ae -> {
            try {
                int len = Integer.parseInt(length.getText().trim());
                Booking updated = new Booking(booking.getUser(), destination.getText().trim(), pickup.getText().trim(), len,
                        (int) passengers.getValue(), (int) luggage.getValue(), (java.util.Date) dateSpinner.getValue(), (java.util.Date) timeSpinner.getValue());

                if (BookingRepository.updateBooking(booking, updated)) {
                    PriceBreakdown newBreakdown = BookingService.calculatePriceWithDiscount(updated, null);
                    JOptionPane.showMessageDialog(dialog, String.format("Booking updated! New price: £%.2f", newBreakdown.getFinalTotal()));
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update booking", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid length", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel south = new JPanel();
        south.add(saveBtn);
        dialog.add(south, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}