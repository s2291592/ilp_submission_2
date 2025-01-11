package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.data.Restaurant;
import com.ilp.ilp_submission_2.utils.PathfindingUtils;
import com.ilp.ilp_submission_2.validators.DeliveryValidator;
import com.ilp.ilp_submission_2.validators.RestaurantValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DeliveryPathService {

    private final DeliveryValidator deliveryValidator;
    private final RestaurantValidator restaurantValidator;

    public DeliveryPathService(DeliveryValidator deliveryValidator, RestaurantValidator restaurantValidator) {
        this.deliveryValidator = deliveryValidator;
        this.restaurantValidator = restaurantValidator;
    }

    public List<LngLat> calculatePath(Order order) {
        // Validate the order
        deliveryValidator.validateOrder(order);

        // Fetch restaurants and no-fly zones
        List<Restaurant> restaurants = deliveryValidator.fetchRestaurants();
        Set<LngLat> noFlyZones = deliveryValidator.fetchNoFlyZones();

        // Get start and end points
        LngLat start = restaurantValidator.getRestaurantLocation(order, restaurants);
        LngLat end = deliveryValidator.getDeliveryLocation(order);

        // Calculate path using A* algorithm
        return PathfindingUtils.calculatePathAvoidingNoFlyZones(start, end, noFlyZones);
    }
}
