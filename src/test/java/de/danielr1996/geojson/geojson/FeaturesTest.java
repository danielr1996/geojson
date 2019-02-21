package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FeaturesTest {

    @Test
    void ofGeometry() {
        LngLatAlt coord1 = new LngLatAlt(0, 0, 0);
        LngLatAlt coord2 = new LngLatAlt(0, 1, 0);
        LineString lineString = new LineString();
        lineString.setCoordinates(Arrays.asList(coord1, coord2));
        Feature feature = Features.ofGeometry(lineString);
        assertEquals(lineString, feature.getGeometry());
    }

    @Test
    void extractLineStringContainsLineString() {
        LngLatAlt coord1 = new LngLatAlt(0, 0, 0);
        LngLatAlt coord2 = new LngLatAlt(0, 1, 0);
        LineString expected = new LineString();
        expected.setCoordinates(Arrays.asList(coord1, coord2));
        Feature feature = new Feature();
        feature.setGeometry(expected);

        LineString actual = Features.extractTypedGeometry(LineString.class).apply(feature);
        assertEquals(expected, actual);
    }

    @Test
    void extractLineStringContainsMultiPoint() {
        LngLatAlt coord1 = new LngLatAlt(0, 0, 0);
        LngLatAlt coord2 = new LngLatAlt(0, 1, 0);
        MultiPoint expected = new MultiPoint();
        expected.setCoordinates(Arrays.asList(coord1, coord2));
        Feature feature = new Feature();
        feature.setGeometry(expected);

        assertThrows(IllegalArgumentException.class, () -> Features.extractTypedGeometry(LineString.class).apply(feature));
    }
}