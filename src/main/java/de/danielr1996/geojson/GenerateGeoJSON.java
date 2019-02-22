package de.danielr1996.geojson;

import com.codepoetics.protonpack.StreamUtils;
import de.danielr1996.geojson.colors.DistinctColors;
import de.danielr1996.geojson.geojson.Util;
import de.danielr1996.geojson.topojson.Definition;
import de.danielr1996.geojson.topojson.Definitions;
import de.danielr1996.geojson.topojson.Generator;
import de.danielr1996.geojson.topojson.Properties;
import org.geojson.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateGeoJSON {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Stream<String> noerdlicheostalpen = Stream.of(
                "Bregenzerwaldgebirge",
                "AllgäuerAlpen",
                "Lechquellengebirge",
                "LechtalerAlpen",
                "Wettersteingebirge",
                //"MiemingerKette",
                //"Karwendel",
                "AmmergauerAlpen",
                "BayrischeVoralpen",
                //"Kaisergebirge",
                //"Loferer Steinberge",
                //"Leoganger Steinberge",
                "BerchtesgadenerAlpen",
                "ChiemgauerAlpen",
                "SalzburgerSchieferAlpen",
                //"Tennengebirge",
                "Dachsteingebirge"
                //"Totes Gebirge",
                //"EnnstalerAlpen",
                //"Salzkammergutberge",
                //"OberöstereichischeVoralpen",
                //"HochschwabGruppe",
                //"MürzstegerAlpen",
                //"Rax-SchneebergGruppe",
                //"YbbstalerAlpen",
                //"TürnitzerAlpen",
                //"Gutensteiner Alpen",
                //"Wienerwald",
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
                "Berninaalpen",
                "ZillertalerAlpen",
                "Tuxeralpen",
                "KitzbühelerAlpen"
        );
        Stream<String> suedlicheostalpen = Stream.of(
                "Bergamaskeralpen"

        );

        Stream<String> doing = Stream.of(
                "BerchtesgadenerAlpen"
        );
        Stream<String> ostalpen = Stream.of(noerdlicheostalpen, zentraleostalpen, suedlicheostalpen).reduce(Stream::concat).orElseGet(Stream::empty);
        Stream<String> alle = Stream.of(
                ostalpen,
//                doing,
                Stream.<String>empty()
        ).reduce(Stream::concat).orElseGet(Stream::empty);


        FeatureCollection featureCollection = new FeatureCollection();

        Stream<Definition> definitions = alle
                .map(name -> Definitions.read(Generator.class.getResourceAsStream(String.format("/generate/%s.json", name))));

        StreamUtils.zip(definitions, DistinctColors.distinctColors(), (Definition def, String color) -> {
            if (def.properties == null) {
                def.properties = new Properties();
            }
            def.properties.setFill(color);
            return def;
        })
                .map(Generator::generate)
//                .map(ElevationService::getElevationsForMultiPointFeature)
                .forEach(feature -> {
                    File f = new File("src/main/res/generated/" + feature.getProperty("name").toString().replaceAll(" ", "") + ".geo.json");
                    File f2 = new File("src/main/res/generated/" + feature.getProperty("name").toString().replaceAll(" ", "") + "-height.geo.json");
                    Util.writeGeoJsonObject(feature, f);
                    Feature elevations = ElevationService.getElevationsForMultiPointFeature(feature);
                    Util.writeGeoJsonObject(elevations, f2);


                    String csv = ((MultiPoint) elevations.getGeometry()).getCoordinates().stream().filter(coord -> coord.getAltitude() != Double.MIN_VALUE)
                            .sorted(Comparator.comparingDouble(LngLatAlt::getAltitude))
                            .map(coord -> String.format("%s,%s,%s", coord.getLongitude(), coord.getLatitude()
                                    , coord.getAltitude())).collect(Collectors.joining("\n"));
                    csv = "Longitude,Latitude,Altitude\n" + csv;
                    try {
                        Files.write(Paths.get("src/main/res/generated/" + feature.getProperty("name").toString().replaceAll(" ", "") + "-height.csv"), csv.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
//        Util.writeGeoJsonObject(featureCollection, new File("src/main/res/generated/FeatureCollection.geo.json"));

    }
}
