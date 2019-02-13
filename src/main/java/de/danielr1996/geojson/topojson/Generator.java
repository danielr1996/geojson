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

public class Generator {
    public static Feature generate(String name) {
        Definition definition = Definitions.read(new File("C:/workspace/geojson/src/main/res/generate/" + name + ".json"));
        MultiPoint multipoint =
                definition.lines.stream()
                        .map(line -> {
                            File f = new File(String.format("C:/workspace/geojson/src/main/res/base/rivers/%s.geojson", line.name));
                            LineString lineString = FeatureCollections.extractLineString(Util.readFeatureCollection(f));
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
            feature.setProperty("stroke", definition.properties.stroke);
            feature.setProperty("strokeOpacity", definition.properties.strokeOpacity);
            feature.setProperty("strokeWidth", definition.properties.strokeWidth);
            feature.setProperty("fill", definition.properties.fill);
            feature.setProperty("fillOpacity", definition.properties.fillOpacity);
        }
        feature.setGeometry(polygon);
        return feature;
    }


}
