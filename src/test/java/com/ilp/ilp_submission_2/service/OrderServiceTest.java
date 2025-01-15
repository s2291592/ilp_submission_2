package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.constant.ILPEndpoints;
import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.constant.SystemConstants;
import com.ilp.ilp_submission_2.data.*;
import com.ilp.ilp_submission_2.model.OrderValidation;
import com.ilp.ilp_submission_2.validators.CreditCardValidator;
import com.ilp.ilp_submission_2.validators.OrderValidator;
import com.ilp.ilp_submission_2.validators.PizzaValidator;
import com.ilp.ilp_submission_2.validators.RestaurantValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private PizzaValidator pizzaValidator;
    @Mock
    private RestaurantValidator restaurantValidator;
    @Mock
    private CreditCardValidator creditCardValidator;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(restTemplate, orderValidator, pizzaValidator, restaurantValidator, creditCardValidator);
    }

    @Test
    void testValidateIncomingOrder_ValidOrder() {
        // Arrange
        Order validOrder = createValidOrder();
        Restaurant[] restaurants = {create3Restaurant()};

        when(restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, Restaurant[].class)).thenReturn(restaurants);
        when(pizzaValidator.validatePizzaArray(validOrder.getPizzasInOrder())).thenReturn(null);
        when(pizzaValidator.validatePizzaCount(validOrder.getPizzasInOrder(), SystemConstants.MAX_PIZZAS_PER_ORDER)).thenReturn(null);
        when(pizzaValidator.validateMenuMatch(validOrder.getPizzasInOrder(), restaurants[0].menu())).thenReturn(null);
        when(restaurantValidator.validateRestaurantOpen(restaurants[0], validOrder.getOrderDate().getDayOfWeek())).thenReturn(null);
        when(creditCardValidator.validateCreditCard(validOrder.getCreditCardInformation())).thenReturn(null);

        // Act
        OrderValidation result = orderService.validateIncomingOrder(validOrder);

//        System.out.println("Order Total Price (validOrder): " + validOrder.getPriceTotalInPence());
//        System.out.println("Pizza Prices (validOrder): " +
//                Arrays.stream(validOrder.getPizzasInOrder())
//                        .mapToInt(Pizza::priceInPence)
//                        .sum());
//        System.out.println("Expected Total: " +
//                (Arrays.stream(validOrder.getPizzasInOrder())
//                        .mapToInt(Pizza::priceInPence)
//                        .sum() + SystemConstants.ORDER_CHARGE_IN_PENCE));
//        System.out.println("Validation Result: " + result);


        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.VALID, result.orderStatus());
        assertEquals(OrderValidationCode.NO_ERROR, result.orderValidationCode());
    }



    @Test
    void testValidateIncomingOrder_InvalidOrderRequest() {
        // Arrange
        Order invalidOrder = new Order(); // Missing required fields
        when(orderValidator.validateOrderRequest(invalidOrder)).thenReturn(ResponseEntity.badRequest().build());

        // Act
        OrderValidation result = orderService.validateIncomingOrder(invalidOrder);

        // Assert
        assertNotNull(result);
        assertNull(result.orderStatus());
        assertNull(result.orderValidationCode());
    }

    @Test
    void testValidateIncomingOrder_NoRestaurants() {
        // Arrange
        Order validOrder = createValidOrder();
        when(restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, Restaurant[].class)).thenReturn(null);

        // Act
        OrderValidation result = orderService.validateIncomingOrder(validOrder);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.INVALID, result.orderStatus());
        assertEquals(OrderValidationCode.UNDEFINED, result.orderValidationCode());
    }

    @Test
    void testValidateIncomingOrder_MultipleRestaurantsForPizzas() {
        // Arrange
        Order validOrder = createInvalidOrder();
        Restaurant[] restaurants = {
                create1Restaurant(),
                create2Restaurant()
        };

        when(restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, Restaurant[].class)).thenReturn(restaurants);

        // Act
        OrderValidation result = orderService.validateIncomingOrder(validOrder);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.INVALID, result.orderStatus());
        assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, result.orderValidationCode());
    }

    @Test
    void testValidateIncomingOrder_InvalidPizzaCount() {
        // Arrange
        Order validOrder = createValidOrder();
        Restaurant[] restaurants = {create1Restaurant(), create2Restaurant(), create3Restaurant()};

        when(restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, Restaurant[].class)).thenReturn(restaurants);
        when(pizzaValidator.validatePizzaCount(validOrder.getPizzasInOrder(), SystemConstants.MAX_PIZZAS_PER_ORDER))
                .thenReturn(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);

        // Act
        OrderValidation result = orderService.validateIncomingOrder(validOrder);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.INVALID, result.orderStatus());
        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, result.orderValidationCode());
    }

    // Helper methods for creating mock data
//    private Order createValidOrder() {
//        Pizza pizza1 = new Pizza("Margherita", 1200); // Price: 1200 pence
//        Pizza pizza2 = new Pizza("Pepperoni", 1500); // Price: 1500 pence
//        Pizza[] pizzas = {pizza1, pizza2};
//
//        int sumOfPizzaPrices = Arrays.stream(pizzas)
//                .mapToInt(Pizza::priceInPence)
//                .sum();
//
//        int totalPrice = sumOfPizzaPrices + SystemConstants.ORDER_CHARGE_IN_PENCE;
//
//        return new Order("12345", LocalDate.now(), pizzas, null, totalPrice, null);
//    }
    private Order createValidOrder() {
        Pizza pizza1 = new Pizza("Margherita", 1200); // Price: 1200 pence
        Pizza pizza2 = new Pizza("Pepperoni", 1500); // Price: 1500 pence
        Pizza[] pizzas = {pizza1, pizza2};

        int sumOfPizzaPrices = Arrays.stream(pizzas)
                .mapToInt(Pizza::priceInPence)
                .sum();

        int totalPrice = sumOfPizzaPrices + SystemConstants.ORDER_CHARGE_IN_PENCE;

        Order order = new Order("12345", LocalDate.now(), totalPrice, pizzas, null);
//        System.out.println("Order Total Price after creation: " + order.getPriceTotalInPence()); // 打印 2800

        return order;
    }


    private Order createInvalidOrder() {
        Pizza pizza1 = new Pizza("Margherita", 1200); // Price: 1200 pence
        Pizza pizza2 = new Pizza("Reg", 1500); // Price: 1500 pence
        Pizza[] pizzas = {pizza1, pizza2};

        int sumOfPizzaPrices = Arrays.stream(pizzas)
                .mapToInt(Pizza::priceInPence)
                .sum();

        int totalPrice = sumOfPizzaPrices + SystemConstants.ORDER_CHARGE_IN_PENCE;

        return new Order("12345", LocalDate.now(), pizzas, null, totalPrice, null);
    }



    private Restaurant create1Restaurant() {
        Pizza pizza = new Pizza("Reg", 1200);
        return new Restaurant("PizzaPlace", new LngLat((double) 0, (double) 0), new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY}, new Pizza[]{pizza});
    }

    private Restaurant create2Restaurant() {
        Pizza pizza = new Pizza("Nam", 1500);
        return new Restaurant("AnotherPizzaPlace", new LngLat(1.0, 1.0), new DayOfWeek[]{DayOfWeek.WEDNESDAY}, new Pizza[]{pizza});
    }

    private Restaurant create3Restaurant() {
        Pizza pizza1 = new Pizza("Margherita", 1200);
        Pizza pizza2 = new Pizza("Pepperoni", 1500);
        return new Restaurant("PizzaPlace", new LngLat((double) 0, (double) 0),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY},
                new Pizza[]{pizza1, pizza2});
    }


}
