package de.danielr1996.geojson;

import org.geojson.LngLatAlt;

public class HgtReaderMain {
    public static void main(String[] args) {
        System.out.println(
                new HgtReader().getElevationFromHgt(new LngLatAlt(47.42106, 10.98536)));
    }
}