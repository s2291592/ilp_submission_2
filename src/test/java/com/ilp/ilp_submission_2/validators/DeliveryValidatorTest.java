package com.ilp.ilp_submission_2.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilp.ilp_submission_2.constant.ILPEndpoints;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Restaurant;
import com.ilp.ilp_submission_2.validators.DeliveryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DeliveryValidatorTest {

    private DeliveryValidator deliveryValidator;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        objectMapper = new ObjectMapper();
        deliveryValidator = new DeliveryValidator(restTemplate, objectMapper);
    }

    @Test
    void testValidateOrder_ValidOrder() {
        Order validOrder = new Order();
        validOrder.setPizzasInOrder(new com.ilp.ilp_submission_2.data.Pizza[]{
                new com.ilp.ilp_submission_2.data.Pizza("Pizza1", 1000)
        });

        assertDoesNotThrow(() -> deliveryValidator.validateOrder(validOrder));
    }

    @Test
    void testValidateOrder_InvalidOrder_Null() {
        assertThrows(IllegalArgumentException.class, () -> deliveryValidator.validateOrder(null));
    }

    @Test
    void testValidateOrder_InvalidOrder_EmptyPizzas() {
        Order invalidOrder = new Order();
        invalidOrder.setPizzasInOrder(new com.ilp.ilp_submission_2.data.Pizza[]{});

        assertThrows(IllegalArgumentException.class, () -> deliveryValidator.validateOrder(invalidOrder));
    }

    @Test
    void testFetchRestaurants_Success() throws Exception {
        String mockResponse = "[\n" +
                "  {\"name\": \"Civerinos Slice\", \"location\": {\"lng\": -3.19, \"lat\": 55.94}, \"menu\": [], \"openingDays\": [\"MONDAY\"]} \n" +
                "]";

        when(restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, String.class)).thenReturn(mockResponse);

        List<Restaurant> restaurants = deliveryValidator.fetchRestaurants();
        assertNotNull(restaurants);
        assertEquals(1, restaurants.size());
        assertEquals("Civerinos Slice", restaurants.get(0).name());
    }

    @Test
    void testFetchRestaurants_Failure() {
        when(restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, String.class)).thenReturn(null);

        assertThrows(RuntimeException.class, deliveryValidator::fetchRestaurants);
    }

    @Test
    void testFetchNoFlyZones_Success() throws Exception {
        String mockResponse = "[\n" +
                "  {\"name\": \"Zone1\", \"vertices\": [\n" +
                "    {\"lng\": -3.19, \"lat\": 55.94},\n" +
                "    {\"lng\": -3.18, \"lat\": 55.95}\n" +
                "  ]}\n" +
                "]";

        when(restTemplate.getForObject(ILPEndpoints.NO_FLY_ZONES_URL, String.class)).thenReturn(mockResponse);

        Set<List<LngLat>> noFlyZones = deliveryValidator.fetchNoFlyZones();
        assertNotNull(noFlyZones);
        assertEquals(1, noFlyZones.size());
        assertTrue(noFlyZones.iterator().next().contains(new LngLat(-3.19, 55.94)));
    }

    @Test
    void testFetchNoFlyZones_Failure_EmptyResponse() {
        when(restTemplate.getForObject(ILPEndpoints.NO_FLY_ZONES_URL, String.class)).thenReturn(null);

        assertThrows(RuntimeException.class, deliveryValidator::fetchNoFlyZones);
    }

    @Test
    void testGetDeliveryLocation() {
        Order order = new Order();
        LngLat deliveryLocation = deliveryValidator.getDeliveryLocation(order);
        assertNotNull(deliveryLocation);
        assertEquals(-3.186874, deliveryLocation.lng(), 1e-9);
        assertEquals(55.944494, deliveryLocation.lat(), 1e-9);
    }
}

