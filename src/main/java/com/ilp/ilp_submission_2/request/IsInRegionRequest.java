package com.ilp.ilp_submission_2.request;

import com.ilp.ilp_submission_2.data.LngLat;
import com.ilp.ilp_submission_2.data.NamedRegion;

/**
 * Request object for checking if a position is inside or on the boundary of a region.
 *
 * @param position the position to check
 * @param region   the named region (polygon)
 */
public record IsInRegionRequest(LngLat position, NamedRegion region) {}

