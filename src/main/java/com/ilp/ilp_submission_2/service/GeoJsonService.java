package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.utils.GeoJsonUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoJsonService {

    private final DeliveryPathService deliveryPathService;

    public GeoJsonService(DeliveryPathService deliveryPathService) {
        this.deliveryPathService = deliveryPathService;
    }

    public String calculateGeoJsonPath(Order order) {
        List<LngLat> path = deliveryPathService.calculatePath(order);
        return GeoJsonUtils.convertPathToGeoJson(path);
    }
}
