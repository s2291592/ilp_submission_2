package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.constant.SystemConstants;
import com.ilp.ilp_submission_2.data.Pizza;

import java.util.Arrays;

public final class OrderUtils {

    private OrderUtils() {
        // Utility class: private constructor to prevent instantiation
    }

    /**
     * Calculates the total expected price of an order:
     * the sum of all pizzas + the fixed delivery charge.
     */
    public static int calculateExpectedTotal(Pizza[] pizzas) {
        int sum = Arrays.stream(pizzas)
                .mapToInt(Pizza::priceInPence)
                .sum();
        return sum + SystemConstants.ORDER_CHARGE_IN_PENCE;
    }
}
