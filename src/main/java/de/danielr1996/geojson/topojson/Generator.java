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
        Supplier<Stream<Feature>> features = () -> definition.lines.stream()
                .map(line -> Pair.<String, InputStream>builder().first(line.name).second(Generator.class.getResourceAsStream(String.format("/base/rivers/%s.geo.json", line.name))).build())
                .map(pair -> Util.readFeatureCollection(pair.getSecond(), pair.getFirst()).getFeatures().get(0));

        // FIXME: Aus .... Gründen .... wird bei reduce das letze und vorletze Element nicht richtig verglichen, daher muss das letze Element noch manuell aligned werden
       /* List<LineString> lineStringList = lineDefinitions.get().collect(Collectors.toList());

        LineString first = lineStringList.get(0);
        LineString second = lineStringList.get(1);
        LineString last = lineStringList.get(lineStringList.size() - 1);
        LineString nextToLast = lineStringList.get(lineStringList.size() - 2);
        LineString nextToNextToLast = lineStringList.get(lineStringList.size() - 3);

        System.out.println(LineStrings.areAligned(nextToLast, last));
        System.out.println(LineStrings.areAligned(last, first));*/

       /*         Pair<LineString, LineString> aligned = LineStrings.alignDirection(first, last);
        lineStringList.set(0, aligned.getFirst());
        lineStringList.set(lineStringList.size() - 1, aligned.getSecond());
        lineDefinitions = lineStringList.stream();*/
        // FIXME ENDE

        Feature lineString = features.get()
                .reduce((Feature a, Feature b) -> {
//                    System.out.println(a);
//                    System.out.println(b);
//                    System.out.println();
                    Feature feature = LineStrings.merge(a, b);
//                    if("notaligned".equals(feature.getProperty("warn"))){
//                        System.out.println(Util.geoJsonObjectToString(a));
//                        System.out.println(Util.geoJsonObjectToString(a));

//                    }
                    return feature;
                }).get();
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
