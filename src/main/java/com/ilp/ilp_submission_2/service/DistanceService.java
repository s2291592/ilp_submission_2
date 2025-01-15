package com.ilp.ilp_submission_2.service;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import com.ilp.ilp_submission_2.model.NextPositionRequest;
import com.ilp.ilp_submission_2.model.IsInRegionRequest;
import com.ilp.ilp_submission_2.validators.DistanceRequestValidator;
import com.ilp.ilp_submission_2.validators.NextPosRequestValidator;
import com.ilp.ilp_submission_2.validators.IsInRegionValidator;
import com.ilp.ilp_submission_2.utils.DistanceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    private final NextPosRequestValidator validator;
    private final IsInRegionValidator regionValidator;

    public DistanceService(DistanceRequestValidator distanceRequestValidator,
                           NextPosRequestValidator validator,
                           IsInRegionValidator regionValidator) {
        this.validator = validator;
        this.regionValidator = regionValidator;
    }

    /**
     * Process distance calculation between two positions.
     */
    public ResponseEntity<String> processDistanceRequest(LngLatPairRequest request) {
        ResponseEntity<String> validationError =
                DistanceRequestValidator.validateLngLatPairRequest(request);
        if (validationError != null) {
            // Return the error immediately if invalid
            return validationError;
        }

        // If valid, do the distance calculation
        LngLat pos1 = request.getPosition1();
        LngLat pos2 = request.getPosition2();
        double distance = DistanceUtils.calculateEuclideanDistance(
                pos1.lat(), pos1.lng(), pos2.lat(), pos2.lng()
        );
        return ResponseEntity.ok(String.valueOf(distance));
    }

    /**
     * Process "is close to" check between two positions.
     */
    public ResponseEntity<?> processIsCloseToRequest(LngLatPairRequest request) {
        ResponseEntity<String> validationError =
                DistanceRequestValidator.validateLngLatPairRequest(request);
        if (validationError != null) {
            // Return the error immediately if invalid
            return validationError;
        }

        // If valid, check threshold
        LngLat pos1 = request.getPosition1();
        LngLat pos2 = request.getPosition2();
        boolean close = DistanceUtils.isWithinThreshold(
                pos1.lat(), pos1.lng(), pos2.lat(), pos2.lng()
        );
        return ResponseEntity.ok(close);
    }

    /**
     * Process next position calculation based on an angle and start position.
     */
    public ResponseEntity<?> processNextPositionRequest(NextPositionRequest request) {
        // Validate the request
        if (!validator.isValidRequest(request)) {
            return ResponseEntity.badRequest().body("Invalid input: Check 'start' lat/lng or 'angle' range");
        }

        LngLat start = request.getStart();
        Double angle = request.getAngle();

        // Calculate new position using utility
        LngLat newPosition = DistanceUtils.getPosition(angle, start);
        return ResponseEntity.ok(newPosition);
    }

    /**
     * Process "is in region" check.
     */
    public ResponseEntity<Boolean> processIsInRegionRequest(IsInRegionRequest request) {
        // Validate region
        if (!regionValidator.isRegionValid(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        // Check if position is inside/on border
        boolean isInRegion = regionValidator.isInRegionChecker(request);
        return ResponseEntity.ok(isInRegion);
    }
}
