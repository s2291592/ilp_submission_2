package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.Pizza;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Validates pizzas in an order (count, price, definitions, etc.).
 */
@Component
public class PizzaValidator {

    /**
     * Checks that the pizza array is neither null nor empty,
     * and ensures each pizza has a valid price (>0).
     *
     * @param pizzas array of Pizza from the order
     * @return an OrderValidationCode if invalid, or null if OK
     */
    public OrderValidationCode validatePizzaArray(Pizza[] pizzas) {
        // 1) Null or zero-length check
        if (pizzas == null || pizzas.length == 0) {
            return OrderValidationCode.EMPTY_ORDER;
        }

        // 2) Check each pizza for a valid price
        for (Pizza p : pizzas) {
            if (p == null) {
                return OrderValidationCode.PIZZA_NOT_DEFINED;
            }
            if (p.priceInPence() <= 0) {
                return OrderValidationCode.PRICE_FOR_PIZZA_INVALID;
            }
        }

        // If all good
        return null;
    }

    /**
     * Checks if the array length is <= max pizzas allowed.
     */
    public OrderValidationCode validatePizzaCount(Pizza[] pizzas, int maxCount) {
        if (pizzas != null && pizzas.length > maxCount) {
            return OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED;
        }
        return null;
    }

    /**
     * Checks if all pizzas are found on the "official" restaurant menu
     * and that the prices match exactly.
     *
     * @param orderPizzas  pizzas from the order
     * @param menuPizzas   the official menu from the restaurant
     * @return an OrderValidationCode if invalid, or null if OK
     */
    public OrderValidationCode validateMenuMatch(Pizza[] orderPizzas, Pizza[] menuPizzas) {
        // If the restaurant somehow has no menu
        if (menuPizzas == null || menuPizzas.length == 0) {
            return OrderValidationCode.PIZZA_NOT_DEFINED;
        }

        for (Pizza orderPizza : orderPizzas) {
            // find a match in the restaurant's menu
            boolean found = Arrays.stream(menuPizzas)
                    .anyMatch(mp -> mp.name().equalsIgnoreCase(orderPizza.name())
                            && mp.priceInPence() == orderPizza.priceInPence());
            if (!found) {
                // either not found by name or price mismatch
                return OrderValidationCode.PIZZA_NOT_DEFINED;
                // or PRICE_FOR_PIZZA_INVALID if just the price is wrong
            }
        }
        return null;
    }
}
