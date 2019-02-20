package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Coordinates {
    public static String toString(LngLatAlt lngLatAlt) {
        return String.format("%.1f|%.1f", lngLatAlt.getLongitude(), lngLatAlt.getLatitude());
    }

    public static LngLatAlt of(LngLatAlt coord) {
        LngLatAlt newCoord = new LngLatAlt();
        newCoord.setLatitude(coord.getLatitude());
        newCoord.setLongitude(coord.getLongitude());
        newCoord.setAltitude(coord.getAltitude());
        return newCoord;
    }


    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveNorth = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        newCoord.setLatitude(coord.getLatitude() + offset);
        return newCoord;
    };

    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveSouth = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        newCoord.setLatitude(coord.getLatitude() - offset);
        return newCoord;
    };

    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveWest = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        newCoord.setLongitude(coord.getLongitude() - offset);
        return newCoord;
    };

    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveEast = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        newCoord.setLongitude(coord.getLongitude() + offset);
        return newCoord;
    };
}
