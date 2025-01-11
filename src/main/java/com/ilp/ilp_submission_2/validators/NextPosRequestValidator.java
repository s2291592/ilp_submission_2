package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.request.NextPositionRequest;
import org.springframework.stereotype.Component;

@Component
public class NextPosRequestValidator {

    public boolean isValidRequest(NextPositionRequest request) {
        LngLat start = request.getStart();
        Double angle = request.getAngle();

        // Check if start is null or missing fields
        if (start == null || start.lat() == null || start.lng() == null) {
            return false;
        }

        // Check latitude and longitude ranges
        if (start.lat() < -90 || start.lat() > 90) {
            return false;
        }
        if (start.lng() < -180 || start.lng() > 180) {
            return false;
        }

        // Check angle
        if (angle == null) {
            return false;
        }
        return angle >= 0 && angle < 360;
    }
}
