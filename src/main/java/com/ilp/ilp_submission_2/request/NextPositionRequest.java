package com.ilp.ilp_submission_2.request;

import com.ilp.ilp_submission_2.data.LngLat;

/**
 * Represents a request to calculate the next position of a drone based on a starting position and an angle.
 *
 */
public class NextPositionRequest {
    private LngLat start;
    private Double angle;

    public LngLat getStart() {
        return start;
    }

    public void setStart(LngLat start) {
        this.start = start;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}