package com.ilp.ilp_submission_2.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardUtilsTest {

    @Test
    void testIsValidCreditCardNumber() {
        // Valid credit card numbers
        assertTrue(CreditCardUtils.isValidCreditCardNumber("1234567812345678"));
        assertTrue(CreditCardUtils.isValidCreditCardNumber("0000000000000000"));

        // Invalid credit card numbers
        assertFalse(CreditCardUtils.isValidCreditCardNumber(null));               // Null input
        assertFalse(CreditCardUtils.isValidCreditCardNumber(""));                 // Empty string
        assertFalse(CreditCardUtils.isValidCreditCardNumber("12345678abcd5678")); // Contains non-numeric characters
        assertFalse(CreditCardUtils.isValidCreditCardNumber("12345678"));         // Less than 16 digits
        assertFalse(CreditCardUtils.isValidCreditCardNumber("123456781234567890"));// More than 16 digits
    }

    @Test
    void testIsValidExpiry() {
        // Valid expiry dates
        assertTrue(CreditCardUtils.isValidExpiry("12/25"));
        assertTrue(CreditCardUtils.isValidExpiry("01/30"));

        // Invalid expiry dates
        assertFalse(CreditCardUtils.isValidExpiry(null));       // Null input
        assertFalse(CreditCardUtils.isValidExpiry(""));         // Empty string
        assertFalse(CreditCardUtils.isValidExpiry("1225"));     // Missing slash
        assertFalse(CreditCardUtils.isValidExpiry("12/2025"));  // Year has more than 2 digits
        assertFalse(CreditCardUtils.isValidExpiry("12-25"));    // Invalid format
        assertFalse(CreditCardUtils.isValidExpiry("13/25"));    // Invalid month
        assertFalse(CreditCardUtils.isValidExpiry("00/25"));    // Invalid month
    }

    @Test
    void testIsValidCvv() {
        // Valid CVVs
        assertTrue(CreditCardUtils.isValidCvv("123"));
        assertTrue(CreditCardUtils.isValidCvv("999"));

        // Invalid CVVs
        assertFalse(CreditCardUtils.isValidCvv(null));       // Null input
        assertFalse(CreditCardUtils.isValidCvv(""));         // Empty string
        assertFalse(CreditCardUtils.isValidCvv("12"));       // Less than 3 digits
        assertFalse(CreditCardUtils.isValidCvv("1234"));     // More than 3 digits
        assertFalse(CreditCardUtils.isValidCvv("abc"));      // Non-numeric characters
    }
}
