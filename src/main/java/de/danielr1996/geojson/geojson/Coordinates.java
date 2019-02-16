package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;

public class Coordinates {
    public static  String toString(LngLatAlt lngLatAlt) {
        return String.format("%.1f|%.1f", lngLatAlt.getLongitude(), lngLatAlt.getLatitude());
    }
}
