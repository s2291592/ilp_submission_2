package com.ilp.ilp_submission_2.validators;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.model.LngLatPairRequest;
import com.ilp.ilp_submission_2.utils.PositionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DistanceRequestValidator {



    /**
     * Validate the overall request. Returns:
     *  - A ResponseEntity with an error message if invalid
     *  - null if everything is valid
     */
    public static ResponseEntity<String> validateLngLatPairRequest(LngLatPairRequest request) {
        // 1) Check if request structure is valid
        if (!PositionUtils.isValidRequest(request)) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid input: One or both positions are invalid or out of range");
        }

        // 2) Check if each LngLat is in range
        LngLat pos1 = request.getPosition1();
        LngLat pos2 = request.getPosition2();
        if (PositionUtils.isValidLngLat(pos1) || PositionUtils.isValidLngLat(pos2)) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid input: One or both positions are invalid or out of range");
        }

        // If no issues, return null to signal "valid"
        return null;
    }
}
