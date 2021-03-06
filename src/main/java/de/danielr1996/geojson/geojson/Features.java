package de.danielr1996.geojson.geojson;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;

import java.util.function.Function;

public class Features {
    public static Feature of(Feature feature){
        Feature f = new Feature();
        f.setGeometry(feature.getGeometry());
        f.setProperties(feature.getProperties());
        f.setId(feature.getId());
        f.setBbox(feature.getBbox());
        f.setCrs(feature.getCrs());
        return f;
    }

    public static Feature ofGeometry(GeoJsonObject geoJsonObject) {
        Feature feature = new Feature();
        feature.setGeometry(geoJsonObject);
        return feature;
    }


    public static <T> Function<Feature, T> extractTypedGeometry(Class<T> clazz) {
        return (Feature feature) -> {
            GeoJsonObject geometry = feature.getGeometry();

            if (clazz.isInstance(geometry)) {
                return (T) geometry;
            } else {
                throw new IllegalArgumentException(String.format("FeatureCollection does not contain request type %s", clazz.getCanonicalName()));
            }
        };
    }
}
