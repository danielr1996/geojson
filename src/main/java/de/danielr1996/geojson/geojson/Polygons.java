package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.util.ArrayList;
import java.util.List;


public class Polygons {
    public static Polygon fromMultiPoint(MultiPoint multiPoint){
        Polygon polygon = new Polygon();
        List<LngLatAlt> points = new ArrayList<>(multiPoint.getCoordinates());
        points.add(multiPoint.getCoordinates().get(0));
        polygon.setExteriorRing(points);
        return polygon;
    }
}
