package de.danielr1996.geojson.topojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.Features;
import de.danielr1996.geojson.geojson.GeoJSONCollectors;
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
        if (definition.linesDeprecated != null && definition.lines != null) {
            throw new IllegalArgumentException("Cannot use lines and linesDeprecated in one Definition");
        }
        if (definition.linesDeprecated == null && definition.lines == null) {
            throw new IllegalArgumentException("No lines Found in Definition "+definition.name);
        }
        Stream<String> lineNames = Stream.empty();
        if (definition.linesDeprecated != null) {
            if(definition.linesDeprecated.size()<2){
                throw new IllegalArgumentException("Definition needs more than 2 lines");
            }
            lineNames = definition.linesDeprecated.stream()
                    .map(line -> line.name);
        }

        if (definition.lines != null) {
            if(definition.lines.size()<2){
                throw new IllegalArgumentException("Definition needs more than 2 lines");
            }
            lineNames = definition.lines.stream();
        }



        Stream<Feature> features = lineNames
                .map(name -> Pair.<String, InputStream>builder().first(name).second(Generator.class.getResourceAsStream(String.format("/base/rivers/%s.geo.json", name))).build())
                .map(pair -> Util.readFeatureCollection(pair.getSecond(), pair.getFirst()).getFeatures().get(0));
        LineString lineString = features
                .map(Features.extractTypedGeometry(LineString.class))
                .reduce(LineStrings::merge).get();
        Polygon polygon = lineString.getCoordinates().stream().collect(GeoJSONCollectors.toPolygon());
        polygon.getExteriorRing().add(polygon.getExteriorRing().get(0));
//        Polygon polygon = Polygons.fromLineString.apply(lineString);
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
