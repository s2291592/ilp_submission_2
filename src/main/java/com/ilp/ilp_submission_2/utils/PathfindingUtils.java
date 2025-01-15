package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.constant.DroneConstants;
import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Node;

import java.util.*;

public class PathfindingUtils {

    private static final double HEURISTIC_MULTIPLIER = 2;

    /**
     * Calculates a path from the start to the end point while avoiding no-fly zones.
     *
     * @param start       Starting LngLat point
     * @param end         Ending LngLat point
     * @param noFlyZones  Set of polygons representing restricted zones
     * @return List of LngLat points forming the path
     */
    public static List<LngLat> calculatePathAvoidingNoFlyZones(LngLat start, LngLat end, Set<List<LngLat>> noFlyZones) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getCost));
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Double> gScore = new HashMap<>();
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node(start);
        Node endNode = new Node(end);

        startNode.setCost(0);
        openSet.add(startNode);
        gScore.put(startNode, 0.0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (DistanceUtils.isWithinThreshold(current.getPoint().lat(), current.getPoint().lng(), endNode.getPoint().lat(), endNode.getPoint().lng())) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current, noFlyZones)) {
                if (closedSet.contains(neighbor)) continue;

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) +
                        DistanceUtils.calculateEuclideanDistance(current.getPoint().lat(), current.getPoint().lng(), neighbor.getPoint().lat(), neighbor.getPoint().lng());

                if (!openSet.contains(neighbor) || tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    neighbor.setPrevious(current);
                    gScore.put(neighbor, tentativeGScore);

                    double fScore = tentativeGScore + HEURISTIC_MULTIPLIER * heuristic(neighbor.getPoint(), end);
                    neighbor.setCost(fScore);
                    openSet.add(neighbor);
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    /**
     * Generates neighboring nodes for a given node while avoiding no-fly zones.
     */
    static List<Node> getNeighbors(Node current, Set<List<LngLat>> noFlyZones) {
        List<Node> neighbors = new ArrayList<>();
        double lng = current.getPoint().lng();
        double lat = current.getPoint().lat();

        for (double[] direction : DroneConstants.COMPASS_DIRECTIONS) {
            LngLat neighborPoint = new LngLat(lng + direction[0], lat + direction[1]);
            if (!isInsideNoFlyZone(neighborPoint, noFlyZones)) {
                neighbors.add(new Node(neighborPoint));
            }
        }

        return neighbors;
    }

    /**
     * Checks if a point is inside any no-fly zone.
     */
    private static boolean isInsideNoFlyZone(LngLat point, Set<List<LngLat>> noFlyZones) {
        for (List<LngLat> zone : noFlyZones) {
            if (isPointInsidePolygon(point, zone)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a point lies inside a polygon using the ray-casting algorithm.
     */
    static boolean isPointInsidePolygon(LngLat point, List<LngLat> polygon) {
        int n = polygon.size();
        boolean inside = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            LngLat vi = polygon.get(i);
            LngLat vj = polygon.get(j);

            if ((vi.lat() > point.lat()) != (vj.lat() > point.lat()) &&
                    (point.lng() < (vj.lng() - vi.lng()) * (point.lat() - vi.lat()) / (vj.lat() - vi.lat()) + vi.lng())) {
                inside = !inside;
            }
        }

        return inside;
    }

    /**
     * Reconstructs the path from the current node back to the start.
     */
    static List<LngLat> reconstructPath(Node current) {
        LinkedList<LngLat> path = new LinkedList<>();
        while (current != null) {
            path.addFirst(current.getPoint());
            current = current.getPrevious();
        }
        return path;
    }

    /**
     * Heuristic function for A* algorithm.
     */
    private static double heuristic(LngLat a, LngLat b) {
        return DistanceUtils.calculateEuclideanDistance(a.lat(), a.lng(), b.lat(), b.lng());
    }

}
