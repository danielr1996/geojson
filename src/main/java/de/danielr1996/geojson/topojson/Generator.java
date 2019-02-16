package de.danielr1996.geojson.topojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.LineStrings;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import de.danielr1996.geojson.util.Pair;
import org.geojson.*;

import java.io.InputStream;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Generator {
    public static Feature generate(Definition definition) {
        Stream<Feature> features = definition.lines.stream()
                .map(line -> Pair.<String, InputStream>builder().first(line.name).second(Generator.class.getResourceAsStream(String.format("/base/rivers/%s.geo.json", line.name))).build())
                .map(pair -> Util.readFeatureCollection(pair.getSecond(), pair.getFirst()).getFeatures().get(0));
        Feature lineString = features
                .reduce(LineStrings::merge).get();
        Polygon polygon = Polygons.fromLineString.apply((LineString) lineString.getGeometry());
        Feature feature = new Feature();
        if (definition.properties != null) {
            feature.setProperty("name", definition.name);
            feature.setProperty("stroke", definition.properties.getStroke());
            feature.setProperty("stroke-opacity", definition.properties.getStrokeOpacity());
            feature.setProperty("stroke-width", definition.properties.getStrokeWidth());
            feature.setProperty("fill", definition.properties.getFill());
            feature.setProperty("fill-opacity", definition.properties.getFillOpacity());
        }
        feature.setGeometry(polygon);
        return feature;
    }


}
