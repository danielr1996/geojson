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

    @Test
    void of() {
        LngLatAlt coord = new LngLatAlt(1, 2, 3);
        LngLatAlt expected = new LngLatAlt(1, 2, 3);
        LngLatAlt actual = Coordinates.of(coord);

        assertEquals(expected, actual);
        assertNotSame(expected, actual);
    }

    @Test
    void moveNorth() {
        LngLatAlt coord = new LngLatAlt(1, 2, 3);
        LngLatAlt expected = new LngLatAlt(1, 3, 3);
        LngLatAlt actual = Coordinates.moveNorth.apply(1D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveNorthGreater90Degrees() {
        LngLatAlt coord = new LngLatAlt(1, 80, 3);
        LngLatAlt expected = new LngLatAlt(1, 90, 3);
        LngLatAlt actual = Coordinates.moveNorth.apply(20D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveNorthCrossEquator() {
        LngLatAlt coord = new LngLatAlt(1, -5, 3);
        LngLatAlt expected = new LngLatAlt(1, 5, 3);
        LngLatAlt actual = Coordinates.moveNorth.apply(10D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveSouth() {
        LngLatAlt coord = new LngLatAlt(1, 2, 3);
        LngLatAlt expected = new LngLatAlt(1, 1, 3);
        LngLatAlt actual = Coordinates.moveSouth.apply(1D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveSouthSmallerMinus90Degrees() {
        LngLatAlt coord = new LngLatAlt(1, -80, 3);
        LngLatAlt expected = new LngLatAlt(1, -90, 3);
        LngLatAlt actual = Coordinates.moveSouth.apply(20D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveSouthCrossEquator() {
        LngLatAlt coord = new LngLatAlt(1, 5, 3);
        LngLatAlt expected = new LngLatAlt(1, -5, 3);
        LngLatAlt actual = Coordinates.moveSouth.apply(10D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveWest() {
        LngLatAlt coord = new LngLatAlt(1, 2, 3);
        LngLatAlt expected = new LngLatAlt(0, 2, 3);
        LngLatAlt actual = Coordinates.moveWest.apply(1D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveWestCrossNullMeridian() {
        LngLatAlt coord = new LngLatAlt(5, 2, 3);
        LngLatAlt expected = new LngLatAlt(-5, 2, 3);
        LngLatAlt actual = Coordinates.moveWest.apply(10D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveWestSmaller180Degress() {
        LngLatAlt coord = new LngLatAlt(-160, 2, 3);
        LngLatAlt expected = new LngLatAlt(160, 2, 3);
        LngLatAlt actual = Coordinates.moveWest.apply(40D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveWestSmaller180Degress2() {
        LngLatAlt coord = new LngLatAlt(-120, 2, 3);
        LngLatAlt expected = new LngLatAlt(140, 2, 3);
        LngLatAlt actual = Coordinates.moveWest.apply(100D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveEast() {
        LngLatAlt coord = new LngLatAlt(1, 2, 3);
        LngLatAlt expected = new LngLatAlt(2, 2, 3);
        LngLatAlt actual = Coordinates.moveEast.apply(1D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveEastCrossNullMeridian() {
        LngLatAlt coord = new LngLatAlt(-1, 2, 3);
        LngLatAlt expected = new LngLatAlt(1, 2, 3);
        LngLatAlt actual = Coordinates.moveEast.apply(2D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveEastGreater180Degrees() {
        LngLatAlt coord = new LngLatAlt(160, 2, 3);
        LngLatAlt expected = new LngLatAlt(-160, 2, 3);
        LngLatAlt actual = Coordinates.moveEast.apply(40D).apply(coord);
        assertEquals(expected, actual);
    }

    @Test
    void moveEastGreater180Degrees2() {
        LngLatAlt coord = new LngLatAlt(120, 2, 3);
        LngLatAlt expected = new LngLatAlt(-140, 2, 3);
        LngLatAlt actual = Coordinates.moveEast.apply(100D).apply(coord);
        assertEquals(expected, actual);
    }
}