//package com.ilp.ilp_submission_2.validators;
//
//import com.ilp.ilp_submission_2.data.LngLat;
//import com.ilp.ilp_submission_2.data.NamedRegion;
//import com.ilp.ilp_submission_2.model.IsInRegionRequest;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class IsInRegionValidatorTest {
//
//    private final IsInRegionValidator validator = new IsInRegionValidator();
//
//    // Helper method to create a closed polygon
//    private List<LngLat> createClosedPolygon() {
//        return Arrays.asList(
//                new LngLat(-3.19, 55.94),
//                new LngLat(-3.18, 55.94),
//                new LngLat(-3.18, 55.95),
//                new LngLat(-3.19, 55.94) // Closing the polygon
//        );
//    }
//
//    // Helper method to create an open polygon
//    private List<LngLat> createOpenPolygon() {
//        return Arrays.asList(
//                new LngLat(-3.19, 55.94),
//                new LngLat(-3.18, 55.94),
//                new LngLat(-3.18, 55.95) // Not closing the polygon
//        );
//    }
//
//    @Test
//    void testIsRegionValid_NullRequest() {
//        IsInRegionRequest request = null;
//        assertFalse(validator.isRegionValid(request), "Null request should return false");
//    }
//
//    @Test
//    void testIsRegionValid_NullPosition() {
//        NamedRegion region = new NamedRegion("central", createClosedPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(null, region);
//        assertFalse(validator.isRegionValid(request), "Request with null position should return false");
//    }
//
//    @Test
//    void testIsRegionValid_NullRegion() {
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), null);
//        assertFalse(validator.isRegionValid(request), "Request with null region should return false");
//    }
//
//    @Test
//    void testIsRegionValid_InvalidRegionName() {
//        NamedRegion invalidRegion = new NamedRegion("wrong-name", createClosedPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), invalidRegion);
//        assertFalse(validator.isRegionValid(request), "Region with invalid name should return false");
//    }
//
//    @Test
//    void testIsRegionValid_NullVertices() {
//        NamedRegion invalidRegion = new NamedRegion("central", null);
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), invalidRegion);
//        assertFalse(validator.isRegionValid(request), "Region with null vertices should return false");
//    }
//
//    @Test
//    void testIsRegionValid_TooFewVertices() {
//        List<LngLat> vertices = Arrays.asList(
//                new LngLat(-3.19, 55.94),
//                new LngLat(-3.18, 55.94) // Only two vertices
//        );
//        NamedRegion invalidRegion = new NamedRegion("central", vertices);
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), invalidRegion);
//        assertFalse(validator.isRegionValid(request), "Region with less than 3 vertices should return false");
//    }
//
//    @Test
//    void testIsRegionValid_ValidPolygon() {
//        NamedRegion validRegion = new NamedRegion("central", createClosedPolygon());
//        IsInRegionRequest validRequest = new IsInRegionRequest(new LngLat(-3.185, 55.945), validRegion);
//        assertTrue(validator.isRegionValid(validRequest), "Valid region should return true");
//    }
//
//    @Test
//    void testIsInRegionChecker_PointInside() {
//        NamedRegion validRegion = new NamedRegion("central", createClosedPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), validRegion);
//        assertTrue(validator.isInRegionChecker(request), "Point inside region should return true");
//    }
//
//    @Test
//    void testIsInRegionChecker_PointOutside() {
//        NamedRegion validRegion = new NamedRegion("central", createClosedPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.2, 55.945), validRegion);
//        assertFalse(validator.isInRegionChecker(request), "Point outside region should return false");
//    }
//
//    @Test
//    void testIsInRegionChecker_PointOnEdge() {
//        NamedRegion validRegion = new NamedRegion("central", createClosedPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.19, 55.94), validRegion);
//        assertTrue(validator.isInRegionChecker(request), "Point on edge of region should return true");
//    }
//
//    @Test
//    void testIsInRegionChecker_InvalidPolygon() {
//        NamedRegion invalidRegion = new NamedRegion("central", createOpenPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), invalidRegion);
//        assertFalse(validator.isRegionValid(request), "Invalid polygon should return false");
//        assertFalse(validator.isInRegionChecker(request), "Checker should return false for invalid polygon");
//    }
//
//    @Test
//    void testIsInRegionChecker_EmptyVertices() {
//        NamedRegion invalidRegion = new NamedRegion("central", List.of());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-3.185, 55.945), invalidRegion);
//        assertFalse(validator.isRegionValid(request), "Region with empty vertices should return false");
//        assertFalse(validator.isInRegionChecker(request), "Checker should return false for empty vertices");
//    }
//
//    @Test
//    void testIsInRegionChecker_PointFarOutside() {
//        NamedRegion validRegion = new NamedRegion("central", createClosedPolygon());
//        IsInRegionRequest request = new IsInRegionRequest(new LngLat(-10.0, 10.0), validRegion);
//        assertFalse(validator.isInRegionChecker(request), "Point far outside region should return false");
//    }
//}
//
