//package com.ilp.ilp_submission_2.validators;
//
//import com.ilp.ilp_submission_2.data.LngLat;
//import com.ilp.ilp_submission_2.data.NamedRegion;
//import com.ilp.ilp_submission_2.request.IsInRegionRequest;
//
//import java.awt.geom.Path2D;
//import java.util.List;
//
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class IsInRegionValidator {
//
//    /**
//     * Checks if the region in the request is valid (i.e., at least 3 vertices,
//     * and the first and last vertices are "close enough" to consider it closed).
//     */
//    public boolean isRegionValid(IsInRegionRequest request) {
//        NamedRegion region = request.region();
//        List<LngLat> vertices = region.vertices();
//
//        // A valid polygon must have at least 3 vertices
//        if (vertices.size() < 3) {
//            return false;
//        }
//
//        // Check if first and last vertices are within a small threshold
//        LngLat first = vertices.get(0);
//        LngLat last = vertices.get(vertices.size() - 1);
//
//        return isWithinThreshold(first.lat(), first.lng(), last.lat(), last.lng());
//    }
//
//    /**
//     * Checks if the position in the request is inside the region or on its border.
//     */
//    public boolean isInRegionChecker(IsInRegionRequest request) {
//        LngLat position = request.position();
//        List<LngLat> vertices = request.region().vertices();
//
//        // Create a Path2D polygon
//        Path2D regionPath = new Path2D.Double();
//        regionPath.moveTo(vertices.get(0).lng(), vertices.get(0).lat());
//
//        for (int i = 1; i < vertices.size(); i++) {
//            regionPath.lineTo(vertices.get(i).lng(), vertices.get(i).lat());
//        }
//        regionPath.closePath();
//
//        // If the point is inside the polygon
//        if (regionPath.contains(position.lng(), position.lat())) {
//            return true;
//        }
//
//        // Otherwise, check each border segment
//        for (int i = 0; i < vertices.size(); i++) {
//            LngLat v1 = vertices.get(i);
//            LngLat v2 = vertices.get((i + 1) % vertices.size()); // wrap-around
//
//            if (isOnBorder(v1, v2, position.lng(), position.lat())) {
//                return true;
//            }
//        }
//
//        // Not inside and not on any border
//        return false;
//    }
//
//    /**
//     * Helper method to determine if the region’s first and last vertex are within a threshold.
//     * You can customize the threshold as needed.
//     */
//    private boolean isWithinThreshold(Double lat1, Double lng1, Double lat2, Double lng2) {
//        // Example threshold
//        double threshold = 0.00015;
//
//        double distance = Math.sqrt(
//                Math.pow(lat1 - lat2, 2) + Math.pow(lng1 - lng2, 2)
//        );
//
//        return distance < threshold;
//    }
//
//    /**
//     * Checks if the point (lng, lat) is on the line segment from vertex1 to vertex2.
//     */
//    private boolean isOnBorder(LngLat vertex1, LngLat vertex2, Double lng, Double lat) {
//        double minLng = Math.min(vertex1.lng(), vertex2.lng());
//        double maxLng = Math.max(vertex1.lng(), vertex2.lng());
//        double minLat = Math.min(vertex1.lat(), vertex2.lat());
//        double maxLat = Math.max(vertex1.lat(), vertex2.lat());
//
//        // Quick bounding-box check
//        if (lng >= minLng && lng <= maxLng && lat >= minLat && lat <= maxLat) {
//            // Cross-product to check near-collinearity
//            double crossProduct =
//                    (lat - vertex1.lat()) * (vertex2.lng() - vertex1.lng())
//                            - (lng - vertex1.lng()) * (vertex2.lat() - vertex1.lat());
//            return Math.abs(crossProduct) < 1e-6;
//        }
//        return false;
//    }
//}
package com.ilp.ilp_submission_2.validators;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.NamedRegion;
import com.ilp.ilp_submission_2.request.IsInRegionRequest;
import org.springframework.stereotype.Component;

import java.awt.geom.Path2D;
import java.util.List;

@Component
public class IsInRegionValidator {

    /**
     * Validates the overall structure of the IsInRegionRequest.
     * Ensures that:
     * - The request has a properly mapped 'position' (not null).
     * - The request has a properly mapped 'region' (not null).
     * - The region has the correct 'name' (non-null, non-empty, and exactly "central")
     * - The region has a valid list of vertices (non-null, at least 3 vertices).
     * - The polygon is considered closed (i.e. the first and last vertices are "close enough").
     *
     * If any of these checks fail, it indicates that the JSON used the wrong keys.
     */
    public boolean isRegionValid(IsInRegionRequest request) {
        // Check that the top-level fields have been mapped correctly.
        if (request == null) return false;
        if (request.position() == null) return false;
        if (request.region() == null) return false;

        NamedRegion region = request.region();

        // Check that the region's name is correctly mapped and valid.
        // In the correct JSON, the property should be "name" with value "central".
        if (region.name() == null || region.name().trim().isEmpty()
                || !region.name().equalsIgnoreCase("central")) {
            return false;
        }

        List<LngLat> vertices = region.vertices();

        // Must have a valid list of vertices – if the JSON used the wrong key (e.g., "verticesList"),
        // then vertices could be null.
        if (vertices == null || vertices.size() < 3) {
            return false;
        }

        // Check if first and last vertices are within a small threshold to consider the polygon as closed.
        LngLat first = vertices.get(0);
        LngLat last = vertices.get(vertices.size() - 1);
        return isWithinThreshold(first.lat(), first.lng(), last.lat(), last.lng());
    }

    /**
     * Checks if the position in the request is inside the region or on its border.
     */
    public boolean isInRegionChecker(IsInRegionRequest request) {
        LngLat position = request.position();
        List<LngLat> vertices = request.region().vertices();

        // Create a Path2D polygon from the vertices
        Path2D regionPath = new Path2D.Double();
        regionPath.moveTo(vertices.get(0).lng(), vertices.get(0).lat());

        for (int i = 1; i < vertices.size(); i++) {
            regionPath.lineTo(vertices.get(i).lng(), vertices.get(i).lat());
        }
        regionPath.closePath();

        // If the point is inside the polygon
        if (regionPath.contains(position.lng(), position.lat())) {
            return true;
        }

        // Otherwise, check if the point is on one of the edges of the polygon.
        for (int i = 0; i < vertices.size(); i++) {
            LngLat v1 = vertices.get(i);
            LngLat v2 = vertices.get((i + 1) % vertices.size()); // wrap-around
            if (isOnBorder(v1, v2, position.lng(), position.lat())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to check if the first and last vertices are "close enough" to consider the polygon as closed.
     */
    private boolean isWithinThreshold(Double lat1, Double lng1, Double lat2, Double lng2) {
        double threshold = 0.00015;
        double distance = Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lng1 - lng2, 2));
        return distance < threshold;
    }

    /**
     * Determines if a given point (lng, lat) lies on the line segment from vertex1 to vertex2.
     */
    private boolean isOnBorder(LngLat vertex1, LngLat vertex2, Double lng, Double lat) {
        double minLng = Math.min(vertex1.lng(), vertex2.lng());
        double maxLng = Math.max(vertex1.lng(), vertex2.lng());
        double minLat = Math.min(vertex1.lat(), vertex2.lat());
        double maxLat = Math.max(vertex1.lat(), vertex2.lat());

        // Quick bounding-box check
        if (lng >= minLng && lng <= maxLng && lat >= minLat && lat <= maxLat) {
            double crossProduct = (lat - vertex1.lat()) * (vertex2.lng() - vertex1.lng())
                    - (lng - vertex1.lng()) * (vertex2.lat() - vertex1.lat());
            return Math.abs(crossProduct) < 1e-6;
        }
        return false;
    }
}
