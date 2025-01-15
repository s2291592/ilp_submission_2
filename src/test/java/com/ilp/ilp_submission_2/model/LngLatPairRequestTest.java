package com.ilp.ilp_submission_2.model;

import com.ilp.ilp_submission_2.data.LngLat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LngLatPairRequestTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        LngLat position1 = new LngLat(-3.18, 55.94);
        LngLat position2 = new LngLat(-3.19, 55.95);

        // Act
        LngLatPairRequest request = new LngLatPairRequest(position1, position2);

        // Assert
        assertEquals(position1, request.getPosition1(), "Position1 should match the value set in constructor");
        assertEquals(position2, request.getPosition2(), "Position2 should match the value set in constructor");
    }

    @Test
    void testSetters() {
        // Arrange
        LngLat initialPosition1 = new LngLat(-3.18, 55.94);
        LngLat initialPosition2 = new LngLat(-3.19, 55.95);
        LngLatPairRequest request = new LngLatPairRequest(initialPosition1, initialPosition2);

        LngLat newPosition1 = new LngLat(-3.20, 55.96);
        LngLat newPosition2 = new LngLat(-3.21, 55.97);

        // Act
        request.setPosition1(newPosition1);
        request.setPosition2(newPosition2);

        // Assert
        assertEquals(newPosition1, request.getPosition1(), "Position1 should match the updated value");
        assertEquals(newPosition2, request.getPosition2(), "Position2 should match the updated value");
    }

    @Test
    void testNullValues() {
        // Act
        LngLatPairRequest request = new LngLatPairRequest(null, null);

        // Assert
        assertNull(request.getPosition1(), "Position1 should be null when set to null in constructor");
        assertNull(request.getPosition2(), "Position2 should be null when set to null in constructor");

        // Update to null via setters
        request.setPosition1(null);
        request.setPosition2(null);

        assertNull(request.getPosition1(), "Position1 should be null after setter");
        assertNull(request.getPosition2(), "Position2 should be null after setter");
    }
}
