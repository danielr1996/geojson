package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeoJSONCollectorsTest {
    private static List<LngLatAlt> coords = new ArrayList<>();

    @BeforeAll
    static void setup(){
        coords.add(new LngLatAlt(0,0));
        coords.add(new LngLatAlt(1,1));
        coords.add(new LngLatAlt(1,2));
        coords.add(new LngLatAlt(2,1));
        coords.add(new LngLatAlt(3,3));
        coords.add(new LngLatAlt(4,4));
    }

    @Test
    void toMultiPoint() {
        MultiPoint expected = new MultiPoint();
        expected.add(new LngLatAlt(0,0));
        expected.add(new LngLatAlt(1,1));
        expected.add(new LngLatAlt(1,2));
        expected.add(new LngLatAlt(2,1));
        expected.add(new LngLatAlt(3,3));
        expected.add(new LngLatAlt(4,4));

        MultiPoint actual = coords.stream().collect(GeoJSONCollectors.toMultiPoint());
        assertEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void toPolygon() {
        Polygon expected = new Polygon();
        expected.setExteriorRing(new ArrayList<>());
        expected.getExteriorRing().add(new LngLatAlt(0,0));
        expected.getExteriorRing().add(new LngLatAlt(1,1));
        expected.getExteriorRing().add(new LngLatAlt(1,2));
        expected.getExteriorRing().add(new LngLatAlt(2,1));
        expected.getExteriorRing().add(new LngLatAlt(3,3));
        expected.getExteriorRing().add(new LngLatAlt(4,4));

        Polygon actual = coords.stream().collect(GeoJSONCollectors.toPolygon());
        assertEquals(expected.getCoordinates(), actual.getCoordinates());
    }
}