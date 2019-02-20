package de.danielr1996.geojson.geojson;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.util.ArrayList;
import java.util.Comparator;
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

    public static Function<LineString, Polygon> fromLineString = (LineString lineString) -> {
        return fromMultiPoint.compose(MultiPoints.fromLineString).apply(lineString);
    };

    public static Function<Polygon, LngLatAlt> findNorth = polygon -> polygon
            .getExteriorRing()
            .stream()
            .sorted(Comparator.comparingDouble(LngLatAlt::getLongitude))
            .max(Comparator.comparingDouble(LngLatAlt::getLatitude))
            .orElse(new LngLatAlt());

    public static Function<Polygon, LngLatAlt> findSouth = polygon -> polygon
            .getExteriorRing()
            .stream()
            .sorted(Comparator.comparingDouble(LngLatAlt::getLongitude))
            .min(Comparator.comparingDouble(LngLatAlt::getLatitude))
            .orElse(new LngLatAlt());

    public static Function<Polygon, LngLatAlt> findWest = polygon -> polygon
            .getExteriorRing()
            .stream()
            .sorted(Comparator.comparingDouble(LngLatAlt::getLatitude))
            .min(Comparator.comparingDouble(LngLatAlt::getLongitude))
            .orElse(new LngLatAlt());

    public static Function<Polygon, LngLatAlt> findEast = polygon -> polygon
            .getExteriorRing()
            .stream()
            .sorted(Comparator.comparingDouble(LngLatAlt::getLatitude))
            .max(Comparator.comparingDouble(LngLatAlt::getLongitude))
            .orElse(new LngLatAlt());



    public static Function<Polygon, MultiPoint> raster = polygon -> {
        Rectangle boundingRect = Polygons.boundingRect.apply(polygon);
        MultiPoint raster = Rectangle.raster.apply(10).apply(boundingRect);

        return raster;
    };

    public static Function<Polygon, Rectangle> boundingRect = polygon->{
        LngLatAlt north = Polygons.findNorth.apply(polygon);
        LngLatAlt west = Polygons.findWest.apply(polygon);
        LngLatAlt south = Polygons.findSouth.apply(polygon);
        LngLatAlt east = Polygons.findEast.apply(polygon);
        LngLatAlt northWest = new LngLatAlt(west.getLongitude(), north.getLatitude());
        LngLatAlt northEast = new LngLatAlt(east.getLongitude(), north.getLatitude());
        LngLatAlt southWest = new LngLatAlt(west.getLongitude(), south.getLatitude());
        LngLatAlt southEast = new LngLatAlt(east.getLongitude(), south.getLatitude());

       return Rectangle.builder()
               .southEast(southEast)
               .southWest(southWest)
               .northEast(northEast)
               .northWest(northWest)
               .build();
    };

    public static Function<LngLatAlt, Function<Polygon, Boolean>> contains = coord->polygon->{
      return true;
    };
}
