package ui;

import javax.swing.*;
import java.awt.*;
import classes.Booking;
import classes.Payment;
import classes.PriceBreakdown;
import repository.BookingRepository;
import service.BookingService;

public class PaymentUI extends JFrame {
    /**
     * Class that creates the window for processing booking payments.
     * It shows the dynamic price calculations and allows users to pay via card or bank transfer,
     * apply discounts, or open the amendment dialog box.
     */

    // Graphical user interface text fields, choices, and text display boxes
    private JTextField amountField;
    private JComboBox<String> methodBox;
    private JTextArea outputArea;

    // Core transaction and data reference objects
    private Payment payment;
    private PriceBreakdown breakdown;
    private Booking booking;

    // Fixed primitive variables to keep track of the booking's original state
    private long originalDateMs;
    private long originalTimeMs;

    public PaymentUI(Payment payment, PriceBreakdown breakdown, Booking booking) {
        this.payment = payment;
        this.breakdown = breakdown;
        this.booking = booking;

        // Freeze the window size so it doesn't stretch or break the layout
        setResizable(false);

        // Capture the booking's current date and time as numbers so we can detect changes later
        this.originalDateMs = booking.getDate() != null ? booking.getDate().getTime() : 0L;
        this.originalTimeMs = booking.getTime() != null ? booking.getTime().getTime() : 0L;

        // Setup the basic frame layout and window title
        setTitle("Payment UI");
        setSize(420, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Add the price display row (locked so users cannot type inside it)
        add(new JLabel("Amount (£):"));
        amountField = new JTextField(10);
        amountField.setEditable(false);
        add(amountField);

        // Add the dropdown selection menu for payment types
        add(new JLabel("Method:"));
        methodBox = new JComboBox<>(new String[]{"card", "bank"});
        add(methodBox);

        // Core interactive button fields
        JButton payButton = new JButton("Pay");
        add(payButton);

        JButton cancelBtn = new JButton("Cancel");
        add(cancelBtn);

        // Add the text scrolling pane to output transaction logs and receipts
        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        // Setup the apply discount button from Maryjane's feature merge
        JButton discountButton = new JButton("Apply Discount");
        add(discountButton);

        discountButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                DiscountUI.launch(amountField, discountButton);
            }
        });

        // Setup the amendment trigger button
        JButton amendBtn = new JButton("Amend this Booking");
        amendBtn.setEnabled(true);
        add(amendBtn);

        // Refresh the displayed cost box to calculate the dynamic totals on load
        refreshDisplayAmount();

        // Standard listeners to execute our window interactions smoothly
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

    /**
     * Checks if the date or time on the booking has actually been modified
     */
    private boolean isBookingAltered() {
        if (this.booking == null || this.booking.getDate() == null || this.booking.getTime() == null) {
            return false;
        }
        return (this.booking.getDate().getTime() != originalDateMs) ||
                (this.booking.getTime().getTime() != originalTimeMs);
    }

    /**
     * Recalculates the trip costs and conditionally appends the amendment fee if changed
     */
    public void refreshDisplayAmount() {
        // Grab the latest price breakdown details
        this.breakdown = BookingService.calculatePriceWithDiscount(this.booking, null);

        // If the user changed the date or time inside the form, apply the extra processing fee
        double amendmentFee = 0.0;
        if (isBookingAltered()) {
            amendmentFee = BookingService.calculateAmendmentFee(this.booking);
        }

        // Calculate final total and write it cleanly to our text field
        double totalDue = this.breakdown.getFinalTotal() + amendmentFee;
        this.amountField.setText(String.format("%.2f", totalDue));
    }

    /**
     * Handles the transaction click events and prints out an itemised receipt
     */
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

            // Route execution through our transaction logic processing methods
            boolean success = payment.processPayment(activeAmount, method);

            if (success) {
                double amendmentFee = 0.0;
                if (isBookingAltered()) {
                    amendmentFee = BookingService.calculateAmendmentFee(this.booking);
                }

                // Build a clean string layout for our text receipt
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

                // Force our dashboard view to refresh lists automatically
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

    /**
     * Standard method to launch our separate popup amendment entry panel.
     */
    public static void openQuickAmendDialog(Component parent, Booking booking) {
        if (booking == null) return;

        final Window activeWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(activeWindow, "Amend " + booking.toString(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Attach an input panel directly inside the dialog view box
        AmendBookingUI amendPanel = new AmendBookingUI();
        dialog.add(amendPanel, BorderLayout.CENTER);

        // Watch for the amendment form window closing so we can automatically refresh prices
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
