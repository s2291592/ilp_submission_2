package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import com.ilp.ilp_submission_2.model.NextPositionRequest;
import com.ilp.ilp_submission_2.model.IsInRegionRequest;
import com.ilp.ilp_submission_2.validators.NextPosRequestValidator;
import com.ilp.ilp_submission_2.validators.IsInRegionValidator;
import com.ilp.ilp_submission_2.utils.DistanceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DistanceServiceTest {

    @Mock
    private NextPosRequestValidator nextPosValidator;

    @Mock
    private IsInRegionValidator regionValidator;

    @InjectMocks
    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessDistanceRequest_ValidRequest() {
        LngLat pos1 = new LngLat(0.0, 0.0);
        LngLat pos2 = new LngLat(3.0, 4.0);
        LngLatPairRequest request = new LngLatPairRequest(pos1, pos2);

        ResponseEntity<String> response = distanceService.processDistanceRequest(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("5.0", response.getBody());
    }

    @Test
    void testProcessDistanceRequest_InvalidRequest() {
        LngLatPairRequest request = new LngLatPairRequest(null, new LngLat(3.0, 4.0));

        ResponseEntity<String> response = distanceService.processDistanceRequest(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Invalid input"));
    }

    @Test
    void testProcessIsCloseToRequest_ValidRequest() {
        LngLat pos1 = new LngLat(0.0, 0.0);
        LngLat pos2 = new LngLat(0.0, 0.0001);
        LngLatPairRequest request = new LngLatPairRequest(pos1, pos2);

        ResponseEntity<?> response = distanceService.processIsCloseToRequest(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
    }

    @Test
    void testProcessIsCloseToRequest_InvalidRequest() {
        LngLatPairRequest request = new LngLatPairRequest(null, null);

        ResponseEntity<?> response = distanceService.processIsCloseToRequest(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid input"));
    }

    @Test
    void testProcessNextPositionRequest_ValidRequest() {
        LngLat start = new LngLat(0.0, 0.0);
        Double angle = 90.0;
        NextPositionRequest request = new NextPositionRequest(start, angle);

        when(nextPosValidator.isValidRequest(request)).thenReturn(true);
        LngLat expectedPosition = new LngLat(0.0, 0.00015);
        mockStatic(DistanceUtils.class);
        when(DistanceUtils.getPosition(angle, start)).thenReturn(expectedPosition);

        ResponseEntity<?> response = distanceService.processNextPositionRequest(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedPosition, response.getBody());
    }

    @Test
    void testProcessNextPositionRequest_InvalidRequest() {
        NextPositionRequest request = new NextPositionRequest(null, null);

        when(nextPosValidator.isValidRequest(request)).thenReturn(false);

        ResponseEntity<?> response = distanceService.processNextPositionRequest(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid input"));
    }

    @Test
    void testProcessIsInRegionRequest_ValidRequest() {
        IsInRegionRequest request = mock(IsInRegionRequest.class);

        when(regionValidator.isRegionValid(request)).thenReturn(true);
        when(regionValidator.isInRegionChecker(request)).thenReturn(true);

        ResponseEntity<Boolean> response = distanceService.processIsInRegionRequest(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testProcessIsInRegionRequest_InvalidRequest() {
        IsInRegionRequest request = mock(IsInRegionRequest.class);

        when(regionValidator.isRegionValid(request)).thenReturn(false);

        ResponseEntity<Boolean> response = distanceService.processIsInRegionRequest(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }
}
