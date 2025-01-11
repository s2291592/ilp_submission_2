package com.ilp.ilp_submission_2.controllers;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.service.DeliveryPathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeliveryPathController {

    private final DeliveryPathService deliveryPathService;

    public DeliveryPathController(DeliveryPathService deliveryPathService) {
        this.deliveryPathService = deliveryPathService;
    }

    @PostMapping("/calcDeliveryPath")
    public ResponseEntity<List<LngLat>> calculateDeliveryPath(@RequestBody Order order) {
        try {
            List<LngLat> path = deliveryPathService.calculatePath(order);
            return ResponseEntity.ok(path); // Return the path as JSON
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Return 400 for invalid input
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Return 500 for server errors
        }
    }
}
