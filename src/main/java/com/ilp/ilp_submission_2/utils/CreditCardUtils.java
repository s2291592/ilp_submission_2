package com.ilp.ilp_submission_2.utils;

public class CreditCardUtils {
    public static boolean isValidCreditCardNumber(String ccNumber) {
        return (ccNumber != null && ccNumber.matches("\\d{16}"));
    }

    public static boolean isValidExpiry(String expiry) {
        if (expiry == null || !expiry.matches("\\d{2}/\\d{2}")) {
            return false;
        }

        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        return month >= 1 && month <= 12;
    }


    public static boolean isValidCvv(String cvv) {
        return (cvv != null && cvv.matches("\\d{3}"));
    }
}
