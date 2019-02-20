package de.danielr1996.geojson.linearalgebra;

import lombok.Builder;
import lombok.Data;
import org.geojson.LngLatAlt;

public class Vectors {
    public static Vector of(LngLatAlt coord){
        return Vector.builder().a(coord.getLongitude()).b(coord.getLongitude()).build();
    }

    public static Vector between(LngLatAlt coord1, LngLatAlt coord2){
        return Vectors.of(coord2).substract(Vectors.of(coord1));
    }
}
