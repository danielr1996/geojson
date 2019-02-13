package de.danielr1996.geojson.geojson;

import org.geojson.MultiPoint;

import java.util.stream.Stream;

public class MultiPoints {
    public static MultiPoint merge(MultiPoint m1, MultiPoint m2) {
        if(m1 == null && m2 != null){
            return m2;
        }
        if(m2 == null && m1 != null){
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
}
