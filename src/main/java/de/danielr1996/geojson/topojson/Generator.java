package de.danielr1996.geojson.topojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.LineStrings;
import de.danielr1996.geojson.geojson.MultiPoints;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import org.geojson.Feature;
import org.geojson.LineString;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.io.File;
import java.io.InputStream;

public class Generator {
    public static Feature generate(Definition definition) {
//        Definition definition = Definitions.read(new File("C:/workspace/geojson/src/main/res/generate/" + name + ".json"));
        MultiPoint multipoint =
                definition.lines.stream()
                        .map(line -> {
//                            File f = new File(String.format("C:/workspace/geojson/src/main/res/base/rivers/%s.geojson", line.name));
                            InputStream inputStream = Generator.class.getResourceAsStream(String.format("/base/rivers/%s.geojson", line.name));
                            LineString lineString = FeatureCollections.extractLineString(Util.readFeatureCollection(inputStream));
                            if ("reverse".equals(line.order)) {
                                lineString = LineStrings.reverse(lineString);
                            }
                            return lineString;
                        })
                        .map(LineStrings::toMultiPoint)
                        .reduce(null, MultiPoints::merge);
        Polygon polygon = Polygons.fromMultiPoint((MultiPoint) multipoint);
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
