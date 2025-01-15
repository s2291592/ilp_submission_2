package com.ilp.ilp_submission_2.utils;

import com.ilp.ilp_submission_2.data.LngLat;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeoJsonUtilsTest {

    @Test
    void testConvertPathToGeoJson_ValidPath() {
        // Define a sample path
        List<LngLat> path = List.of(
                new LngLat(-3.190286, 55.945535),
                new LngLat(-3.191286, 55.946535),
                new LngLat(-3.192286, 55.947535)
        );

        // Expected GeoJSON output
        String expectedGeoJson = """
                {"type":"Feature",
                "properties":{},
                "geometry":{"type":"LineString","coordinates":[
                [-3.190286,55.945535],
                [-3.191286,55.946535],
                [-3.192286,55.947535]
                ]}}
                """.replace("\n", "").replace(" ", "");

        // Call the method
        String actualGeoJson = GeoJsonUtils.convertPathToGeoJson(path);

        // Assert the generated GeoJSON matches the expected output
        assertEquals(expectedGeoJson, actualGeoJson);
    }

    @Test
    void testConvertPathToGeoJson_EmptyPath() {
        // Define an empty path
        List<LngLat> path = List.of();

        // Expected GeoJSON output for an empty path
        String expectedGeoJson = """
                {"type":"Feature",
                "properties":{},
                "geometry":{"type":"LineString","coordinates":[]}}
                """.replace("\n", "").replace(" ", "");

        // Call the method
        String actualGeoJson = GeoJsonUtils.convertPathToGeoJson(path);

        // Assert the generated GeoJSON matches the expected output
        assertEquals(expectedGeoJson, actualGeoJson);
    }
}
