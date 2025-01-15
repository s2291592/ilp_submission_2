package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.NextPositionRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NextPosRequestValidatorTest {

    private final NextPosRequestValidator validator = new NextPosRequestValidator();

    @Test
    void testValidRequest() {
        // Valid request
        LngLat start = new LngLat(45.0, -90.0);
        Double angle = 45.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        assertTrue(validator.isValidRequest(request), "Valid request should return true");
    }

    @Test
    void testNullRequest() {
        // Null request
        NextPositionRequest request = null;
        assertFalse(validator.isValidRequest(request), "Null request should return false");
    }

    @Test
    void testNullStart() {
        // Request with null start
        Double angle = 45.0;
        NextPositionRequest request = new NextPositionRequest(null, angle);

        assertFalse(validator.isValidRequest(request), "Request with null start should return false");
    }

    @Test
    void testNullLatLng() {
        // Start with null latitude and longitude
        LngLat start = new LngLat(null, null);
        Double angle = 45.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        assertFalse(validator.isValidRequest(request), "Request with null latitude and longitude should return false");
    }

    @Test
    void testInvalidLatitude() {
        // Latitude out of range
        LngLat start = new LngLat(100.0, -91.0);
        Double angle = 45.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        assertFalse(validator.isValidRequest(request), "Request with invalid latitude should return false");
    }

    @Test
    void testInvalidLongitude() {
        // Longitude out of range
        LngLat start = new LngLat(45.0, -200.0);
        Double angle = 45.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        assertFalse(validator.isValidRequest(request), "Request with invalid longitude should return false");
    }

    @Test
    void testNullAngle() {
        // Null angle
        LngLat start = new LngLat(45.0, -90.0);
        NextPositionRequest request = new NextPositionRequest(start, null);

        assertFalse(validator.isValidRequest(request), "Request with null angle should return false");
    }

    @Test
    void testNegativeAngle() {
        // Negative angle
        LngLat start = new LngLat(45.0, -90.0);
        Double angle = -10.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        assertFalse(validator.isValidRequest(request), "Request with negative angle should return false");
    }

    @Test
    void testAngleOutOfRange() {
        // Angle greater than or equal to 360
        LngLat start = new LngLat(45.0, -90.0);
        Double angle = 360.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        assertFalse(validator.isValidRequest(request), "Request with angle >= 360 should return false");
    }
}
