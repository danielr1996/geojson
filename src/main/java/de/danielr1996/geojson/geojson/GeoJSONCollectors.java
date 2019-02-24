package de.danielr1996.geojson.geojson;

import org.geojson.*;

import java.util.ArrayList;
import java.util.stream.Collector;

public class GeoJSONCollectors {
    public static Collector<LngLatAlt, MultiPoint, MultiPoint> toMultiPoint() {
        return Collector.of(MultiPoint::new, MultiPoint::add, (left, right) -> {
            left.getCoordinates().addAll(right.getCoordinates());
            return left;
        });
    }

    public static Collector<LngLatAlt, Polygon, Polygon> toPolygon() {
        return Collector.of(
                () -> {
                    Polygon polygon = new Polygon();
                    polygon.setExteriorRing(new ArrayList<>());
                    return polygon;
                },
                (polygon, coord) -> polygon.getExteriorRing().add(coord),
                (left, right) -> {
                    left.getExteriorRing().addAll(right.getExteriorRing());
                    return left;
                });
    }

    public static Collector<Feature, FeatureCollection, FeatureCollection> toFeatureCollection() {
        return Collector.of(
                FeatureCollection::new,
                FeatureCollection::add,
                (left, right) -> {
                    left.getFeatures().addAll(right.getFeatures());
                    return left;
                }
        );
    }
}
