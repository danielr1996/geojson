package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FeaturesTest {

    @Test
    void ofLineString() {
    LngLatAlt coord1 = new LngLatAlt(0,0,0);
    LngLatAlt coord2 = new LngLatAlt(0,1,0);
    LineString lineString = new LineString();
    lineString.setCoordinates(Arrays.asList(coord1, coord2));
    Feature feature = Features.ofGeometry(lineString);
    assertEquals(lineString, feature.getGeometry());
    }
}