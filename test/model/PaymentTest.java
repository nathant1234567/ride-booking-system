package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void testCardPaymentSucceeds() {
        Payment payment = new Payment();

        assertTrue(payment.processPayment(25.0, "card"));
    }

    @Test
    void testBankPaymentSucceeds() {
        Payment payment = new Payment();

        assertTrue(payment.processPayment(25.0, "bank"));
    }

    @Test
    void testNegativeAmountFails() {
        Payment payment = new Payment();

        assertFalse(payment.processPayment(-10.0, "card"));
    }

    @Test
    void testInvalidMethodFails() {
        Payment payment = new Payment();

        assertFalse(payment.processPayment(25.0, "paypal"));
    }
}