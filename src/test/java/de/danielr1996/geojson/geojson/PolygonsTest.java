package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;
import org.geojson.Polygon;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PolygonsTest {
    @Test
    void findNorth() {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(2, 0),
                new LngLatAlt(3, 1),
                new LngLatAlt(2, 2),
                new LngLatAlt(0, 3),
                new LngLatAlt(0, 3),
                new LngLatAlt(1, 3),
                new LngLatAlt(-1, 3),
                new LngLatAlt(0, 2),
                new LngLatAlt(-1, 2),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-2, 0),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-1, -2),
                new LngLatAlt(0, -2),
                new LngLatAlt(0, -1),
                new LngLatAlt(0, 0)
        ));
        LngLatAlt northernmost = new LngLatAlt(-1, 3);
        LngLatAlt actual = Polygons.findNorth.apply(polygon);
        assertEquals(northernmost, actual);
    }

    @Test
    void findWest() {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(2, 0),
                new LngLatAlt(3, 1),
                new LngLatAlt(2, 2),
                new LngLatAlt(0, 3),
                new LngLatAlt(-1, 3),
                new LngLatAlt(0, 3),
                new LngLatAlt(1, 3),
                new LngLatAlt(0, 2),
                new LngLatAlt(-1, 2),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-2, 0),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-1, -2),
                new LngLatAlt(0, -2),
                new LngLatAlt(0, -1),
                new LngLatAlt(0, 0)
        ));
        LngLatAlt westernmost = new LngLatAlt(-2, 0);
        LngLatAlt actual = Polygons.findWest.apply(polygon);
        assertEquals(westernmost, actual);
    }

    @Test
    void findSouth() {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(2, 0),
                new LngLatAlt(3, 1),
                new LngLatAlt(2, 2),
                new LngLatAlt(0, 3),
                new LngLatAlt(-1, 3),
                new LngLatAlt(0, 3),
                new LngLatAlt(1, 3),
                new LngLatAlt(0, 2),
                new LngLatAlt(-1, 2),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-2, 0),
                new LngLatAlt(-1, -1),
                new LngLatAlt(0, -2),
                new LngLatAlt(-1, -2),
                new LngLatAlt(0, -1),
                new LngLatAlt(0, 0)
        ));
        LngLatAlt westernmost = new LngLatAlt(-1, -2);
        LngLatAlt actual = Polygons.findSouth.apply(polygon);
        assertEquals(westernmost, actual);
    }

    @Test
    void findEast() {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(
                new LngLatAlt(0, 0),
                new LngLatAlt(1, 0),
                new LngLatAlt(2, 0),
                new LngLatAlt(3, 1),
                new LngLatAlt(2, 2),
                new LngLatAlt(0, 3),
                new LngLatAlt(-1, 3),
                new LngLatAlt(0, 3),
                new LngLatAlt(1, 3),
                new LngLatAlt(0, 2),
                new LngLatAlt(-1, 2),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-2, 0),
                new LngLatAlt(-1, -1),
                new LngLatAlt(-1, -2),
                new LngLatAlt(0, -2),
                new LngLatAlt(0, -1),
                new LngLatAlt(0, 0)
        ));
        LngLatAlt westernmost = new LngLatAlt(3, 1);
        LngLatAlt actual = Polygons.findEast.apply(polygon);
        assertEquals(westernmost, actual);
    }

    @Test
    void containsTrue() {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(new LngLatAlt(0, 0), new LngLatAlt(0, 1), new LngLatAlt(1, 1), new LngLatAlt(1, 0)));
        LngLatAlt coord = new LngLatAlt(0.5, 0.5);
        assertTrue(Polygons.contains.apply(coord).apply(polygon));
    }

    @Test
    void containsFalse() {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(new LngLatAlt(0, 0), new LngLatAlt(0, 1), new LngLatAlt(1, 1), new LngLatAlt(1, 0)));
        LngLatAlt coord = new LngLatAlt(-1, -1);
        assertFalse(Polygons.contains.apply(coord).apply(polygon));
    }
}