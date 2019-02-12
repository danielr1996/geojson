package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;

public class FeatureCollections {
    public static LineString toLineString(FeatureCollection featureCollection) {
        return (LineString) featureCollection.getFeatures().get(0).getGeometry();
    }
}
