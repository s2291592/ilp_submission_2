package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Node;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PathfindingUtilsTest {


    @Test
    void testIsPointInsidePolygon() {
        // Define a simple square polygon
        List<LngLat> squarePolygon = Arrays.asList(
                new LngLat(0.0, 0.0),
                new LngLat(0.0, 1.0),
                new LngLat(1.0, 1.0),
                new LngLat(1.0, 0.0)
        );

        // Test point inside the polygon
        LngLat insidePoint = new LngLat(0.5, 0.5);
        assertTrue(PathfindingUtils.isPointInsidePolygon(insidePoint, squarePolygon), "Point inside polygon should return true");

        // Test point outside the polygon
        LngLat outsidePoint = new LngLat(1.5, 0.5);
        assertFalse(PathfindingUtils.isPointInsidePolygon(outsidePoint, squarePolygon), "Point outside polygon should return false");

        // Test point on the edge of the polygon
        LngLat edgePoint = new LngLat(0.0, 0.5);
        assertTrue(PathfindingUtils.isPointInsidePolygon(edgePoint, squarePolygon), "Point on polygon edge should return true");
    }

    @Test
    void testDistance() {
        // Test identical points
        LngLat pointA = new LngLat(0.0, 0.0);
        LngLat pointB = new LngLat(0.0, 0.0);
        assertEquals(0.0, DistanceUtils.calculateEuclideanDistance(pointA.lat(), pointA.lng(), pointB.lat(), pointB.lng()), 1e-9, "Distance between identical points should be 0");

        // Test known distance
        pointB = new LngLat(3.0, 4.0);
        assertEquals(5.0, DistanceUtils.calculateEuclideanDistance(pointA.lat(), pointA.lng(), pointB.lat(), pointB.lng()), 1e-9, "Distance between (0,0) and (3,4) should be 5");
    }

    @Test
    void testCalculatePathAvoidingNoFlyZones_ComplexNoFlyZone() {
        LngLat start = new LngLat(0.0, 0.0);
        LngLat end = new LngLat(0.0015, 0.0015);

        Set<List<LngLat>> noFlyZones = new HashSet<>();
        noFlyZones.add(Arrays.asList(
                new LngLat(0.0005, 0.0005),
                new LngLat(0.0005, 0.001),
                new LngLat(0.001, 0.001),
                new LngLat(0.001, 0.0005),
                new LngLat(0.0005, 0.0005)
        ));

        List<LngLat> path = PathfindingUtils.calculatePathAvoidingNoFlyZones(start, end, noFlyZones);

        assertNotNull(path, "Path should not be null");
        assertFalse(path.isEmpty(), "Path should not be null");
        assertEquals(start, path.get(0), "Path should start at the start point");
        assertTrue(DistanceUtils.isWithinThreshold(path.get(path.size() - 1).lat(), path.get(path.size() - 1).lng(), end.lat(), end.lng()),
                "Path should end close to the end point");

        for (LngLat point : path) {
            for (List<LngLat> noFlyZone : noFlyZones) {
                assertFalse(PathfindingUtils.isPointInsidePolygon(point, noFlyZone),
                        "Path should not pass through a no-fly zone");
            }
        }
    }

    @Test
    void testCalculatePathAvoidingNoFlyZones_NoValidPath() {
        LngLat start = new LngLat(0.0, 0.0);
        LngLat end = new LngLat(0.002, 0.002);

        // Define a no-fly zone that completely blocks the path
        Set<List<LngLat>> noFlyZones = new HashSet<>();
        noFlyZones.add(Arrays.asList(
                new LngLat(-0.001, -0.001),
                new LngLat(-0.001, 0.003),
                new LngLat(0.003, 0.003),
                new LngLat(0.003, -0.001),
                new LngLat(-0.001, -0.001) // Ensure the polygon is closed
        ));

        // Call the method to calculate the path
        List<LngLat> path = PathfindingUtils.calculatePathAvoidingNoFlyZones(start, end, noFlyZones);

        // Validate the result
        assertNotNull(path, "Path should not be null");
        assertTrue(path.isEmpty(), "Path should be empty when no valid path exists");
    }






    @Test
    void testGetNeighbors() {
        LngLat current = new LngLat(2.0, 2.0);
        Set<List<LngLat>> noFlyZones = new HashSet<>();
        noFlyZones.add(Arrays.asList(
                new LngLat(1.0, 0.0),
                new LngLat(1.0, 3.0),
                new LngLat(3.0, 3.0),
                new LngLat(3.0, 0.0)
        ));

        List<Node> neighbors = PathfindingUtils.getNeighbors(new Node(current), noFlyZones);

        for (Node neighbor : neighbors) {
            assertFalse(PathfindingUtils.isPointInsidePolygon(neighbor.getPoint(), noFlyZones.iterator().next()),
                    "Neighbor should not be inside a no-fly zone");
        }
    }


    @Test
    void testReconstructPath() {
        // Define a chain of nodes
        Node node1 = new Node(new LngLat(0.0, 0.0));
        Node node2 = new Node(new LngLat(1.0, 1.0));
        Node node3 = new Node(new LngLat(2.0, 2.0));
        node2.setPrevious(node1);
        node3.setPrevious(node2);

        // Reconstruct path
        List<LngLat> path = PathfindingUtils.reconstructPath(node3);

        // Validate path
        assertNotNull(path, "Path should not be null");
        assertEquals(3, path.size(), "Path should contain 3 points");
        assertEquals(new LngLat(0.0, 0.0), path.get(0), "First point should be (0.0, 0.0)");
        assertEquals(new LngLat(1.0, 1.0), path.get(1), "Second point should be (1.0, 1.0)");
        assertEquals(new LngLat(2.0, 2.0), path.get(2), "Third point should be (2.0, 2.0)");
    }
}
