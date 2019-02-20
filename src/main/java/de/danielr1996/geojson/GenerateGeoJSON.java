package de.danielr1996.geojson;

import com.codepoetics.protonpack.StreamUtils;
import de.danielr1996.geojson.colors.DistinctColors;
import de.danielr1996.geojson.geojson.Util;
import de.danielr1996.geojson.topojson.Definition;
import de.danielr1996.geojson.topojson.Definitions;
import de.danielr1996.geojson.topojson.Generator;
import de.danielr1996.geojson.topojson.Properties;
import org.geojson.FeatureCollection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
                "Raetikon"
        );
        Stream<String> ostalpen = Stream.of(noerdlicheostalpen, zentraleostalpen, suedlicheostalpen).reduce(Stream::concat).orElseGet(Stream::empty);
        Stream<String> alle = Stream.of(
//                ostalpen,
                doing,
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
                .forEach(featureCollection::add);
        Util.writeGeoJsonObject(featureCollection, new File("src/main/res/generated/FeatureCollection.geo.json"));

    }
}