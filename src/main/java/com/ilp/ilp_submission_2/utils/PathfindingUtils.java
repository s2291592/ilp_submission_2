package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.Node;

import java.util.*;

public class PathfindingUtils {

    public static List<LngLat> calculatePathAvoidingNoFlyZones(LngLat start, LngLat end, Set<LngLat> noFlyZones) {
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

            if (current.equals(endNode)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current, noFlyZones)) {
                if (closedSet.contains(neighbor)) continue;

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) +
                        distance(current.getPoint(), neighbor.getPoint());

                if (!openSet.contains(neighbor) || tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    neighbor.setPrevious(current);
                    gScore.put(neighbor, tentativeGScore);
                    neighbor.setCost(tentativeGScore + heuristic(neighbor.getPoint(), end));
                    openSet.add(neighbor);
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    private static List<Node> getNeighbors(Node current, Set<LngLat> noFlyZones) {
        List<Node> neighbors = new ArrayList<>();
        double lng = current.getPoint().lng();
        double lat = current.getPoint().lat();

        List<LngLat> potentialNeighbors = List.of(
                new LngLat(lng + 0.0001, lat),
                new LngLat(lng - 0.0001, lat),
                new LngLat(lng, lat + 0.0001),
                new LngLat(lng, lat - 0.0001)
        );

        for (LngLat neighborPoint : potentialNeighbors) {
            if (!noFlyZones.contains(neighborPoint)) {
                neighbors.add(new Node(neighborPoint));
            }
        }

        return neighbors;
    }

    private static List<LngLat> reconstructPath(Node current) {
        List<LngLat> path = new ArrayList<>();
        while (current != null) {
            path.addFirst(current.getPoint());
            current = current.getPrevious();
        }
        return path;
    }

    private static double heuristic(LngLat a, LngLat b) {
        return distance(a, b);
    }

    private static double distance(LngLat a, LngLat b) {
        return Math.sqrt(Math.pow(a.lng() - b.lng(), 2) + Math.pow(a.lat() - b.lat(), 2));
    }
}
