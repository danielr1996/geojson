package de.danielr1996.geojson.geojson;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineStringsTest {

    @Test
    void mergeFacing() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, -1));
        base.add(new LngLatAlt(0, 0));
        LineString append = new LineString();
        append.add(new LngLatAlt(0, 1));
        append.add(new LngLatAlt(0, 0));
        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, -1));
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, 1));

        LineString actual = LineStrings.merge(base, append);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void mergeAverted() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, 0));
        base.add(new LngLatAlt(0, -1));
        LineString append = new LineString();
        append.add(new LngLatAlt(0, 0));
        append.add(new LngLatAlt(0, 1));
        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, -1));

        LineString actual = LineStrings.merge(base, append);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void mergeAligned() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, 0));
        base.add(new LngLatAlt(0, 1));
        LineString append = new LineString();
        append.add(new LngLatAlt(0, 1));
        append.add(new LngLatAlt(1, 1));

        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(1, 1));

        LineString actual = LineStrings.merge(base, append);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void mergeNotConnected() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, 0));
        base.add(new LngLatAlt(0, 1));
        LineString append = new LineString();
        append.add(new LngLatAlt(1, 1));
        append.add(new LngLatAlt(1, 2));

        assertThrows(IllegalArgumentException.class, () ->
                LineStrings.merge(base, append));
    }

    @Test
    void mergeNull() {
        assertThrows(IllegalArgumentException.class, () ->
                LineStrings.merge(new LineString(), null));
        assertThrows(IllegalArgumentException.class, () ->
                LineStrings.merge(null, new LineString()));
        assertThrows(IllegalArgumentException.class, () ->
                LineStrings.merge(null, null));
    }

    @Test
    void appendPoint() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, 0));
        base.add(new LngLatAlt(0, 1));
        LngLatAlt coord = new LngLatAlt(1, 1);

        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(1, 1));

        LineString actual = LineStrings.append(base, coord);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void appendLine() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, 0));
        base.add(new LngLatAlt(0, 0.5));
        base.add(new LngLatAlt(0, 1));

        LineString append = new LineString();
        append.add(new LngLatAlt(0, 1));
        append.add(new LngLatAlt(1, 1));
        append.add(new LngLatAlt(2, 1));

        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, 0.5));
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(1, 1));
        expected.add(new LngLatAlt(2, 1));

        LineString actual = LineStrings.append(base, append);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void prependPoint() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, 0));
        base.add(new LngLatAlt(0, 1));
        LngLatAlt coord = new LngLatAlt(1, 1);

        LineString expected = new LineString();
        expected.add(new LngLatAlt(1, 1));
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, 1));

        LineString actual = LineStrings.prepend(base, coord);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void prependLine() {
        LineString prepend = new LineString();
        prepend.add(new LngLatAlt(0, 0));
        prepend.add(new LngLatAlt(0, 0.5));
        prepend.add(new LngLatAlt(0, 1));

        LineString base = new LineString();
        base.add(new LngLatAlt(0, 1));
        base.add(new LngLatAlt(1, 1));
        base.add(new LngLatAlt(2, 1));

        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, 0.5));
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(1, 1));
        expected.add(new LngLatAlt(2, 1));

        LineString actual = LineStrings.prepend(base, prepend);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void ofLineString() {
        LineString in = new LineString();
        in.add(new LngLatAlt(0, 0));
        in.add(new LngLatAlt(0, 1));
        in.add(new LngLatAlt(1, 1));

        LineString out = LineStrings.of(in);

        assertIterableEquals(in.getCoordinates(), out.getCoordinates());
        assertNotSame(in, out);
    }

    @Test
    void ofCoordinates() {
        LngLatAlt coord1 = new LngLatAlt(0, 0);
        LngLatAlt coord2 = new LngLatAlt(0, 1);
        LngLatAlt coord3 = new LngLatAlt(1, 1);
        LineString expected = new LineString(coord1, coord2, coord3);

        LineString actual = LineStrings.of(coord1, coord2, coord3);

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
        assertNotSame(expected, actual);
    }

    @Test
    void reverse() {
        LineString in = new LineString();
        in.add(new LngLatAlt(0, 0));
        in.add(new LngLatAlt(0, 1));
        in.add(new LngLatAlt(1, 1));

        LineString expected = new LineString();
        expected.add(new LngLatAlt(1, 1));
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(0, 0));

        LineString actual = LineStrings.reverse(in);

        assertEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void start() {
        LineString lineString = new LineString();
        lineString.add(new LngLatAlt(0, 0));
        lineString.add(new LngLatAlt(0, 1));
        lineString.add(new LngLatAlt(1, 1));

        LngLatAlt expected = new LngLatAlt(0,0);
        LngLatAlt actual = LineStrings.start(lineString);
        assertEquals(expected, actual);
    }

    @Test
    void end() {
            LineString lineString = new LineString();
            lineString.add(new LngLatAlt(0, 0));
            lineString.add(new LngLatAlt(0, 1));
            lineString.add(new LngLatAlt(1, 1));

            LngLatAlt expected = new LngLatAlt(1,1);
            LngLatAlt actual = LineStrings.end(lineString);
            assertEquals(expected, actual);
    }

    @Test
    void intersectsTrue() {
        LineString vertikal = LineStrings.of(new LngLatAlt(0,0),new LngLatAlt(0,10));
        LineString horizontal = LineStrings.of(new LngLatAlt(5,5),new LngLatAlt(-5,5));

        assertTrue(LineStrings.intersects(vertikal, horizontal));
    }@Test

    void intersectsFalse() {
        LineString vertikal = LineStrings.of(new LngLatAlt(0,0),new LngLatAlt(0,10));
        LineString horizontal = LineStrings.of(new LngLatAlt(5,5),new LngLatAlt(10,5));

        assertFalse(LineStrings.intersects(vertikal, horizontal));
    }
}