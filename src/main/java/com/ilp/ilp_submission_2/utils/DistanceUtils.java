package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;

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

        // Calculate the new latitude and longitude
        double newLat = start.lat() + (0.00015 * Math.cos(angleInRadians));
        double newLng = start.lng() + (0.00015 * Math.sin(angleInRadians));

        // Ensure latitude is within -90 to 90
        newLat = Math.max(-90, Math.min(90, newLat));

        // Robust wrapping for longitude
        newLng = ((newLng + 180) % 360 + 360) % 360 - 180;

        return new LngLat(newLng, newLat);
    }




}
