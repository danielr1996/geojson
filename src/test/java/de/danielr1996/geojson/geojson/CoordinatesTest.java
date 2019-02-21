package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    void toDecimalString() {
    }

    @Test
    void toHexagesimalString() {
        LngLatAlt coord = new LngLatAlt(47.42106, 10.98536);
        String expected = "47 25 15.82, 10 59 7.30";
        String actual = Coordinates.toHexagesimalString(coord);
        assertEquals(expected, actual);
    }

    @Test
    void singleToHexagesimalString() {
        double part = 47.42106;
        String expected = "47 25 15.82";
        String actual = Coordinates.singleToHexagesimalString(part);
        assertEquals(expected, actual);
    }

    @Test
    void singleToHexagesimalString2() {
        double part = 10.98536;
        String expected = "10 59 7.30";
        String actual = Coordinates.singleToHexagesimalString(part);
        assertEquals(expected, actual);
    }
}