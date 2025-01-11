package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Restaurant;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

@Component
public class RestaurantValidator {

    /**
     * Checks if the restaurant is open on the given dayOfWeek.
     *
     * @param restaurant the restaurant in question
     * @param orderDay   the day of the order
     * @return an OrderValidationCode if closed, or null if open
     */
    public OrderValidationCode validateRestaurantOpen(Restaurant restaurant, DayOfWeek orderDay) {
        if (restaurant == null || restaurant.openingDays() == null) {
            // If the restaurant is null or has no openingDays, assume invalid
            return OrderValidationCode.RESTAURANT_CLOSED;
        }

        // Check if orderDay is in the restaurant's openingDays array
        boolean isOpen = Arrays.asList(restaurant.openingDays()).contains(orderDay);
        if (!isOpen) {
            return OrderValidationCode.RESTAURANT_CLOSED;
        }
        return null;
    }

    public LngLat getRestaurantLocation(Order order, List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            if (Arrays.stream(restaurant.menu())
                    .anyMatch(pizza -> Arrays.stream(order.getPizzasInOrder())
                            .anyMatch(orderPizza -> orderPizza.name().equals(pizza.name())))) {
                return restaurant.location();
            }
        }
        throw new IllegalArgumentException("No matching restaurant found for the order.");
    }
}
