package com.ilp.ilp_submission_2.controllers;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.IsInRegionRequest;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import com.ilp.ilp_submission_2.model.NextPositionRequest;
import com.ilp.ilp_submission_2.service.DistanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DistancePostControllerTest {

    @Mock
    private DistanceService distanceService;

    @InjectMocks
    private DistancePostController distancePostController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(distancePostController).build();
    }

    @Test
    void testCalculateDistance() throws Exception {
        when(distanceService.processDistanceRequest(any(LngLatPairRequest.class)))
                .thenReturn(ResponseEntity.ok("123.45"));

        mockMvc.perform(post("/api/distanceTo")
                        .contentType("application/json")
                        .content("{\"position1\": {\"lat\": 55.94, \"lng\": -3.19}, \"position2\": {\"lat\": 55.95, \"lng\": -3.18}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("123.45"));
    }

    @Test
    void testIsCloseTo() throws Exception {
        when(distanceService.processIsCloseToRequest(any(LngLatPairRequest.class)))
                .thenReturn((ResponseEntity) ResponseEntity.ok(Boolean.TRUE));

        mockMvc.perform(post("/api/isCloseTo")
                        .contentType("application/json")
                        .content("{\"position1\": {\"lat\": 55.94, \"lng\": -3.19}, \"position2\": {\"lat\": 55.95, \"lng\": -3.18}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testNextPosition() throws Exception {
        LngLat mockNextPosition = new LngLat(55.945, -3.18);
        when(distanceService.processNextPositionRequest(any(NextPositionRequest.class)))
                .thenReturn((ResponseEntity) ResponseEntity.ok(mockNextPosition));


        mockMvc.perform(post("/api/nextPosition")
                        .contentType("application/json")
                        .content("{\"start\": {\"lng\": 55.94, \"lat\": -3.19}, \"angle\": 90}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"lng\": 55.945, \"lat\": -3.18}"));
    }


    @Test
    void testIsInRegion() throws Exception {
        when(distanceService.processIsInRegionRequest(any(IsInRegionRequest.class)))
                .thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(post("/api/isInRegion")
                        .contentType("application/json")
                        .content("{\"point\": {\"lat\": 55.94, \"lng\": -3.19}, \"region\": {\"name\": \"Central\", \"vertices\": [{\"lat\": 55.94, \"lng\": -3.19}, {\"lat\": 55.95, \"lng\": -3.18}]}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
