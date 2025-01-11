package com.ilp.ilp_submission_2.constant;

/**
 * Centralizes all ILP 2024 endpoint URLs.
 */
public final class ILPEndpoints {

    /**
     * Prevent instantiation of utility class.
     */
    private ILPEndpoints() {}

    public static final String ORDERS_URL = "https://ilp-rest-2024.azurewebsites.net/orders";
    public static final String RESTAURANTS_URL = "https://ilp-rest-2024.azurewebsites.net/restaurants";
    public static final String NO_FLY_ZONES_URL = "https://ilp-rest-2024.azurewebsites.net/noFlyZones";
    public static final String CENTRAL_AREA_URL = "https://ilp-rest-2024.azurewebsites.net/centralArea";
    public static final String BOUNDING_BOX_GEOJSON_URL = "https://ilp-rest-2024.azurewebsites.net/bounding-box.geojson";
    public static final String ALL_GEOJSON_URL = "https://ilp-rest-2024.azurewebsites.net/all.geojson";
}
