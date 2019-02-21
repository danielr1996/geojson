package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;

public class Features {
    public static Feature ofGeometry(GeoJsonObject geoJsonObject){
        Feature feature = new Feature();
        feature.setGeometry(geoJsonObject);
        return feature;
    }
}
