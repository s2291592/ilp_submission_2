package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceUtilsTest {

    @Test
    void testCalculateEuclideanDistance() {
        // Same points
        double distance = DistanceUtils.calculateEuclideanDistance(55.944, -3.187, 55.944, -3.187);
        assertEquals(0.0, distance, 1e-9);

        // Different points
        distance = DistanceUtils.calculateEuclideanDistance(55.944, -3.187, 55.945, -3.188);
        assertTrue(distance > 0);
        assertEquals(0.001414, distance, 1e-6); // √((0.001)² + (0.001)²)
    }

    @Test
    void testIsWithinThreshold() {
        // Points within threshold
        assertTrue(DistanceUtils.isWithinThreshold(55.944, -3.187, 55.94401, -3.18701));

        // Points on the threshold
        assertTrue(DistanceUtils.isWithinThreshold(55.944, -3.187, 55.944 + 0.00015, -3.187));

        // Points outside the threshold
        assertFalse(DistanceUtils.isWithinThreshold(55.944, -3.187, 55.945, -3.188));
    }

    @Test
    void testGetPosition() {
        // Test movement eastward
        LngLat start = new LngLat(0.0, 0.0);
        LngLat newPosition = DistanceUtils.getPosition(90.0, start);
        assertEquals(0.00015, newPosition.lng(), 1e-9, "Longitude should increase eastward");
        assertEquals(0.0, newPosition.lat(), 1e-9, "Latitude should remain unchanged");

        // Test movement northward
        newPosition = DistanceUtils.getPosition(0.0, start);
        assertEquals(0.0, newPosition.lng(), 1e-9, "Longitude should remain unchanged");
        assertEquals(0.00015, newPosition.lat(), 1e-9, "Latitude should increase northward");

        // Test crossing the international date line eastward
        start = new LngLat(179.9999, 0.0);
        newPosition = DistanceUtils.getPosition(90.0, start);
        assertTrue(newPosition.lng() < -179.999, "Longitude should wrap to the negative range");
        assertEquals(0.0, newPosition.lat(), 1e-9, "Latitude should remain unchanged");

        // Test crossing the international date line westward
        start = new LngLat(-179.9999, 0.0);
        newPosition = DistanceUtils.getPosition(270.0, start);
        assertTrue(newPosition.lng() > 179.999, "Longitude should wrap to the positive range");
        assertEquals(0.0, newPosition.lat(), 1e-9, "Latitude should remain unchanged");

        // Test movement southward
        newPosition = DistanceUtils.getPosition(180.0, new LngLat(0.0, 0.0));
        assertEquals(0.0, newPosition.lng(), 1e-9, "Longitude should remain unchanged");
        assertEquals(-0.00015, newPosition.lat(), 1e-9, "Latitude should decrease southward");

        // Test movement westward
        newPosition = DistanceUtils.getPosition(270.0, new LngLat(0.0, 0.0));
        assertEquals(-0.00015, newPosition.lng(), 1e-9, "Longitude should decrease westward");
        assertEquals(0.0, newPosition.lat(), 1e-9, "Latitude should remain unchanged");
    }

}
