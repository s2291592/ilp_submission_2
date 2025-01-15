package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.Pizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PizzaValidatorTest {

    private PizzaValidator pizzaValidator;

    @BeforeEach
    void setUp() {
        pizzaValidator = new PizzaValidator();
    }

    @Test
    void testValidatePizzaArray_NullArray() {
        Pizza[] pizzas = null;
        OrderValidationCode result = pizzaValidator.validatePizzaArray(pizzas);
        assertEquals(OrderValidationCode.EMPTY_ORDER, result, "Null array should return EMPTY_ORDER");
    }

    @Test
    void testValidatePizzaArray_EmptyArray() {
        Pizza[] pizzas = new Pizza[0];
        OrderValidationCode result = pizzaValidator.validatePizzaArray(pizzas);
        assertEquals(OrderValidationCode.EMPTY_ORDER, result, "Empty array should return EMPTY_ORDER");
    }

    @Test
    void testValidatePizzaArray_InvalidPrice() {
        Pizza[] pizzas = {
                new Pizza("Margherita", -1),
                new Pizza("Pepperoni", 1000)
        };
        OrderValidationCode result = pizzaValidator.validatePizzaArray(pizzas);
        assertEquals(OrderValidationCode.PRICE_FOR_PIZZA_INVALID, result, "Pizza with invalid price should return PRICE_FOR_PIZZA_INVALID");
    }

    @Test
    void testValidatePizzaArray_ValidPizzas() {
        Pizza[] pizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200)
        };
        OrderValidationCode result = pizzaValidator.validatePizzaArray(pizzas);
        assertNull(result, "Valid pizzas should return null");
    }

    @Test
    void testValidatePizzaCount_ExceedsMax() {
        Pizza[] pizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200),
                new Pizza("Veggie", 1100)
        };
        int maxCount = 2;
        OrderValidationCode result = pizzaValidator.validatePizzaCount(pizzas, maxCount);
        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, result, "Exceeding max count should return MAX_PIZZA_COUNT_EXCEEDED");
    }

    @Test
    void testValidatePizzaCount_WithinMax() {
        Pizza[] pizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200)
        };
        int maxCount = 3;
        OrderValidationCode result = pizzaValidator.validatePizzaCount(pizzas, maxCount);
        assertNull(result, "Pizzas within max count should return null");
    }

    @Test
    void testValidateMenuMatch_MissingMenu() {
        Pizza[] orderPizzas = {
                new Pizza("Margherita", 1000)
        };
        Pizza[] menuPizzas = null;
        OrderValidationCode result = pizzaValidator.validateMenuMatch(orderPizzas, menuPizzas);
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, result, "Missing menu should return PIZZA_NOT_DEFINED");
    }

    @Test
    void testValidateMenuMatch_PizzaNotOnMenu() {
        Pizza[] orderPizzas = {
                new Pizza("Nonexistent", 1000)
        };
        Pizza[] menuPizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200)
        };
        OrderValidationCode result = pizzaValidator.validateMenuMatch(orderPizzas, menuPizzas);
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, result, "Pizza not on menu should return PIZZA_NOT_DEFINED");
    }

    @Test
    void testValidateMenuMatch_PriceMismatch() {
        Pizza[] orderPizzas = {
                new Pizza("Margherita", 1100)
        };
        Pizza[] menuPizzas = {
                new Pizza("Margherita", 1000)
        };
        OrderValidationCode result = pizzaValidator.validateMenuMatch(orderPizzas, menuPizzas);
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, result, "Price mismatch should return PIZZA_NOT_DEFINED");
    }

    @Test
    void testValidateMenuMatch_ValidMenuMatch() {
        Pizza[] orderPizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200)
        };
        Pizza[] menuPizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200)
        };
        OrderValidationCode result = pizzaValidator.validateMenuMatch(orderPizzas, menuPizzas);
        assertNull(result, "Valid menu match should return null");
    }
}
