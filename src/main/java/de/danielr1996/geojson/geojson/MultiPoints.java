package de.danielr1996.geojson.geojson;

import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class MultiPoints {
    public static Function<LineString, MultiPoint> fromLineString = (LineString lineString) -> {
        MultiPoint multiPoint = new MultiPoint();
        lineString.getCoordinates().stream().forEach(multiPoint::add);

        return multiPoint;
    };

    public static MultiPoint merge(MultiPoint m1, MultiPoint m2) {
        if (m1 == null && m2 != null) {
            return m2;
        }
        if (m2 == null && m1 != null) {
            return m1;
        }

        MultiPoint newMultiPoint = new MultiPoint();

        Stream.concat(
                m1.getCoordinates().stream(), m2.getCoordinates().stream()
        )
                .distinct()
                .forEach(newMultiPoint::add);

        return newMultiPoint;
    }

    public static Collector<LngLatAlt, MultiPoint, MultiPoint> collector(){
        return Collector.of(MultiPoint::new, MultiPoint::add, (left, right)->{
            left.getCoordinates().addAll(right.getCoordinates());
            return left;
        });

    }
}
