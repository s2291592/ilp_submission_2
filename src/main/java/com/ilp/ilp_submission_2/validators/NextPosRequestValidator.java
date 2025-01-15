package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.NextPositionRequest;
import org.springframework.stereotype.Component;

@Component
public class NextPosRequestValidator {

    public boolean isValidRequest(NextPositionRequest request) {
        // Check if the request itself is null
        if (request == null) {
            return false;
        }

        LngLat start = request.getStart();
        Double angle = request.getAngle();

        // Check if start is null or has missing fields
        if (start == null || start.lat() == null || start.lng() == null) {
            return false;
        }

        // Validate latitude range (-90 to 90)
        if (start.lat() < -90 || start.lat() > 90) {
            return false;
        }

        // Validate longitude range (-180 to 180)
        if (start.lng() < -180 || start.lng() > 180) {
            return false;
        }

        // Check angle
        if (angle == null) {
            return false;
        }

        // Validate angle range (0 to 360 exclusive)
        return angle >= 0 && angle < 360;
    }

}
