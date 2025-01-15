package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import com.ilp.ilp_submission_2.validators.DistanceRequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class DistanceRequestValidatorTest {

    @Test
    void testValidateLngLatPairRequest_ValidRequest() {
        LngLat pos1 = new LngLat(45.0, -90.0);
        LngLat pos2 = new LngLat(10.0, 20.0);
        LngLatPairRequest request = new LngLatPairRequest(pos1, pos2);

        ResponseEntity<String> response = DistanceRequestValidator.validateLngLatPairRequest(request);

        assertNull(response, "Valid request should return null (indicating success)");
    }

    @Test
    void testValidateLngLatPairRequest_NullRequest() {
        LngLatPairRequest request = null;

        ResponseEntity<String> response = DistanceRequestValidator.validateLngLatPairRequest(request);

        assertNotNull(response, "Null request should return a bad request response");
        assertEquals(400, response.getStatusCodeValue(), "Response status should be 400");
        assertEquals("Invalid input: One or both positions are invalid or out of range", response.getBody(), "Error message should indicate invalid input");
    }

    @Test
    void testValidateLngLatPairRequest_NullPositions() {
        LngLatPairRequest request = new LngLatPairRequest(null, null);

        ResponseEntity<String> response = DistanceRequestValidator.validateLngLatPairRequest(request);

        assertNotNull(response, "Request with null positions should return a bad request response");
        assertEquals(400, response.getStatusCodeValue(), "Response status should be 400");
        assertEquals("Invalid input: One or both positions are invalid or out of range", response.getBody(), "Error message should indicate invalid input");
    }

    @Test
    void testValidateLngLatPairRequest_InvalidLngLat() {
        LngLat pos1 = new LngLat(100.0, 200.0); // Invalid latitude and longitude
        LngLat pos2 = new LngLat(10.0, 20.0);   // Valid
        LngLatPairRequest request = new LngLatPairRequest(pos1, pos2);

        ResponseEntity<String> response = DistanceRequestValidator.validateLngLatPairRequest(request);

        assertNotNull(response, "Request with invalid positions should return a bad request response");
        assertEquals(400, response.getStatusCodeValue(), "Response status should be 400");
        assertEquals("Invalid input: One or both positions are invalid or out of range", response.getBody(), "Error message should indicate invalid input");
    }

    @Test
    void testValidateLngLatPairRequest_BothInvalidLngLat() {
        LngLat pos1 = new LngLat(100.0, 200.0); // Invalid
        LngLat pos2 = new LngLat(-95.0, -200.0); // Invalid
        LngLatPairRequest request = new LngLatPairRequest(pos1, pos2);

        ResponseEntity<String> response = DistanceRequestValidator.validateLngLatPairRequest(request);

        assertNotNull(response, "Request with both positions invalid should return a bad request response");
        assertEquals(400, response.getStatusCodeValue(), "Response status should be 400");
        assertEquals("Invalid input: One or both positions are invalid or out of range", response.getBody(), "Error message should indicate invalid input");
    }
}
