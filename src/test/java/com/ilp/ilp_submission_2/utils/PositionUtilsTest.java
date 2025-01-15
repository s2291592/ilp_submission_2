package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionUtilsTest {

    @Test
    void testIsValidLngLat_ValidCoordinates() {
        LngLat validLngLat = new LngLat(45.0, -90.0);
        assertFalse(PositionUtils.isValidLngLat(validLngLat), "Valid LngLat should return false");
    }

    @Test
    void testIsValidLngLat_InvalidLat() {
        LngLat invalidLat = new LngLat(100.0, -91.0);
        assertTrue(PositionUtils.isValidLngLat(invalidLat), "Invalid latitude should return true");
    }

    @Test
    void testIsValidLngLat_InvalidLng() {
        LngLat invalidLng = new LngLat(45.0, -200.0);
        assertTrue(PositionUtils.isValidLngLat(invalidLng), "Invalid longitude should return true");
    }

    @Test
    void testIsValidLngLat_NullLngLat() {
        assertTrue(PositionUtils.isValidLngLat(null), "Null LngLat should return true");
    }

    @Test
    void testIsValidLngLat_NullLatOrLng() {
        LngLat nullLat = new LngLat(null, -90.0);
        LngLat nullLng = new LngLat(45.0, null);
        assertTrue(PositionUtils.isValidLngLat(nullLat), "LngLat with null latitude should return true");
        assertTrue(PositionUtils.isValidLngLat(nullLng), "LngLat with null longitude should return true");
    }

    @Test
    void testIsValidRequest_ValidRequest() {
        LngLat pos1 = new LngLat(45.0, -90.0);
        LngLat pos2 = new LngLat(10.0, 20.0);
        LngLatPairRequest validRequest = new LngLatPairRequest(pos1, pos2);

        assertTrue(PositionUtils.isValidRequest(validRequest), "Valid LngLatPairRequest should return true");
    }

    @Test
    void testIsValidRequest_NullRequest() {
        assertFalse(PositionUtils.isValidRequest(null), "Null LngLatPairRequest should return false");
    }

    @Test
    void testIsValidRequest_NullPositions() {
        LngLatPairRequest nullPositions = new LngLatPairRequest(null, null);
        assertFalse(PositionUtils.isValidRequest(nullPositions), "LngLatPairRequest with null positions should return false");

        LngLat pos1 = new LngLat(45.0, -90.0);
        LngLatPairRequest oneNullPosition = new LngLatPairRequest(pos1, null);
        assertFalse(PositionUtils.isValidRequest(oneNullPosition), "LngLatPairRequest with one null position should return false");
    }
}
