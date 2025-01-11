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
        if (order.getOrderDate() == null) {
            return ResponseEntity.badRequest().body("Order date is missing");
        }
        // Possibly more checks...

        // If everything is structurally fine, return null to indicate "OK to proceed"
        return null;
    }
}
