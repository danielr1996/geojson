package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.LineString;

public class Features {
    public static Feature ofLineString(LineString lineString){
        Feature feature = new Feature();
        feature.setGeometry(lineString);
        return feature;
    }
}
