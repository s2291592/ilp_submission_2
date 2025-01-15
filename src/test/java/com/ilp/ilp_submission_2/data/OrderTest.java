package com.ilp.ilp_submission_2.data;

import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testFullConstructor() {
        Pizza[] pizzas = {
                new Pizza("Margherita", 1000),
                new Pizza("Pepperoni", 1200)
        };
        CreditCardInformation ccInfo = new CreditCardInformation("1234567812345678", "12/25", "123");
        Order order = new Order("12345", LocalDate.now(), OrderStatus.VALID, OrderValidationCode.NO_ERROR, 2200, pizzas, ccInfo);

        assertEquals("12345", order.getOrderNo());
        assertEquals(LocalDate.now(), order.getOrderDate());
        assertEquals(OrderStatus.VALID, order.getOrderStatus());
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
        assertEquals(2200, order.getPriceTotalInPence());
        assertArrayEquals(pizzas, order.getPizzasInOrder());
        assertEquals(ccInfo, order.getCreditCardInformation());
    }

    @Test
    void testPartialConstructor() {
        Pizza[] pizzas = {
                new Pizza("Veggie", 1500)
        };
        CreditCardInformation ccInfo = new CreditCardInformation("9876543210987654", "11/24", "321");
        Order order = new Order("54321", LocalDate.now().plusDays(1), 1600, pizzas, ccInfo);

        assertEquals("54321", order.getOrderNo());
        assertEquals(LocalDate.now().plusDays(1), order.getOrderDate());
        assertEquals(1600, order.getPriceTotalInPence());
        assertArrayEquals(pizzas, order.getPizzasInOrder());
        assertEquals(ccInfo, order.getCreditCardInformation());
        assertEquals(OrderStatus.UNDEFINED, order.getOrderStatus(), "Default order status should be UNDEFINED");
        assertEquals(OrderValidationCode.UNDEFINED, order.getOrderValidationCode(), "Default validation code should be UNDEFINED");
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order();
        order.setOrderNo("12345");
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.INVALID);
        order.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
        order.setPriceTotalInPence(500);
        Pizza[] pizzas = {
                new Pizza("BBQ Chicken", 1500)
        };
        order.setPizzasInOrder(pizzas);
        CreditCardInformation ccInfo = new CreditCardInformation("1111222233334444", "01/26", "456");
        order.setCreditCardInformation(ccInfo);

        assertEquals("12345", order.getOrderNo());
        assertEquals(LocalDate.now(), order.getOrderDate());
        assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
        assertEquals(500, order.getPriceTotalInPence());
        assertArrayEquals(pizzas, order.getPizzasInOrder());
        assertEquals(ccInfo, order.getCreditCardInformation());
    }

    @Test
    void testHashCode() {
        Pizza[] pizzas = {
                new Pizza("Hawaiian", 1800)
        };
        Order order1 = new Order("11111", LocalDate.now(), 1900, pizzas, null);
        Order order2 = new Order("11111", LocalDate.now(), 1900, pizzas, null);

        assertEquals(order1.hashCode(), order2.hashCode(), "Orders with the same data should have the same hash code");
    }

    @Test
    void testOrderFieldEquality() {
        Pizza[] pizzas = {
                new Pizza("Hawaiian", 1800)
        };

        Order order1 = new Order("11111", LocalDate.now(), 1900, pizzas, null);
        Order order2 = new Order("11111", LocalDate.now(), 1900, pizzas, null);

        // Compare fields manually
        assertEquals(order1.getOrderNo(), order2.getOrderNo(), "Order numbers should be equal");
        assertEquals(order1.getOrderDate(), order2.getOrderDate(), "Order dates should be equal");
        assertEquals(order1.getPriceTotalInPence(), order2.getPriceTotalInPence(), "Order total price should be equal");
        assertArrayEquals(order1.getPizzasInOrder(), order2.getPizzasInOrder(), "Pizzas in the orders should be equal");
        assertEquals(order1.getCreditCardInformation(), order2.getCreditCardInformation(), "Credit card information should be equal");
    }


    @Test
    void testDefaultConstructor() {
        Order order = new Order();

        assertEquals("", order.getOrderNo(), "Default order number should be an empty string");
        assertEquals(LocalDate.MIN, order.getOrderDate(), "Default order date should be LocalDate.MIN");
        assertEquals(OrderStatus.UNDEFINED, order.getOrderStatus(), "Default order status should be UNDEFINED");
        assertEquals(OrderValidationCode.UNDEFINED, order.getOrderValidationCode(), "Default validation code should be UNDEFINED");
        assertEquals(0, order.getPriceTotalInPence(), "Default price should be 0");
        assertArrayEquals(new Pizza[0], order.getPizzasInOrder(), "Default pizzas array should be empty");
        assertNull(order.getCreditCardInformation(), "Default credit card info should be null");
    }
}
