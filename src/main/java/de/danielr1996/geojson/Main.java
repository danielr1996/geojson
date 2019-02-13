package de.danielr1996.geojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.LineStrings;
import de.danielr1996.geojson.geojson.MultiPoints;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import de.danielr1996.geojson.topojson.Definition;
import de.danielr1996.geojson.topojson.Definitions;
import de.danielr1996.geojson.topojson.Generator;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Stream<String> polygons = Stream.of("Silvretta", "Verwall","Samnaun");
        FeatureCollection featureCollection = new FeatureCollection();

        polygons
                .map(Generator::generate)
                .map(polygon -> {
                    Feature feature = new Feature();
                    feature.setGeometry(polygon);
                    return feature;
                })
                .forEach(featureCollection::add);
            Util.writeGeoJsonObject(featureCollection,new File("C:/workspace/geojson/src/main/res/generated/FeatureCollection.geojson"));
//        Util.writeGeoJsonObject(verwall,new File("C:/workspace/geojson/src/main/res/generated/Verwall.geojson"));

    }
}
