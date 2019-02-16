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

    public static boolean areFacing(LineString lineString1, LineString lineString2) {
        LngLatAlt head1 = LineStrings.head(lineString1);
        LngLatAlt head2 = LineStrings.head(lineString2);
        return head1.equals(head2);
    }

    public static boolean areAverted(LineString lineString1, LineString lineString2) {
        LngLatAlt tail1 = LineStrings.tail(lineString1);
        LngLatAlt tail2 = LineStrings.tail(lineString2);
        return tail1.equals(tail2);
    }

    public static boolean areAligned(LineString lineString1, LineString lineString2) {
        LngLatAlt head1 = LineStrings.head(lineString1);
        LngLatAlt head2 = LineStrings.head(lineString2);
        LngLatAlt tail1 = LineStrings.tail(lineString1);
        LngLatAlt tail2 = LineStrings.tail(lineString2);

        return head1.equals(tail2) || head2.equals(tail1);
    }

    // Falls die Linien in unterschiedliche Richtungen laufen (d.h. beide Heads oder Tails sind gleich) die 2te Linie umdrehen
    public static Pair<LineString, LineString> alignDirection(LineString lineString1, LineString lineString2) {
        if (areAverted(lineString1, lineString2)) {
            lineString2 = LineStrings.reverse(lineString2);
        }
        if (areFacing(lineString1, lineString2)) {
            lineString1 = LineStrings.reverse(lineString1);
        }

        return Pair.<LineString, LineString>builder().first(lineString1).second(lineString2).build();
    }


    public static Feature merge(Feature feature1, Feature feature2) {
        LineString lineString1 = (LineString) feature1.getGeometry();
        LineString lineString2 = (LineString) feature2.getGeometry();

        if (lineString1 == null || lineString2 == null) {
            throw new IllegalArgumentException("LineString cannot be null");
        }
        LineString newLineString = new LineString();
        Pair<LineString, LineString> aligned = alignDirection(lineString1, lineString2);
        lineString1 = aligned.getFirst();
        lineString2 = aligned.getSecond();

        Feature feature = new Feature();
        feature.setProperty("name",feature2.getProperty("name"));
        if (!areAligned(lineString1, lineString2)) {
            System.err.printf("Lines %s and %s are not aligned\n", feature1.getProperty("name"), feature2.getProperty("name"));
            feature.setProperty("warn","notaligned");
        }
        Stream.concat(
                lineString1.getCoordinates().stream(),
                lineString2.getCoordinates().stream()
        )
//                .distinct()
                .forEach(newLineString::add);
        feature.setGeometry(newLineString);
        return feature;
    }

    public static LngLatAlt head(LineString lineString) {
        if (!lineString.getCoordinates().isEmpty()) {
            return lineString.getCoordinates().get(0);
        }
        return new LngLatAlt();
    }

    public static LngLatAlt tail(LineString lineString) {
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
