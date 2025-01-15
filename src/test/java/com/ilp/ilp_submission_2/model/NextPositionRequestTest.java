package com.ilp.ilp_submission_2.model;

import com.ilp.ilp_submission_2.data.LngLat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NextPositionRequestTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        LngLat start = new LngLat(45.0, -90.0);
        Double angle = 45.0;

        // Act
        NextPositionRequest request = new NextPositionRequest(start, angle);

        // Assert
        assertEquals(start, request.getStart(), "Start position should match the constructor value");
        assertEquals(angle, request.getAngle(), "Angle should match the constructor value");
    }

    @Test
    void testSetStart() {
        // Arrange
        NextPositionRequest request = new NextPositionRequest(new LngLat(0.0, 0.0), 0.0);
        LngLat newStart = new LngLat(50.0, -100.0);

        // Act
        request.setStart(newStart);

        // Assert
        assertEquals(newStart, request.getStart(), "Start position should be updated correctly");
    }

    @Test
    void testSetAngle() {
        // Arrange
        NextPositionRequest request = new NextPositionRequest(new LngLat(0.0, 0.0), 0.0);
        Double newAngle = 90.0;

        // Act
        request.setAngle(newAngle);

        // Assert
        assertEquals(newAngle, request.getAngle(), "Angle should be updated correctly");
    }

    @Test
    void testConstructorWithNulls() {
        // Act
        NextPositionRequest request = new NextPositionRequest(null, null);

        // Assert
        assertNull(request.getStart(), "Start should be null when initialized with null");
        assertNull(request.getAngle(), "Angle should be null when initialized with null");
    }

    @Test
    void testSetNullValues() {
        // Arrange
        NextPositionRequest request = new NextPositionRequest(new LngLat(0.0, 0.0), 0.0);

        // Act
        request.setStart(null);
        request.setAngle(null);

        // Assert
        assertNull(request.getStart(), "Start should be null after setting it to null");
        assertNull(request.getAngle(), "Angle should be null after setting it to null");
    }
}
