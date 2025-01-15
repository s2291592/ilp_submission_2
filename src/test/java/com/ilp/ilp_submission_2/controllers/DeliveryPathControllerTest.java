package com.ilp.ilp_submission_2.controllers;

import com.ilp.ilp_submission_2.constant.OrderStatus;
import com.ilp.ilp_submission_2.constant.OrderValidationCode;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.model.OrderValidation;
import com.ilp.ilp_submission_2.service.DeliveryPathService;
import com.ilp.ilp_submission_2.service.GeoJsonService;
import com.ilp.ilp_submission_2.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryPathControllerTest {

    @Mock
    private DeliveryPathService deliveryPathService;

    @Mock
    private OrderService orderService;

    @Mock
    private GeoJsonService geoJsonService;

    @InjectMocks
    private DeliveryPathController deliveryPathController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateDeliveryPath_ValidOrder() {
        // Mock valid order validation
        OrderValidation validation = new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
        when(orderService.validateIncomingOrder(any(Order.class))).thenReturn(validation);

        // Mock path calculation
        List<LngLat> mockPath = List.of(new LngLat(-3.190286, 55.945535));
        when(deliveryPathService.calculatePath(any(Order.class))).thenReturn(mockPath);

        // Create a valid order
        Order validOrder = new Order(); // Populate with valid data

        // Call the API
        ResponseEntity<?> response = deliveryPathController.calculateDeliveryPath(validOrder);

        // Assert success response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPath, response.getBody());
    }

    @Test
    void testCalculateDeliveryPath_InvalidOrder() {
        // Mock invalid order validation
        OrderValidation validation = new OrderValidation(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        when(orderService.validateIncomingOrder(any(Order.class))).thenReturn(validation);

        // Create an invalid order
        Order invalidOrder = new Order(); // Populate with invalid data

        // Call the API
        ResponseEntity<?> response = deliveryPathController.calculateDeliveryPath(invalidOrder);

        // Assert 400 response
        assertEquals(400, response.getStatusCodeValue());

        // Assert error details
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertEquals("INVALID", error.get("orderStatus"));
        assertEquals("PIZZA_FROM_MULTIPLE_RESTAURANTS", error.get("orderValidationCode"));
    }

//    @Test
//    void testCalculateDeliveryPath_Exception() {
//        // Mock exception during validation
//        when(orderService.validateIncomingOrder(any(Order.class))).thenThrow(new RuntimeException("Unexpected error"));
//
//        // Create a valid order
//        Order validOrder = new Order(); // Populate with valid data
//
//        // Call the API
//        ResponseEntity<?> response = deliveryPathController.calculateDeliveryPath(validOrder);
//
//        // Assert 500 response
//        assertEquals(500, response.getStatusCodeValue());
//    }

    @Test
    void testCalculateDeliveryPathAsGeoJson_ValidOrder() {
        // Mock valid order validation
        OrderValidation validation = new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
        when(orderService.validateIncomingOrder(any(Order.class))).thenReturn(validation);

        // Mock GeoJSON generation
        String mockGeoJson = "{\"type\": \"Feature\", \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[-3.190286, 55.945535]]}}";
        when(geoJsonService.calculateGeoJsonPath(any(Order.class))).thenReturn(mockGeoJson);

        // Create a valid order
        Order validOrder = new Order(); // Populate with valid data

        // Call the API
        ResponseEntity<?> response = deliveryPathController.calculateDeliveryPathAsGeoJson(validOrder);

        // Assert success response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockGeoJson, response.getBody());
    }

    @Test
    void testCalculateDeliveryPathAsGeoJson_InvalidOrder() {
        // Mock invalid order validation
        OrderValidation validation = new OrderValidation(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        when(orderService.validateIncomingOrder(any(Order.class))).thenReturn(validation);

        // Create an invalid order
        Order invalidOrder = new Order(); // Populate with invalid data

        // Call the API
        ResponseEntity<?> response = deliveryPathController.calculateDeliveryPathAsGeoJson(invalidOrder);

        // Assert 400 response
        assertEquals(400, response.getStatusCodeValue());

        // Assert error details
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertEquals("INVALID", error.get("orderStatus"));
        assertEquals("PIZZA_FROM_MULTIPLE_RESTAURANTS", error.get("orderValidationCode"));
    }

    @Test
    void testCalculateDeliveryPathAsGeoJson_Exception() {
        // Mock exception during GeoJSON generation
        when(orderService.validateIncomingOrder(any(Order.class))).thenReturn(new OrderValidation(OrderStatus.VALID, OrderValidationCode.NO_ERROR));
        when(geoJsonService.calculateGeoJsonPath(any(Order.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Create a valid order
        Order validOrder = new Order(); // Populate with valid data

        // Call the API
        ResponseEntity<?> response = deliveryPathController.calculateDeliveryPathAsGeoJson(validOrder);

        // Assert 500 response
        assertEquals(500, response.getStatusCodeValue());
    }
}
