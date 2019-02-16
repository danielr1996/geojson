package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LineStringsTest {

    @Test
    void areAligned() {
        LineString line1 = (LineString) Util.readFeatureCollection(new File("C:\\Users\\Daniel\\Documents\\Entwicklung\\geojson\\src\\main\\res\\base\\rivers\\Partnach-Loisach-Krankerbach.geo.json")).getFeatures().get(0).getGeometry();
        LineString line2 = (LineString) Util.readFeatureCollection(new File("C:\\Users\\Daniel\\Documents\\Entwicklung\\geojson\\src\\main\\res\\base\\rivers\\Krankerbach-Partnach-Quelle.geo.json")).getFeatures().get(0).getGeometry();
        boolean aligned = LineStrings.areAligned(line1, line2);

        assertTrue(aligned);
    }

    @Test
    void mergeFacing() {
        LineString base = new LineString();
        base.add(new LngLatAlt(0, -1));
//        base.add(new LngLatAlt(0, -0.5));
        base.add(new LngLatAlt(0, 0));
        LineString append = new LineString();
        append.add(new LngLatAlt(0, 1));
//        append.add(new LngLatAlt(0, 0.5));
        append.add(new LngLatAlt(0, 0));
        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, -1));
//        expected.add(new LngLatAlt(0, -0.5));
        expected.add(new LngLatAlt(0, 0));
//        expected.add(new LngLatAlt(0, 0.5));
        expected.add(new LngLatAlt(0, 1));

        LineString actual = LineStrings.ofFeature(LineStrings.merge(Features.ofLineString(base), Features.ofLineString(append)));

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void mergeAverted() {
        LineString lineString1 = new LineString();
        lineString1.add(new LngLatAlt(0, 0));
        lineString1.add(new LngLatAlt(0, -1));
        LineString lineString2 = new LineString();
        lineString2.add(new LngLatAlt(0, 0));
        lineString2.add(new LngLatAlt(0, 1));
        LineString expected = new LineString();
        expected.add(new LngLatAlt(0, 1));
        expected.add(new LngLatAlt(0, 0));
        expected.add(new LngLatAlt(0, -1));

        LineString actual = LineStrings.ofFeature(LineStrings.merge(Features.ofLineString(lineString1), Features.ofLineString(lineString2)));

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

        LineString actual = LineStrings.ofFeature(LineStrings.merge(Features.ofLineString(base), Features.ofLineString(append)));

        assertIterableEquals(expected.getCoordinates(), actual.getCoordinates());
    }

    @Test
    void mergeNotConnected() {
        LineString lineString1 = new LineString();
        lineString1.add(new LngLatAlt(0, 0));
        lineString1.add(new LngLatAlt(0, 1));
        LineString lineString2 = new LineString();
        lineString2.add(new LngLatAlt(1, 1));
        lineString2.add(new LngLatAlt(1, 2));

        assertThrows(IllegalArgumentException.class, () ->
                LineStrings.ofFeature(LineStrings.merge(Features.ofLineString(lineString1), Features.ofLineString(lineString2))));
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
        append.add(new LngLatAlt(0,1));
        append.add(new LngLatAlt(1,1));
        append.add(new LngLatAlt(2,1));

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
    void prepend() {
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
        base.add(new LngLatAlt(0,1));
        base.add(new LngLatAlt(1,1));
        base.add(new LngLatAlt(2,1));

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
    void ofLineString(){
        LineString in = new LineString();
        in.add(new LngLatAlt(0, 0));
        in.add(new LngLatAlt(0, 1));
        in.add(new LngLatAlt(1, 1));

        LineString out = LineStrings.ofLineString(in);

        assertIterableEquals(in.getCoordinates(), out.getCoordinates());
        assertNotSame(in, out);
    }
}