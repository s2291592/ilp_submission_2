package com.ilp.ilp_submission_2.constant;

public final class DroneConstants {

    private DroneConstants() {}

    public static final double STEP_DISTANCE = 0.00015; // Distance for each move
    public static final double DRONE_IS_CLOSE_THRESHOLD = STEP_DISTANCE;

    // 16 Compass directions as (dx, dy) pairs
    public static final double[][] COMPASS_DIRECTIONS = {
//            {STEP_DISTANCE, 0},                                  // E
//            {STEP_DISTANCE / Math.sqrt(2), STEP_DISTANCE / Math.sqrt(2)}, // NE
//            {0, STEP_DISTANCE},                                  // N
//            {-STEP_DISTANCE / Math.sqrt(2), STEP_DISTANCE / Math.sqrt(2)}, // NW
//            {-STEP_DISTANCE, 0},                                 // W
//            {-STEP_DISTANCE / Math.sqrt(2), -STEP_DISTANCE / Math.sqrt(2)}, // SW
//            {0, -STEP_DISTANCE},                                 // S
//            {STEP_DISTANCE / Math.sqrt(2), -STEP_DISTANCE / Math.sqrt(2)}, // SE
            {STEP_DISTANCE, 0},                                   // E
            {STEP_DISTANCE * Math.cos(Math.toRadians(22.5)), STEP_DISTANCE * Math.sin(Math.toRadians(22.5))},  // ESE
            {STEP_DISTANCE / Math.sqrt(2), STEP_DISTANCE / Math.sqrt(2)},  // NE
            {STEP_DISTANCE * Math.cos(Math.toRadians(67.5)), STEP_DISTANCE * Math.sin(Math.toRadians(67.5))},  // NNE
            {0, STEP_DISTANCE},                                   // N
            {-STEP_DISTANCE * Math.cos(Math.toRadians(67.5)), STEP_DISTANCE * Math.sin(Math.toRadians(67.5))}, // NNW
            {-STEP_DISTANCE / Math.sqrt(2), STEP_DISTANCE / Math.sqrt(2)}, // NW
            {-STEP_DISTANCE * Math.cos(Math.toRadians(22.5)), STEP_DISTANCE * Math.sin(Math.toRadians(22.5))}, // WNW
            {-STEP_DISTANCE, 0},                                  // W
            {-STEP_DISTANCE * Math.cos(Math.toRadians(22.5)), -STEP_DISTANCE * Math.sin(Math.toRadians(22.5))}, // WSW
            {-STEP_DISTANCE / Math.sqrt(2), -STEP_DISTANCE / Math.sqrt(2)}, // SW
            {-STEP_DISTANCE * Math.cos(Math.toRadians(67.5)), -STEP_DISTANCE * Math.sin(Math.toRadians(67.5))}, // SSW
            {0, -STEP_DISTANCE},                                  // S
            {STEP_DISTANCE * Math.cos(Math.toRadians(67.5)), -STEP_DISTANCE * Math.sin(Math.toRadians(67.5))},  // SSE
            {STEP_DISTANCE / Math.sqrt(2), -STEP_DISTANCE / Math.sqrt(2)}, // SE
            {STEP_DISTANCE * Math.cos(Math.toRadians(22.5)), -STEP_DISTANCE * Math.sin(Math.toRadians(22.5))}   // ESE
    };
}
