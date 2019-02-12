package de.danielr1996.geojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.LineStrings;
import de.danielr1996.geojson.geojson.Util;
import org.geojson.LineString;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        LineString lineString =
                Files.list(Paths.get(ClassLoader.getSystemResource("").toURI()))
                .map(Path::toString)
                .filter(path -> path.endsWith("geojson"))
//                .limit(16)
                .map(File::new)
                .map(Util::readFeatureCollection)
                .map(FeatureCollections::toLineString)
                .reduce(null, LineStrings::merge);
//                .forEach(System.out::println);
        Util.writeGeoJsonObject(lineString, System.out);

    }
}
