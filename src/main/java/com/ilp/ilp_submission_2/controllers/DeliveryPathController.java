package com.ilp.ilp_submission_2.controllers;

import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.model.OrderValidation;
import com.ilp.ilp_submission_2.service.DeliveryPathService;
import com.ilp.ilp_submission_2.service.GeoJsonService;
import com.ilp.ilp_submission_2.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DeliveryPathController {

    private final DeliveryPathService deliveryPathService;
    private final OrderService orderService;
    private final GeoJsonService geoJsonService;

    public DeliveryPathController(DeliveryPathService deliveryPathService, OrderService orderService, GeoJsonService geoJsonService) {
        this.deliveryPathService = deliveryPathService;
        this.orderService = orderService;
        this.geoJsonService = geoJsonService;
    }

//    @PostMapping("/calcDeliveryPath")
//    public ResponseEntity<List<LngLat>> calculateDeliveryPath(@RequestBody Order order) {
//        try {
//
//            List<LngLat> path = deliveryPathService.calculatePath(order);
//            return ResponseEntity.ok(path);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }

    @PostMapping("/calcDeliveryPath")
    public ResponseEntity<?> calculateDeliveryPath(@RequestBody Order incomingOrder) {
        // Step 1: Validate the incoming order
        OrderValidation validation = orderService.validateIncomingOrder(incomingOrder);

        // Step 2: If validation fails, return 400 with consistent error details
        if (validation.orderStatus() == OrderStatus.INVALID) {
            // Construct a consistent error response using validation details
            return ResponseEntity.badRequest().body(Map.of(
                    "orderStatus", validation.orderStatus().toString(),
                    "orderValidationCode", validation.orderValidationCode().toString()
            ));
        }

        // Step 3: Calculate the delivery path if the order is valid
        List<LngLat> path = deliveryPathService.calculatePath(incomingOrder);

        // Step 4: Return the calculated path
        return ResponseEntity.ok(path);
    }



//    @PostMapping("/calcDeliveryPathAsGeoJson")
//    public ResponseEntity<String> calculateDeliveryPathAsGeoJson(@RequestBody Order order) {
//        try {
//            String geoJson = geoJsonService.calculateGeoJsonPath(order);
//            return ResponseEntity.ok(geoJson);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//    }
    @PostMapping("/calcDeliveryPathAsGeoJson")
    public ResponseEntity<?> calculateDeliveryPathAsGeoJson(@RequestBody Order order) {
        // Step 1: Validate the incoming order
        OrderValidation validation = orderService.validateIncomingOrder(order);

        // Step 2: If validation fails, return 400 with consistent error details
        if (validation.orderStatus() == OrderStatus.INVALID) {
            // Construct a consistent error response using validation details
            return ResponseEntity.badRequest().body(Map.of(
                    "orderStatus", validation.orderStatus().toString(),
                    "orderValidationCode", validation.orderValidationCode().toString()
            ));
        }

        // Step 3: Generate GeoJSON if the order is valid
        try {
            String geoJson = geoJsonService.calculateGeoJsonPath(order);
            return ResponseEntity.ok(geoJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
