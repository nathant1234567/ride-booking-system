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

    // Fixed primitive types to ensure secure millisecond timestamp validation
    private long originalDateMs;
    private long originalTimeMs;

    public PaymentUI(Payment payment, PriceBreakdown breakdown, Booking booking) {
        this.payment = payment;
        this.breakdown = breakdown;
        this.booking = booking;

        setResizable(false);

        // Cache exact historical primitives safely to prevent pointer mutations
        this.originalDateMs = booking.getDate() != null ? booking.getDate().getTime() : 0L;
        this.originalTimeMs = booking.getTime() != null ? booking.getTime().getTime() : 0L;

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

        discountButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Passed in the text field so the calculator could update the price on screen and passed in the
                // button so the calculator could disable it and stop the discount from looping.
                DiscountUI.launch(amountField, discountButton);
            }
        });

        JButton amendBtn = new JButton("Amend this Booking");
        amendBtn.setEnabled(true);
        add(amendBtn);

        refreshDisplayAmount();

        final PaymentUI selfRef = this;
        amendBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openQuickAmendDialog(selfRef, selfRef.booking);
            }
        });

        payButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handlePayment();
                // Froze the price once the transaction is done so the total can't be changed
                discountButton.setEnabled(false);
            }

        });

        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(selfRef,
                        "Are you sure you want to cancel this payment?",
                        "Cancel Payment", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    selfRef.dispose();
                }
            }
        });
    }

    private boolean isBookingAltered() {
        if (this.booking == null || this.booking.getDate() == null || this.booking.getTime() == null) {
            return false;
        }
        return (this.booking.getDate().getTime() != originalDateMs) ||
                (this.booking.getTime().getTime() != originalTimeMs);
    }

    public void refreshDisplayAmount() {
        this.breakdown = BookingService.calculatePriceWithDiscount(this.booking, null);

        double amendmentFee = 0.0;
        if (isBookingAltered()) {
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
                double amendmentFee = 0.0;
                if (isBookingAltered()) {
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

        final Window activeWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(activeWindow, "Amend " + booking.toString(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        AmendBookingUI amendPanel = new AmendBookingUI();
        dialog.add(amendPanel, BorderLayout.CENTER);

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (activeWindow instanceof PaymentUI) {
                    ((PaymentUI) activeWindow).refreshDisplayAmount();
                }
            }
        });

        dialog.setVisible(true);
    }
}
