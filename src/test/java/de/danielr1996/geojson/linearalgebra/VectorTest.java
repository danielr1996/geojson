package de.danielr1996.geojson.linearalgebra;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void substract() {
    }

    @Test
    void pointProduct() {
    }

    @Test
    void abs() {
        Vector vector = Vector.builder().a(5).b(3).build();
        double expected = Math.sqrt(34);
        double actual = vector.abs();

        assertEquals(expected, actual, 0.1);
    }

    @Test
    void winkel() {
        Vector v1 = Vector.builder().a(1).b(5).build();
        Vector v2 = Vector.builder().a(3).b(7).build();

        System.out.println(v1.winkel(v2));
    }
}