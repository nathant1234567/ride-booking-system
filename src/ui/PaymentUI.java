package ui;

import javax.swing.*;
import java.awt.*;
import classes.Payment;

public class PaymentUI extends JFrame {

    private JTextField amountField;
    private JComboBox<String> methodBox;
    private JTextArea outputArea;

    private Payment payment;

    public PaymentUI(Payment payment) {

        this.payment = payment;

        setTitle("Payment UI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // better than EXIT_ON_CLOSE
        setLayout(new FlowLayout());

        add(new JLabel("Amount (£):"));
        amountField = new JTextField(10);
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



        payButton.addActionListener(e -> handlePayment());
    }

    private void handlePayment() {

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
                outputArea.setText(
                        "Payment Successful!\n" +
                                "Amount: £" + amount + "\n" +
                                "Method: " + method
                );
            } else {
                outputArea.setText("Payment Failed. Invalid input or method.");
            }

        } catch (NumberFormatException e) {
            outputArea.setText("Invalid amount. Please enter a valid number.");
        }
    }
}