package de.danielr1996.geojson;

import com.codepoetics.protonpack.StreamUtils;
import de.danielr1996.geojson.colors.DistinctColors;
import de.danielr1996.geojson.geojson.GeoJSONCollectors;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Stream<Definition> definitions = readDefinitions();
        List<Feature> features = definitions.map(Generator::generate).collect(Collectors.toList());

        features.stream().map(feature -> feature.getProperty("name")).forEach(System.out::println);
        features.forEach(Main::writePolygon);
        List<Feature> elevations = features
                .stream()
                .map(ElevationService::getElevationsForMultiPointFeature)
                .collect(Collectors.toList());

        elevations.forEach(Main::writeHeightRaster);
        elevations.forEach(Main::writeHeightCsv);

        FeatureCollection featureCollection = features.stream().collect(GeoJSONCollectors.toFeatureCollection());
        Util.writeGeoJsonObject(featureCollection, new File("src/main/res/generated/FeatureCollection.geo.json"));
    }

    public static void writePolygon(Feature feature) {
        File f = new File("src/main/res/generated/" + feature.getProperty("name").toString().replaceAll(" ", "") + ".geo.json");
        Util.writeGeoJsonObject(feature, f);
    }

    public static void writeHeightRaster(Feature feature) {
        File f2 = new File("src/main/res/generated/" + feature.getProperty("name").toString().replaceAll(" ", "") + "-height.geo.json");
        Util.writeGeoJsonObject(feature, f2);
    }

    public static void writeHeightCsv(Feature feature) {
        String csv = ((MultiPoint) feature.getGeometry()).getCoordinates().stream().filter(coord -> coord.getAltitude() != Double.MIN_VALUE)
                .sorted(Comparator.comparingDouble(LngLatAlt::getAltitude))
                .map(coord -> String.format("%s,%s,%s", coord.getLongitude(), coord.getLatitude()
                        , coord.getAltitude())).collect(Collectors.joining("\n"));
        csv = "Longitude,Latitude,Altitude\n" + csv;
        try {
            Files.write(Paths.get("src", "main", "res", "generated", feature.getProperty("name").toString().replaceAll(" ", "") + "-height.csv"),
                    csv.getBytes());
            //            Files.write(Paths.get("src/main/res/geeeeeeenerated/" + feature.getProperty("name").toString().replaceAll(" ", "") + "-height.csv"), csv.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // https://de.wikipedia.org/wiki/Liste_der_Gebirgsgruppen_in_den_Ostalpen_(nach_AVE)
    public static Stream<Definition> readDefinitions() {
        Stream<String> noerdlicheostalpen = Stream.of(
                "Bregenzerwaldgebirge", //1
                "AllgäuerAlpen", //2
                "Lechquellengebirge", //3a
                "LechtalerAlpen", //3b
                "Wettersteingebirge", //4
                "MiemingerKette", //4
                "Karwendel", //5
                "BrandenbergerAlpen", //6
                "AmmergauerAlpen", //7a
                "BayrischeVoralpen", //7b
                "Kaisergebirge", //8
                "LofererUndLeogangerSteinberge", //9
                "BerchtesgadenerAlpen", //10
                "ChiemgauerAlpen", //11
                "SalzburgerSchieferAlpen", //12
                "Tennengebirge", //13
                "Dachsteingebirge", //14
                "TotesGebirge", //15
                "Ennstaler-Alpen", //16
                "Salzkammergutberge", //17a
                "OberoesterreichischeVoralpen", //17b
                "HochschwabGruppe", //18
                "MürzstegerAlpen", //19
                "RaxSchneeberggruppe", //20
                "YbbstalerAlpen", //21
                "TuernitzerAlpen", //22
                "GutensteinerAlpen", //23
                "Wienerwald" //24
        );
        Stream<String> zentraleostalpen = Stream.of(
                "Raetikon", //25
                "Silvretta", //26
                "Samnaun", //27
                "Verwall", //28
                "Sesvenna", //29
                "OetztalerAlpen", //30
                "Stubaieralpen", //31
//                "SarntalerAlpen", //32
                "Tuxeralpen", //33
                "KitzbühelerAlpen", //34
                "ZillertalerAlpen", //35
//                "Venedigergruppe", //36
//                "Rieserfernergruppe", //37
//                "VillgratnerBerge",//38
//                "Granatspitzgruppe",//39
//                "Glocknergruppe",//40
//                "Schobergruppe",//41
//                "Goldberggruppe",//42
//                "Kreuzeckgruppe",//43
//                "Ankogelgruppe",//44
                "Radstätter-Tauern",//45a
                "Schladminger-Tauern",//45b
                "Rottenmanner-und-Woelzener-Tauern",//45c
                "Seckauer-Tauern",//45d
                "Gurktaler-Alpen",//46a
                "LavanttalerAlpen",//46b
                "RandgebirgeOestlichDerMur" //47
        );

        Stream<String> suedlicheostalpen = Stream.of(
//                "OrtlerAlpen",//48a
//                "Sobretta-Gravia-Gruppe",//48b
//                "Nonsberggruppe",//48c
//                "Adamello-Presanella-Alpen",//49
                "GardaseeBerge", // 50
//                "Brenta-Gruppe", // 51
                "Dolomiten", // 52
//                "Fleimstaler-Alpen", // 53
                "VizentinerAlpen", // 54
//                "Gailtaler-Alpen", // 56
//                "Karnischer-Hauptkamm", // 57
                "SuedlicheKarnischeAlpen", //57b
                "JulischeAlpen", //58
                "KarawankenUndBacherGebirge", //59
                "SteinerAlpen" // 60

        );

        Stream<String> westlicheostalpen = Stream.of(
                "Plessuralpen", //63
                "Plattagruppe", //64
                "Albulaalpen", //65
                "Berninaalpen", //66
//                "Livignoalpen", //67
                "Bergamaskeralpen" //68
        );

        Stream<String> doing = Stream.of(
                "SalzburgerSchieferAlpen", //12
//                "Dachsteingebirge", //14
//                "TotesGebirge", //15
//                "Ennstaler-Alpen", //16
//                "Seckauer-Tauern",//45d
//                "KarawankenUndBacherGebirge",//59
//                "Rottenmanner-und-Woelzener-Tauern",//45c
                "Gurktaler-Alpen",//46a
                "Radstätter-Tauern",//45a
                "Schladminger-Tauern"//45b
        );
        Stream<String> ostalpen = Stream.of(noerdlicheostalpen, zentraleostalpen, suedlicheostalpen, westlicheostalpen).reduce(Stream::concat).orElseGet(Stream::empty);
        Stream<String> alle = Stream.of(
                ostalpen,
//                doing,
                Stream.<String>empty()
        ).reduce(Stream::concat).orElseGet(Stream::empty);

        Stream<Definition> definitions = alle
                .map(name -> Definitions.read(Generator.class.getResourceAsStream(String.format("/generate/%s.json", name)), String.format("/generate/%s.json", name)));
        return StreamUtils.zip(definitions, DistinctColors.distinctColors(), (Definition def, String color) -> {
            def.properties.put("stroke", "#000000");
            def.properties.put("strokeWidth", "1.0");
            def.properties.put("strokeOpacity", "1.0");
            switch ((String) def.properties.get("lage")) {
                case "NOERDLICHE_OSTALPEN":
                    def.properties.put("fill", "blue");
                    break;
                case "SUEDLICHE_OSTALPEN":
                    def.properties.put("fill", "green");
                    break;
                case "WESTLICHE_OSTALPEN":
                    def.properties.put("fill", "red");
                    break;
                case "ZENTRALE_OSTALPEN":
                    def.properties.put("fill", "orange");
                    break;
            }
            def.properties.put("fillOpacity", "0.4");

            return def;
        });
    }
}
