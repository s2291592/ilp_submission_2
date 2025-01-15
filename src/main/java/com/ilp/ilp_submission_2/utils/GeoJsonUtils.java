package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;

import java.util.List;

public class GeoJsonUtils {

    public static String convertPathToGeoJson(List<LngLat> path) {
        StringBuilder geoJsonBuilder = new StringBuilder();
        geoJsonBuilder.append("{\"type\":\"Feature\",");

        // Adding properties
        geoJsonBuilder.append("\"properties\":{},");

        // Geometry section
        geoJsonBuilder.append("\"geometry\":{\"type\":\"LineString\",\"coordinates\":[");
        for (int i = 0; i < path.size(); i++) {
            LngLat point = path.get(i);
            geoJsonBuilder.append("[").append(point.lng()).append(",").append(point.lat()).append("]");
            if (i < path.size() - 1) {
                geoJsonBuilder.append(",");
            }
        }
        geoJsonBuilder.append("]}}");

        return geoJsonBuilder.toString();
    }
}
