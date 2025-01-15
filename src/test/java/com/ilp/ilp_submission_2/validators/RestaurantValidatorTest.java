package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Pizza;
import com.ilp.ilp_submission_2.data.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantValidatorTest {

    private RestaurantValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RestaurantValidator();
    }

    @Test
    void testValidateRestaurantOpen_Open() {
        Restaurant restaurant = new Restaurant("Pizza Place",
                new LngLat(-3.192473, 55.946233),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY},
                new Pizza[]{new Pizza("Margherita", 1000)});

        OrderValidationCode result = validator.validateRestaurantOpen(restaurant, DayOfWeek.MONDAY);
        assertNull(result, "Restaurant open on Monday should return null");
    }

    @Test
    void testValidateRestaurantOpen_Closed() {
        Restaurant restaurant = new Restaurant("Pizza Place",
                new LngLat(-3.192473, 55.946233),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY},
                new Pizza[]{new Pizza("Margherita", 1000)});

        OrderValidationCode result = validator.validateRestaurantOpen(restaurant, DayOfWeek.FRIDAY);
        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, result, "Restaurant closed on Friday should return RESTAURANT_CLOSED");
    }

    @Test
    void testValidateRestaurantOpen_NullOpeningDays() {
        Restaurant restaurant = new Restaurant("Pizza Place",
                new LngLat(-3.192473, 55.946233),
                null,
                new Pizza[]{new Pizza("Margherita", 1000)});

        OrderValidationCode result = validator.validateRestaurantOpen(restaurant, DayOfWeek.MONDAY);
        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, result, "Restaurant with null opening days should return RESTAURANT_CLOSED");
    }

    @Test
    void testGetRestaurantLocation_ValidMatch() {
        Restaurant restaurant1 = new Restaurant("Pizza Place A",
                new LngLat(-3.192473, 55.946233),
                new DayOfWeek[]{DayOfWeek.MONDAY},
                new Pizza[]{new Pizza("Margherita", 1000)});
        Restaurant restaurant2 = new Restaurant("Pizza Place B",
                new LngLat(-3.185000, 55.945000),
                new DayOfWeek[]{DayOfWeek.FRIDAY},
                new Pizza[]{new Pizza("Pepperoni", 1200)});

        Order order = new Order();
        order.setPizzasInOrder(new Pizza[]{new Pizza("Pepperoni", 1200)});

        LngLat location = validator.getRestaurantLocation(order, List.of(restaurant1, restaurant2));
        assertEquals(new LngLat(-3.185000, 55.945000), location, "Location should match the restaurant serving 'Pepperoni'");
    }

    @Test
    void testGetRestaurantLocation_NoMatch() {
        Restaurant restaurant1 = new Restaurant("Pizza Place A",
                new LngLat(-3.192473, 55.946233),
                new DayOfWeek[]{DayOfWeek.MONDAY},
                new Pizza[]{new Pizza("Margherita", 1000)});
        Restaurant restaurant2 = new Restaurant("Pizza Place B",
                new LngLat(-3.185000, 55.945000),
                new DayOfWeek[]{DayOfWeek.FRIDAY},
                new Pizza[]{new Pizza("Veggie", 1100)});

        Order order = new Order();
        order.setPizzasInOrder(new Pizza[]{new Pizza("Pepperoni", 1200)});

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                validator.getRestaurantLocation(order, List.of(restaurant1, restaurant2)));
        assertEquals("No matching restaurant found for the order.", exception.getMessage());
    }
}
