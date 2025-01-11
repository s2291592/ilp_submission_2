package com.ilp.ilp_submission_2.utils;

public class CreditCardUtils {
    public static boolean isValidCreditCardNumber(String ccNumber) {
        return (ccNumber != null && ccNumber.matches("\\d{16}"));
    }

    public static boolean isValidExpiry(String expiry) {
        return (expiry != null && expiry.matches("\\d{2}/\\d{2}"));
    }

    public static boolean isValidCvv(String cvv) {
        return (cvv != null && cvv.matches("\\d{3}"));
    }
}
