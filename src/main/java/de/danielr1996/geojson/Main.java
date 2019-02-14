package de.danielr1996.geojson;

import de.danielr1996.geojson.geojson.Util;
import de.danielr1996.geojson.topojson.Generator;
import org.geojson.FeatureCollection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Stream<String> noerdlicheostalpen = Stream.of(
                "Bregenzerwaldgebirge",
                "AllgäuerAlpen",
                "LechtalerAlpen",
                "AmmergauerAlpen",
                "Wettersteingebirge"
        );
        Stream<String> zentraleostalpen = Stream.of(
                "OetztalerAlpen",
                "Samnaun",
                "Verwall",
                "Raetikon",
                "Silvretta",
                "Sesvenna",
                "Albulaalpen",
                "Plessuralpen",
                "Plattagruppe",
                "Berninaalpen"
        );
        Stream<String> suedlicheostalpen = Stream.of(
                "Bergamaskeralpen"

        );
        Stream<String> polygons = Stream.of(
                noerdlicheostalpen
//                zentraleostalpen,
//                suedlicheostalpen
        ).reduce(Stream::concat)
                .orElseGet(Stream::empty);
        FeatureCollection featureCollection = new FeatureCollection();

        polygons
                .map(Generator::generate)
                .forEach(featureCollection::add);
        Util.writeGeoJsonObject(featureCollection, new File("src/main/res/generated/FeatureCollection.geojson"));

    }
}
