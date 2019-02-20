package de.danielr1996.geojson.linearalgebra;

import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorsTest {

    @Test
    void between() {
        LngLatAlt coord1 = new LngLatAlt(4,-6);
        LngLatAlt coord2 = new LngLatAlt(2,-8);

        Vector expected = Vector.builder().a(-2).b(-2).build();
        Vector actual = Vectors.between(coord2, coord1);

        assertEquals(expected, actual);
    }
}