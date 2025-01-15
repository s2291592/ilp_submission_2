package com.ilp.ilp_submission_2.controllers;

import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.model.OrderValidation;
import com.ilp.ilp_submission_2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ValidateOrderPostController {

    private final OrderService orderService;

    @Autowired
    public ValidateOrderPostController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/validateOrder")
    public ResponseEntity<OrderValidation> validateOrder(@RequestBody Order incomingOrder) {
        // Just pass the Order to a single service method
        OrderValidation result = orderService.validateIncomingOrder(incomingOrder);
        // Return 200 with the result
        return ResponseEntity.ok(result);
    }
}
