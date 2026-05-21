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

<<<<<<< HEAD
    // Save state variables to detect real scheduling alterations
    private java.util.Date originalDate;
    private java.util.Date originalTime;
=======
    public PaymentUI(Payment payment, PriceBreakdown breakdown, Booking booking) {
        setResizable(false);
>>>>>>> 7a7a2ab461d35df26b30a123bcde9d31e4f1513c

    public PaymentUI(Payment payment, PriceBreakdown breakdown, Booking booking) {
        this.payment = payment;
        this.breakdown = breakdown;
        this.booking = booking;

        // Cache historical timestamps to determine if a real change occurred
        this.originalDate = booking.getDate();
        this.originalTime = booking.getTime();

        setTitle("Payment UI");
        setSize(420, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Amount (£):"));
        amountField = new JTextField(10);
        amountField.setEditable(false);
        add(amountField);

        add(new JLabel("Method:"));
        methodBox = new JComboBox<>(new String[]{"card", "bank"});
        add(methodBox);

        JButton payButton = new JButton("Pay");
        add(payButton);

        JButton cancelBtn = new JButton("Cancel");
        add(cancelBtn);

        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        JButton discountButton = new JButton("Apply Discount");
        add(discountButton);

        discountButton.addActionListener(e -> {
            DiscountUI.launch(amountField, discountButton);
        });

        JButton amendBtn = new JButton("Amend this Booking");
        amendBtn.setEnabled(true); // Enabled so users can change choices before paying
        add(amendBtn);

<<<<<<< HEAD
        // --- DYNAMIC PRICE CALCULATION ON LOAD ---
        refreshDisplayAmount();

        // Click listeners recalculate live data on event trigger
        amendBtn.addActionListener(e -> openQuickAmendDialog(this, this.booking));
        payButton.addActionListener(e -> handlePayment());

        cancelBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel this payment?",
                    "Cancel Payment", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });
=======
        amendBtn.addActionListener(e -> openQuickAmendDialog());

        payButton.addActionListener(e -> handlePayment(amendBtn));
>>>>>>> 7a7a2ab461d35df26b30a123bcde9d31e4f1513c
    }

    /**
     * Recalculates price parameters.
     * Enforces the Amendment Fee ONLY if the Date or Time has actually been altered.
     */
    public void refreshDisplayAmount() {
        // 1. Recalculate price parameters with latest booking details
        this.breakdown = BookingService.calculatePriceWithDiscount(this.booking, null);

        // 2. Conditionally apply the fee only if scheduling metrics differ
        double amendmentFee = 0.0;
        if (!this.booking.getDate().equals(originalDate) || !this.booking.getTime().equals(originalTime)) {
            amendmentFee = BookingService.calculateAmendmentFee(this.booking);
        }

        double totalDue = this.breakdown.getFinalTotal() + amendmentFee;
        this.amountField.setText(String.format("%.2f", totalDue));
    }

    private void handlePayment() {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            outputArea.setText("Please enter an amount.");
            return;
        }

        try {
            double activeAmount = Double.parseDouble(amountText);
            String method = (String) methodBox.getSelectedItem();

            if (method == null) {
                outputArea.setText("Please select a payment method.");
                return;
            }

            boolean success = payment.processPayment(activeAmount, method);

            if (success) {
                // Determine whether to display the amendment handling charge line item
                double amendmentFee = 0.0;
                if (!this.booking.getDate().equals(originalDate) || !this.booking.getTime().equals(originalTime)) {
                    amendmentFee = BookingService.calculateAmendmentFee(this.booking);
                }

                StringBuilder sb = new StringBuilder();
                sb.append("Payment Successful!\n");
                sb.append(String.format("Base: £%.2f\n", this.breakdown.getBase()));
                sb.append(String.format("Passengers fee: £%.2f\n", this.breakdown.getPassengerFee()));
                sb.append(String.format("Luggage fee: £%.2f\n", this.breakdown.getLuggageFee()));
                sb.append(String.format("Subtotal: £%.2f\n", this.breakdown.getSubtotal()));
                if (this.breakdown.getDiscountAmount() > 0) {
                    sb.append(String.format("Discounts (%s): -£%.2f\n", this.breakdown.getDiscountDescription(), this.breakdown.getDiscountAmount()));
                }
                if (amendmentFee > 0) {
                    sb.append(String.format("Amendment Fee: +£%.2f\n", amendmentFee));
                }
                sb.append(String.format("Total charged: £%.2f\n", activeAmount));
                sb.append("Method: " + method + "\n");

                outputArea.setText(sb.toString());

                if (ManageBookingsUI.getInstance() != null) {
                    ManageBookingsUI.getInstance().loadBookings();
                }
            } else {
                outputArea.setText("Payment Failed. Invalid input or method.");
            }

        } catch (NumberFormatException e) {
            outputArea.setText("Invalid amount entry processing error.");
        }
    }

    public static void openQuickAmendDialog(Component parent, Booking booking) {
        if (booking == null) return;

        Window activeWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(activeWindow, "Amend " + booking.toString(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        AmendBookingUI amendPanel = new AmendBookingUI();
        dialog.add(amendPanel, BorderLayout.CENTER);

        // --- REFRESH ACTION DETECTOR ON WINDOW CLOSURE ---
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // If parent context is a PaymentUI instance, refresh the visible price calculations
                if (activeWindow instanceof PaymentUI) {
                    ((PaymentUI) activeWindow).refreshDisplayAmount();
                }
            }
        });

        dialog.setVisible(true);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7a7a2ab461d35df26b30a123bcde9d31e4f1513c
