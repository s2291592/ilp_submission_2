package com.ilp.ilp_submission_2.validators;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilp.ilp_submission_2.constant.ILPEndpoints;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.NamedRegion;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Restaurant;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DeliveryValidator {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DeliveryValidator(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void validateOrder(Order order) {
        if (order == null || order.getPizzasInOrder() == null || order.getPizzasInOrder().length == 0) {
            throw new IllegalArgumentException("Invalid order: missing or empty pizzas.");
        }
    }

    public List<Restaurant> fetchRestaurants() {
        try {
            String response = restTemplate.getForObject(ILPEndpoints.RESTAURANTS_URL, String.class);
            return objectMapper.readValue(response, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch restaurants", e);
        }
    }

    public Set<List<LngLat>> fetchNoFlyZones() {
        try {
            String response = restTemplate.getForObject(ILPEndpoints.NO_FLY_ZONES_URL, String.class);

            // Verify the response
            if (response == null || response.trim().isEmpty()) {
                throw new RuntimeException("No-fly zones data is empty or null.");
            }

            // Deserialize JSON into a list of NamedRegion
            ObjectMapper objectMapper = new ObjectMapper();
            List<NamedRegion> noFlyZones = objectMapper.readValue(response, new TypeReference<>() {});

            // Convert NamedRegion to Set<List<LngLat>>
            Set<List<LngLat>> noFlyZonePolygons = new HashSet<>();
            for (NamedRegion region : noFlyZones) {
                noFlyZonePolygons.add(region.vertices());
            }

            return noFlyZonePolygons;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch no-fly zones", e);
        }
    }


    public LngLat getDeliveryLocation(Order order) {
        return new LngLat(-3.186874, 55.944494);
    }
}
