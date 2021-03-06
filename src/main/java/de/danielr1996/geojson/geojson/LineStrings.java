package de.danielr1996.geojson.geojson;

import org.geojson.*;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.stream.Collectors;

public class LineStrings {
    public static LineString of(LineString lineString) {
        LineString newLineString = new LineString();
        newLineString.setCoordinates(lineString.getCoordinates());
        return newLineString;
    }

    public static LineString of(LngLatAlt... points){
        return new LineString(points);
    }

    public static LineString merge(LineString base, LineString append) throws IllegalArgumentException{
        if (base == null || append == null) {
            throw new IllegalArgumentException("LineString cannot be null");
        }

        LineString newLineString;
        if (end(base).equals(start(append))) {
            newLineString = append(base, append);
        } else if (start(base).equals(end(append))) {
            newLineString = prepend(base, append);
        } else if (start(base).equals(start(append))) {
            newLineString = prepend(base, reverse(append));
        } else if (end(base).equals(end(append))) {
            newLineString = append(base, reverse(append));
        } else {
            throw new IllegalArgumentException("Not connected");
        }
        return newLineString;
    }

    public static LineString append(LineString base, LngLatAlt coord) {
        base.add(coord);
        return base;
    }

    public static LineString append(LineString base, LineString append) {
        LineString newLineString = LineStrings.of(base);
        if (!end(base).equals(start(append))) {
            throw new IllegalArgumentException("Base and End are not aligned");
        }

        append.getCoordinates().stream().skip(1).forEach(newLineString::add);

        return newLineString;
    }

    public static LineString prepend(LineString base, LngLatAlt coord) {
        base.getCoordinates().add(0, coord);
        return base;
    }

    public static LineString prepend(LineString base, LineString prepend) {
        return append(prepend, base);
    }

    public static LineString reverse(LineString lineString) {
        Collections.reverse(lineString.getCoordinates());
        return lineString;
    }

    public static LngLatAlt start(LineString lineString) {
        if (!lineString.getCoordinates().isEmpty()) {
            return lineString.getCoordinates().get(0);
        }
        return new LngLatAlt();
    }

    public static LngLatAlt end(LineString lineString) {
        if (!lineString.getCoordinates().isEmpty()) {
            return lineString.getCoordinates().get(lineString.getCoordinates().size() - 1);
        }
        return new LngLatAlt();
    }


    public static String toString(LineString lineString) {
        return lineString.getCoordinates().stream().map(Coordinates::toString).collect(Collectors.joining(" -> "));
    }

    public static boolean intersects(LineString lineString1, LineString lineString2) {
        Line2D line1 = new Line2D.Double(
                lineString1.getCoordinates().get(0).getLongitude(),
                lineString1.getCoordinates().get(0).getLatitude(),
                lineString1.getCoordinates().get(1).getLongitude(),
                lineString1.getCoordinates().get(1).getLatitude());
        Line2D line2 = new Line2D.Double(
                lineString2.getCoordinates().get(0).getLongitude(),
                lineString2.getCoordinates().get(0).getLatitude(),
                lineString2.getCoordinates().get(1).getLongitude(),
                lineString2.getCoordinates().get(1).getLatitude());
        return line1.intersectsLine(line2);
    }

//    public static Function<Double, Function<Double, Function<Double, Double>>> lineareGleichung = m -> t -> x -> m * x + t;


    /*public static double winkel(LngLatAlt coord1, LngLatAlt coord2, LngLatAlt reference) {
        double a = Math.abs(coord1.getLongitude() - reference.getLongitude());
        double b = Math.abs(coord2.getLatitude() - reference.getLatitude());
        double alpha = Math.toDegrees(Math.atan(a / b));
        return alpha;
    }

    public static double winkel(LngLatAlt coord1, LngLatAlt coord2) {
        LngLatAlt hilfsPunkt = new LngLatAlt();
        hilfsPunkt.setLatitude(coord1.getLatitude());
        hilfsPunkt.setLongitude(coord2.getLongitude());
        return winkel(coord1, coord2, hilfsPunkt);
    }

    public static LineareFunktion toLinearFunction(LineString lineString){

    }*/


    /*public static boolean areFacing(LineString lineString1, LineString lineString2) {
        LngLatAlt head1 = LineStrings.start(lineString1);
        LngLatAlt head2 = LineStrings.start(lineString2);
        return head1.equals(head2);
    }

    public static boolean areAverted(LineString lineString1, LineString lineString2) {
        LngLatAlt tail1 = LineStrings.end(lineString1);
        LngLatAlt tail2 = LineStrings.end(lineString2);
        return tail1.equals(tail2);
    }

    public static boolean areAligned(LineString lineString1, LineString lineString2) {
        LngLatAlt head1 = LineStrings.start(lineString1);
        LngLatAlt head2 = LineStrings.start(lineString2);
        LngLatAlt tail1 = LineStrings.end(lineString1);
        LngLatAlt tail2 = LineStrings.end(lineString2);

        return head1.equals(tail2) || head2.equals(tail1);
    }*/
}
