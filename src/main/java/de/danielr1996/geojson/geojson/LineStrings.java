package de.danielr1996.geojson.geojson;

import de.danielr1996.geojson.util.Pair;
import org.geojson.*;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineStrings {
    public static LineString fromFeatureCollection(FeatureCollection featureCollection) {
        LineString lineString = new LineString();
        featureCollection.getFeatures().stream()
                .map(Feature::getGeometry)
                .map(Points::fromGeometry)
                .map(Point::getCoordinates)
                .forEach(lineString::add);
        return lineString;
    }

    public static LineString ofFeature(Feature feature) {
        GeoJsonObject geoJsonObject = feature.getGeometry();
        if (!(geoJsonObject instanceof LineString)) {
            throw new IllegalArgumentException("Feature does not contain LineString");
        }

        return (LineString) geoJsonObject;
    }

    public static LineString ofLineString(LineString lineString) {
        LineString newLineString = new LineString();
        newLineString.setCoordinates(lineString.getCoordinates());
        return newLineString;
    }

    public static boolean areFacing(LineString lineString1, LineString lineString2) {
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
    }


    public static Feature merge(Feature baseFeature, Feature appendFeature) {
        if (baseFeature == null || appendFeature == null) {
            throw new IllegalArgumentException("LineString cannot be null");
        }

        LineString base = LineStrings.ofFeature(baseFeature);
        LineString append = LineStrings.ofFeature(appendFeature);
        LineString newLineString;
        if (end(base).equals(start(append))) {
            newLineString = append(base, append);
        } else if (start(base).equals(end(append))) {
            newLineString = prepend(base, append);
        } else if (start(base).equals(start(append))) {
            newLineString = prepend(base, reverse(append));
        } else if (end(base).equals(end(append))) {
            newLineString = append(base, reverse(append));
//        } else if (start(base).equals(end(append))) {
//            newLineString = prepend(base, append);
       /* } else if (end(base).equals(start(append))) {
//            newLineString = append(base, append);*/
        /*} else if (start(base).equals(start(append))) {
            newLineString = prepend(base, reverse(append));*/
       /* } else if (end(base).equals(end(append))) {
            newLineString = append(base, reverse(append));*/
        } else {
            throw new IllegalArgumentException("Not connected");
        }
        return Features.ofLineString(newLineString);
    }

    public static double distance(LngLatAlt l1, LngLatAlt l2) {
        return Math.sqrt(Math.sqrt(l1.getLatitude() - l2.getLatitude()) + Math.sqrt(l1.getLongitude() - l2.getLongitude()));
    }

    public static LineString append(LineString base, LngLatAlt coord) {
        base.add(coord);
        return base;
    }

    public static LineString append(LineString base, LineString append) {
        LineString newLineString = LineStrings.ofLineString(base);
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

    public static LineString reverse(LineString lineString) {
        Collections.reverse(lineString.getCoordinates());
        return lineString;
    }


    public static String toString(LineString lineString) {
        return lineString.getCoordinates().stream().map(Coordinates::toString).collect(Collectors.joining(" -> "));
    }
}
