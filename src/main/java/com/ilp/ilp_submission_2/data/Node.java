package com.ilp.ilp_submission_2.data;

import java.util.Objects;

public class Node {
    private final LngLat point;
    private double cost = Double.MAX_VALUE; // For A* cost calculation
    private Node previous; // To track the path

    public Node(LngLat point) {
        this.point = point;
    }

    public LngLat getPoint() {
        return point;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(point, node.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "Node{" +
                "point=" + point +
                ", cost=" + cost +
                '}';
    }
}
