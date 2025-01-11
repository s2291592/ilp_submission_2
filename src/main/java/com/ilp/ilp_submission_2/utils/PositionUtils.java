package com.ilp.ilp_submission_2.utils;


import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.request.LngLatPairRequest;

public class PositionUtils {
    /**
     * Basic check if a LngLat is not null and lat/lng are within valid range.
     */
    public static boolean isValidLngLat(LngLat lngLat) {
        if (lngLat == null || lngLat.lat() == null || lngLat.lng() == null) {
            return true;
        }

        double lat = lngLat.lat();
        double lng = lngLat.lng();
        return (!(lat >= -90) || !(lat <= 90)) || (!(lng >= -180) || !(lng <= 180));
    }

    /**
     * Basic check if a LngLatPairRequest is not null and has non-null positions.
     */
    public static boolean isValidRequest(LngLatPairRequest request) {
        if (request == null) {
            return false;
        }

        LngLat pos1 = request.getPosition1();
        LngLat pos2 = request.getPosition2();
        return (pos1 != null && pos2 != null);
    }

}
