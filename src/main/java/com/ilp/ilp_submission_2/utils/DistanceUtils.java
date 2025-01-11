package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.request.LngLatPairRequest;
import com.ilp.ilp_submission_2.validators.DistanceRequestValidator;
import org.springframework.http.ResponseEntity;

public class DistanceUtils {

    /**
     * Calculate the Euclidean distance between two lat/lng coordinates.
     */
    public static double calculateEuclideanDistance(double lat1, double lng1, double lat2, double lng2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));
    }

    /**
     * Check if two positions are within a certain threshold of each other.
     */
    public static boolean isWithinThreshold(double lat1, double lng1, double lat2, double lng2) {
        double threshold = 0.00015;
        double distance = calculateEuclideanDistance(lat1, lng1, lat2, lng2);
        return distance < threshold;
    }

    /**
     * Compute a new position, given a start coordinate and an angle.
     */
    public static LngLat getPosition(Double angle, LngLat start) {
        double angleInRadians = Math.toRadians(angle);

        // Calculate the new latitude
        double newLat = start.lat() + (0.00015 * Math.cos(angleInRadians));
        if (newLat >= 90) {
            newLat = 180 - newLat;
        } else if (newLat <= -90) {
            newLat = -180 - newLat;
        }

        // Calculate the new longitude
        double newLng = start.lng() + (0.00015 * Math.sin(angleInRadians));
        if (newLng >= 180) {
            newLng = newLng - 360;
        } else if (newLng < -180) {
            newLng = newLng + 360;
        }

        // Return a new, immutable LngLat record with updated coordinates
        return new LngLat(newLng, newLat);
    }
}
