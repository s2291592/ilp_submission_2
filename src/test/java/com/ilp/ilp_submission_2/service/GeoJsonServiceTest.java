package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Order;
import com.ilp.ilp_submission_2.utils.GeoJsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeoJsonServiceTest {

    @Mock
    private DeliveryPathService deliveryPathService;

    @InjectMocks
    private GeoJsonService geoJsonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateGeoJsonPath_ValidOrder() {
        Order order = mock(Order.class);
        List<LngLat> path = Arrays.asList(
                new LngLat(-3.186874, 55.944494),
                new LngLat(-3.187874, 55.945494)
        );
        when(deliveryPathService.calculatePath(order)).thenReturn(path);

        String result = geoJsonService.calculateGeoJsonPath(order);
        String expected = "{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":["
                + "[-3.186874,55.944494],[-3.187874,55.945494]"
                + "]}}";

        assertNotNull(result, "GeoJSON result should not be null for a valid order");
        assertEquals(expected, result, "GeoJSON output should match expected output");
    }


    @Test
    void testCalculateGeoJsonPath_EmptyPath() {
        Order order = mock(Order.class);
        when(deliveryPathService.calculatePath(order)).thenReturn(Collections.emptyList());

        String result = geoJsonService.calculateGeoJsonPath(order);
        String expected = "{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[]}}";

        assertNotNull(result, "GeoJSON result should not be null for an empty path");
        assertEquals(expected, result, "GeoJSON output for an empty path should match expected output");
    }

}
