package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Coordinates {
    public static String toDecimalString(LngLatAlt coord){
        return String.format("%f, %f", coord.getLongitude(), coord.getLatitude());
    }

    public static String toHexagesimalString(LngLatAlt coord){
        return String.format("%s, %s", singleToHexagesimalString(coord.getLongitude()),singleToHexagesimalString(coord.getLatitude()));
    }

    public static  String singleToHexagesimalString(double part){
        double deg = Math.floor(part);
        double min = (part-deg)*60;
        double sec = (min- Math.floor(min))*60;
        return String.format(Locale.ROOT,"%.0f %.0f %.2f", deg, min, sec);
    }

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
        double newLatitude = coord.getLatitude() + offset;
        if(newLatitude>90){
            newLatitude=90;
        }
        newCoord.setLatitude(newLatitude);
        return newCoord;
    };

    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveSouth = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        double newLatitude = coord.getLatitude() - offset;
        if(newLatitude<-90){
            newLatitude=-90;
        }
        newCoord.setLatitude(newLatitude);
        return newCoord;
    };

    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveWest = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        double newLatitude = coord.getLongitude() - offset;
        if(newLatitude < -180){
            double rest = newLatitude%180;
            newLatitude = 180+rest;
        }
        newCoord.setLongitude(newLatitude);
        return newCoord;
    };

    public static Function<Double, Function<LngLatAlt, LngLatAlt>> moveEast = offset -> coord -> {
        LngLatAlt newCoord = Coordinates.of(coord);
        double newLatitude = coord.getLongitude() + offset;
        if(newLatitude > 180){
            double rest = newLatitude%180;
            newLatitude = -180+rest;
        }
        newCoord.setLongitude(newLatitude);
        return newCoord;
    };
}
