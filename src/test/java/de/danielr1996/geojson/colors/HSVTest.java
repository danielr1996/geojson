package de.danielr1996.geojson.colors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HSVTest {

    @Test
    void rgbBaseTest() {
        assertEquals(new RGB(255,255,0),new RGB(255,255,0));
        assertNotEquals(new RGB(0,255,0),new RGB(255,255,0));
    }

    @Test
    void hsvToRed() {
       HSV hsv = new HSV(0,1,1);
       RGB expected = new RGB(255,0,0);
       RGB actual = hsv.toRGB();

       assertEquals(expected, actual);
    }

    @Test
    void hsvToGreen() {
        HSV hsv = new HSV(120,1,1);
        RGB expected = new RGB(0,255,0);
        RGB actual = hsv.toRGB();

        assertEquals(expected, actual);
    }

    @Test
    void hsvToBlue() {
        HSV hsv = new HSV(240,1,1);
        RGB expected = new RGB(0,0,255);
        RGB actual = hsv.toRGB();

        assertEquals(expected, actual);
    }

    @Test
    void hsvToCyan() {
        HSV hsv = new HSV(180,1,1);
        RGB expected = new RGB(0,255,255);
        RGB actual = hsv.toRGB();

        assertEquals(expected, actual);
    }

    @Test
    void hsvToYellow() {
        HSV hsv = new HSV(60,1,1);
        RGB expected = new RGB(255,255,0);
        RGB actual = hsv.toRGB();

        assertEquals(expected, actual);
    }

    @Test
    void hsvToMagenta() {
        HSV hsv = new HSV(300,1,1);
        RGB expected = new RGB(255,0,255);
        RGB actual = hsv.toRGB();

        assertEquals(expected, actual);
    }
}