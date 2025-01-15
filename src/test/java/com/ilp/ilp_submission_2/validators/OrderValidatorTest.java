package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private final OrderValidator validator = new OrderValidator();

    @Test
    void testValidateOrderRequest_NullOrder() {
        // Test case where the order is null
        ResponseEntity<?> response = validator.validateOrderRequest(null);
        assertNotNull(response, "Response should not be null for a null order");
        assertEquals(400, response.getStatusCodeValue(), "Status code should be 400");
        assertEquals("Order is null", response.getBody(), "Response body should indicate null order");
    }

    @Test
    void testValidateOrderRequest_MissingOrderNumber() {
        // Test case where order number is missing
        Order order = new Order();
        order.setOrderDate(LocalDate.now()); // Valid date

        ResponseEntity<?> response = validator.validateOrderRequest(order);
        assertNotNull(response, "Response should not be null for a missing order number");
        assertEquals(400, response.getStatusCodeValue(), "Status code should be 400");
        assertEquals("Order number is missing", response.getBody(), "Response body should indicate missing order number");
    }

    @Test
    void testValidateOrderRequest_MissingOrderDate() {
        // Test case where order date is missing
        Order order = new Order();
        order.setOrderNo("12345"); // Valid order number

        ResponseEntity<?> response = validator.validateOrderRequest(order);

        // Assertions
        assertNotNull(response, "Response should not be null for a missing order date");
        assertEquals(400, response.getStatusCodeValue(), "Status code should be 400");
        assertEquals("Order date is missing", response.getBody(), "Response body should indicate missing order date");
    }


    @Test
    void testValidateOrderRequest_ValidOrder() {
        // Test case where the order is fully valid
        Order order = new Order();
        order.setOrderNo("12345");
        order.setOrderDate(LocalDate.now());

        ResponseEntity<?> response = validator.validateOrderRequest(order);
        assertNull(response, "Response should be null for a valid order");
    }
}

