package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.constant.ILPEndpoints;
import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.constant.SystemConstants;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Pizza;
import com.ilp.ilp_submission_2.data.Restaurant;
import com.ilp.ilp_submission_2.model.OrderValidation;
import com.ilp.ilp_submission_2.validators.OrderValidator;
import com.ilp.ilp_submission_2.validators.PizzaValidator;
import com.ilp.ilp_submission_2.validators.RestaurantValidator;
import com.ilp.ilp_submission_2.validators.CreditCardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final OrderValidator orderValidator;
    private final PizzaValidator pizzaValidator;
    private final RestaurantValidator restaurantValidator;
    private final CreditCardValidator creditCardValidator;

    @Autowired
    public OrderService(RestTemplate restTemplate,
                        OrderValidator orderValidator,
                        PizzaValidator pizzaValidator,
                        RestaurantValidator restaurantValidator,
                        CreditCardValidator creditCardValidator) {
        this.restTemplate = restTemplate;
        this.orderValidator = orderValidator;
        this.pizzaValidator = pizzaValidator;
        this.restaurantValidator = restaurantValidator;
        this.creditCardValidator = creditCardValidator;
    }

    /**
     * The "one-stop" method for validating an incoming order:
     * 1) Basic request-level checks (OrderValidator)
     * 2) Fetch all restaurants from ILPEndpoints.RESTAURANTS_URL
     * 3) Identify a single restaurant that all pizzas belong to
     * 4) Final validations (date >= now, pizza checks, credit card, etc.)
     */
    public OrderValidation validateIncomingOrder(Order incomingOrder) {
        // 1) Quick request-level checks
        ResponseEntity<?> problem = orderValidator.validateOrderRequest(incomingOrder);
        if (problem != null) {
            // Means the request was malformed => return minimal invalid result
            return new OrderValidation(null, null);
        }

        // 2) Fetch the restaurant array from external URL
        Restaurant[] restaurants = restTemplate.getForObject(
                ILPEndpoints.RESTAURANTS_URL,
                Restaurant[].class
        );
        if (restaurants == null || restaurants.length == 0) {
            // Can't proceed if we have no restaurants
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.UNDEFINED);
        }

        // 3) Determine which single restaurant (if any) covers all pizzas in this order
        Restaurant singleRestaurant = findSingleRestaurantForAllPizzas(incomingOrder, restaurants);
        if (singleRestaurant == null) {
            // Means either none or multiple restaurants for the pizzas
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        }

        // 4) Perform final validations with that restaurant
        return validateOrderWithSingleRestaurant(incomingOrder, singleRestaurant);
    }

    /**
     * Finds exactly one restaurant from 'restaurants' for all pizzas in the order.
     * Returns null if none or multiple restaurants are involved.
     */
    private Restaurant findSingleRestaurantForAllPizzas(Order order, Restaurant[] restaurants) {
        Restaurant single = null;
        for (Pizza pizza : order.getPizzasInOrder()) {
            Optional<Restaurant> match = findRestaurantByPizzaName(restaurants, pizza.name());
            if (match.isEmpty()) {
                // No or ambiguous match => fail
                return null;
            }
            if (single == null) {
                single = match.get();
            } else if (!single.name().equals(match.get().name())) {
                // A second pizza belongs to a different restaurant => multiple
                return null;
            }
        }
        return single;
    }

    /**
     * Finds the single restaurant that has a menu item matching 'pizzaName'.
     * If none or multiple restaurants match, returns Optional.empty().
     */
    private Optional<Restaurant> findRestaurantByPizzaName(Restaurant[] restaurants, String pizzaName) {
        Restaurant matched = null;
        for (Restaurant r : restaurants) {
            boolean found = Arrays.stream(r.menu())
                    .anyMatch(m -> m.name().equalsIgnoreCase(pizzaName));
            if (found) {
                if (matched == null) {
                    matched = r;
                } else {
                    // More than 1 => ambiguous
                    return Optional.empty();
                }
            }
        }
        return Optional.ofNullable(matched);
    }

    /**
     * The final validation step once we know exactly which Restaurant is used.
     * This is where we check date >= now, open days, pizza validations, credit card, etc.
     *
     * (This method is essentially your existing code that used to live in 'validateOrder(...)')
     */
    private OrderValidation validateOrderWithSingleRestaurant(Order order, Restaurant restaurant) {
        // Date check
        if (order.getOrderDate().isBefore(LocalDate.now())) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.UNDEFINED);
        }

        // Check if restaurant is open that day
        OrderValidationCode restCode = restaurantValidator.validateRestaurantOpen(
                restaurant, order.getOrderDate().getDayOfWeek()
        );
        if (restCode != null) {
            return new OrderValidation(OrderStatus.INVALID, restCode);
        }

        // Pizza checks
        OrderValidationCode arrayCode = pizzaValidator.validatePizzaArray(order.getPizzasInOrder());
        if (arrayCode != null) {
            return new OrderValidation(OrderStatus.INVALID, arrayCode);
        }
        OrderValidationCode countCode = pizzaValidator.validatePizzaCount(
                order.getPizzasInOrder(),
                SystemConstants.MAX_PIZZAS_PER_ORDER
        );
        if (countCode != null) {
            return new OrderValidation(OrderStatus.INVALID, countCode);
        }
        OrderValidationCode menuCode = pizzaValidator.validateMenuMatch(
                order.getPizzasInOrder(),
                restaurant.menu()
        );
        if (menuCode != null) {
            return new OrderValidation(OrderStatus.INVALID, menuCode);
        }

        // Check total price => either do direct logic or your 'OrderUtils' approach
        int sum = Arrays.stream(order.getPizzasInOrder())
                .mapToInt(Pizza::priceInPence)
                .sum();
        int expectedTotal = sum + SystemConstants.ORDER_CHARGE_IN_PENCE;
        if (order.getPriceTotalInPence() != expectedTotal) {
            return new OrderValidation(OrderStatus.INVALID, OrderValidationCode.TOTAL_INCORRECT);
        }

        // Credit card
        OrderValidationCode ccCode = creditCardValidator.validateCreditCard(order.getCreditCardInformation());
        if (ccCode != null) {
            return new OrderValidation(OrderStatus.INVALID, ccCode);
        }

        // All checks passed => valid
        return new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
    }
}
