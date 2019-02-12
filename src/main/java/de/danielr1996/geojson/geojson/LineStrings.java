package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.Point;

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

    public static LineString merge(LineString lineString1, LineString lineString2) {
        if(lineString1 == null && lineString2 != null){
            return lineString2;
        }
        if(lineString2 == null && lineString1 != null){
            return lineString1;
        }
        LineString newLineString = new LineString();

        Stream.concat(
                lineString1.getCoordinates().stream(),
                lineString2.getCoordinates().stream()
        )
                .distinct()
                .forEach(newLineString::add);
        return newLineString;
    }
}
