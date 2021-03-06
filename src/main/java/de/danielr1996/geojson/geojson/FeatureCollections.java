package de.danielr1996.geojson.geojson;

import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.MultiPoint;

public class FeatureCollections {
    public static LineString extractLineString(FeatureCollection featureCollection) {
        if (!featureCollection.getFeatures().isEmpty()) {
            return (LineString) featureCollection.getFeatures().get(0).getGeometry();
        }
        return new LineString();
    }
}
