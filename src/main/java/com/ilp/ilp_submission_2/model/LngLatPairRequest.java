package com.ilp.ilp_submission_2.model;

import com.ilp.ilp_submission_2.data.LngLat;


/**
 * Represents a request containing two positions, each defined by longitude and latitude.
 * <p>
 */
public class LngLatPairRequest {
    private LngLat position1;
    private LngLat position2;

    public LngLatPairRequest(LngLat pos1, LngLat pos2) {
        this.position1 = pos1;
        this.position2 = pos2;
    }

    // Getters and Setters
    public LngLat getPosition1() {
        return position1;
    }

    public void setPosition1(LngLat position1) {
        this.position1 = position1;
    }

    public LngLat getPosition2() {
        return position2;
    }

    public void setPosition2(LngLat position2) {
        this.position2 = position2;
    }
}