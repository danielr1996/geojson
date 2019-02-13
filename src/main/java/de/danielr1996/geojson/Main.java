package de.danielr1996.geojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.LineStrings;
import de.danielr1996.geojson.geojson.MultiPoints;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import de.danielr1996.geojson.topojson.Definition;
import de.danielr1996.geojson.topojson.Definitions;
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

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Definition silvrettaDefintion = Definitions.read(new File("C:/workspace/geojson/src/main/res/generate/Silvretta.json"));

        System.out.println(silvrettaDefintion);

        MultiPoint silvrettaMultipoint =
        silvrettaDefintion.lines.stream()
                .map(line-> String.format("C:/workspace/geojson/src/main/res/base/rivers/%s.geojson", line))
//                Files.list(Paths.get(ClassLoader.getSystemResource("").toURI()))
//                .map(Path::toString)
//                .filter(path -> path.endsWith("geojson"))
                .map(File::new)
                .map(Util::readFeatureCollection)
                .map(FeatureCollections::toMultiPoint)
                .reduce(null, MultiPoints::merge);
        Polygon silvretta = Polygons.fromMultiPoint((MultiPoint)silvrettaMultipoint);
//                .forEach(System.out::println);
        Util.writeGeoJsonObject(silvretta, new File("C:/workspace/geojson/src/main/res/generated/Silvretta.geo.json"));

    }
}
