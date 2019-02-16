package de.danielr1996.geojson.geojson;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class Polygons {
    public static Function<MultiPoint, Polygon> fromMultiPoint = (MultiPoint multiPoint) -> {
        Polygon polygon = new Polygon();
        List<LngLatAlt> points = new ArrayList<>(multiPoint.getCoordinates());
        points.add(multiPoint.getCoordinates().get(0));
        polygon.setExteriorRing(points);
        return polygon;
    };

   /* public static Polygon fromMultiPointOld(MultiPoint multiPoint) {
        Polygon polygon = new Polygon();
        List<LngLatAlt> points = new ArrayList<>(multiPoint.getCoordinates());
        points.add(multiPoint.getCoordinates().get(0));
        polygon.setExteriorRing(points);
        return polygon;
    }*/

    public static Function<LineString, Polygon> fromLineString = (LineString lineString) -> {
        return fromMultiPoint.compose(MultiPoints.fromLineString).apply(lineString);
    };

    /*public static Polygon fromLineStringOld(LineString lineString) {
        return fromMultiPointOld(
                MultiPoints.fromLineStringOld(lineString)
        );
    }*/


}
