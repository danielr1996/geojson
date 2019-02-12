package de.danielr1996.geojson.geojson;

import org.geojson.GeoJsonObject;
import org.geojson.Point;

public class Points {

    public static Point fromGeometry(GeoJsonObject geometry){
        return (Point)geometry;
    }
}
