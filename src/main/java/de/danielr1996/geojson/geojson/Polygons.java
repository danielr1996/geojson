package de.danielr1996.geojson.geojson;

import one.util.streamex.StreamEx;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;


public class Polygons {
    public static BiFunction<ToDoubleFunction<LngLatAlt>, ToDoubleFunction<LngLatAlt>, Function<Polygon, LngLatAlt>> findFarthest =
            (sort, max) -> polygon -> polygon
                    .getExteriorRing()
                    .stream()
                    .sorted(Comparator.comparingDouble(sort))
                    .max(Comparator.comparingDouble(max))
                    .orElse(new LngLatAlt());

//    public static Function<Polygon, LngLatAlt> findNorth = findFarthest.apply(LngLatAlt::getLongitude,LngLatAlt::getLatitude);
//    public static Function<Polygon, LngLatAlt> findSouth = findFarthest.apply(LngLatAlt::getLongitude,LngLatAlt::getLatitude);
//    public static Function<Polygon, LngLatAlt> findWest = findFarthest.apply(LngLatAlt::getLongitude,LngLatAlt::getLatitude);
//    public static Function<Polygon, LngLatAlt> findEast = findFarthest.apply(LngLatAlt::getLongitude,LngLatAlt::getLatitude);

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


    public static Function<Integer, Function<Polygon, MultiPoint>> raster = steps -> polygon ->
            Rectangle.raster
                    .apply(steps)
                    .compose(Polygons.boundingRect)
                    .apply(polygon)
                    .getCoordinates()
                    .stream()
                    .filter(Polygons.contains.apply(polygon))
                    .collect(GeoJSONCollectors.toMultiPoint());

    public static Function<Polygon, Rectangle> boundingRect = polygon -> {
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

    public static Function<Polygon, Predicate<LngLatAlt>> contains = polygon -> coord -> {
        LineString strahl = new LineString();
        strahl.add(coord);
        strahl.add(Coordinates.moveWest.apply(1000D).apply(coord));
        long numberIntersections = StreamEx.of(polygon.getExteriorRing()).pairMap((coord1, coord2) -> {
            LineString lineString = new LineString();
            lineString.add(coord1);
            lineString.add(coord2);
            return lineString;
        }).map(lineSegment -> LineStrings.intersects(strahl, lineSegment))
                .filter(intersects -> intersects)
                .count();

        return numberIntersections % 2 != 0;
    };
}
