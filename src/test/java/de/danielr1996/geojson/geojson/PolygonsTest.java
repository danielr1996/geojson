package de.danielr1996.geojson.geojson;

import com.codepoetics.protonpack.StreamUtils;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PolygonsTest {
    private LngLatAlt linksunten = new LngLatAlt(0, 0);
    private LngLatAlt rechtsunten = new LngLatAlt(0, 10);
    private LngLatAlt rechtsoben = new LngLatAlt(10, 10);
    private LngLatAlt linksoben = new LngLatAlt(10, 0);
    private Polygon expected = new Polygon();

    @BeforeEach
    void setUp() {
        expected.setExteriorRing(Arrays.asList(linksunten, rechtsunten, rechtsoben, linksoben));
    }

    @Test
    void fromNonOverlappingMultiPoint() {
        MultiPoint multiPoint = new MultiPoint();

        multiPoint.add(linksunten);
        multiPoint.add(rechtsunten);
        multiPoint.add(rechtsoben);
        multiPoint.add(linksoben);

        Polygon actual = Polygons.fromMultiPoint(multiPoint);

        StreamUtils.zip(expected.getExteriorRing().stream(), actual.getExteriorRing().stream(), (ex, ac) -> {
            HashMap<String, LngLatAlt> map = new HashMap<>();
            map.put("expected", ex);
            map.put("actual", ac);
            return map;
        })
                .forEach(map -> assertEquals(map.get("expected"), map.get("actual")));
    }

    @Test
    void fromOverlappingMultiPoint() {
        MultiPoint multiPoint = new MultiPoint();

        multiPoint.add(linksunten);
        multiPoint.add(linksoben);
        multiPoint.add(rechtsunten);
        multiPoint.add(rechtsoben);

        Polygon actual = Polygons.fromMultiPoint(multiPoint);

        StreamUtils.zip(expected.getExteriorRing().stream(), actual.getExteriorRing().stream(), (ex, ac) -> {
            HashMap<String, LngLatAlt> map = new HashMap<>();
            map.put("expected", ex);
            map.put("actual", ac);
            return map;
        })
                .forEach(map -> assertEquals(map.get("expected"), map.get("actual")));
    }
}