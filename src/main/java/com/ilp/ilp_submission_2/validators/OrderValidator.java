package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Basic request-level checks for an Order JSON
 * (e.g., no null fields that break the request).
 * If invalid at this level, we might return a 400 directly from the controller.
 */
@Component
public class OrderValidator {

    public ResponseEntity<?> validateOrderRequest(Order order) {
        if (order == null) {
            return ResponseEntity.badRequest().body("Order is null");
        }
        if (order.getOrderNo() == null || order.getOrderNo().isBlank()) {
            return ResponseEntity.badRequest().body("Order number is missing");
        }
        if (order.getOrderDate() == null || order.getOrderDate().toString().equals("-999999999-01-01")) {
            // Properly handle and return the error for a missing OrderDate
            return ResponseEntity.badRequest().body("Order date is missing");
        }

        // If everything is fine, return null to indicate validation success
        return null;
    }
}