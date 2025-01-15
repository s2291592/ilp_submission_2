package com.ilp.ilp_submission_2.controllers;


import com.ilp.ilp_submission_2.model.IsInRegionRequest;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import com.ilp.ilp_submission_2.model.NextPositionRequest;

import com.ilp.ilp_submission_2.service.DistanceService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api")
public class DistancePostController {

    private final DistanceService distanceService;



    @Autowired
    public DistancePostController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }




    @PostMapping("/distanceTo")
    public ResponseEntity<String> calculateDistance(@RequestBody LngLatPairRequest request) {
        return distanceService.processDistanceRequest(request);
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<?> isCloseTo(@RequestBody LngLatPairRequest request) {
        return distanceService.processIsCloseToRequest(request);
    }


    @PostMapping("/nextPosition")
    public ResponseEntity<?> nextPosition(@RequestBody NextPositionRequest request) {
        return distanceService.processNextPositionRequest(request);
    }


    @PostMapping("/isInRegion")
    public ResponseEntity<Boolean> isInRegion(@RequestBody IsInRegionRequest request) {
        return distanceService.processIsInRegionRequest(request);
    }

}
