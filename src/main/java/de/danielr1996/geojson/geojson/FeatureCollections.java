package de.danielr1996.geojson.geojson;

import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.MultiPoint;

public class FeatureCollections {
    public static LineString extractLineString(FeatureCollection featureCollection) {
        return (LineString) featureCollection.getFeatures().get(0).getGeometry();
    }

    public static MultiPoint toMultiPoint(FeatureCollection featureCollection){
        MultiPoint multiPoint = new MultiPoint();

        extractLineString(featureCollection).getCoordinates().stream().forEach(multiPoint::add);

        return multiPoint;
    }
}
