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
                "Bregenzerwaldgebirge.json", //1
                "AllgäuerAlpen.json", //2
                "Lechquellengebirge.json", //3a
                "LechtalerAlpen.json", //3b
                "Wettersteingebirge.json", //4
                "MiemingerKette.json", //4
                "Karwendel.json", //5
                "BrandenbergerAlpen.json", //6
                "AmmergauerAlpen.json", //7a
                "BayrischeVoralpen.json", //7b
                "Kaisergebirge.json", //8
                "LofererUndLeogangerSteinberge.json", //9
                "BerchtesgadenerAlpen.json", //10
                "ChiemgauerAlpen.json", //11
                "SalzburgerSchieferAlpen.json", //12
                "Tennengebirge.json", //13
                "Dachsteingebirge.json", //14
                "TotesGebirge.json", //15
                "Ennstaler-Alpen.json", //16
                "Salzkammergutberge.json", //17a
                "OberoesterreichischeVoralpen.json", //17b
                "HochschwabGruppe.json", //18
                "MürzstegerAlpen.json", //19
                "RaxSchneeberggruppe.json", //20
                "YbbstalerAlpen.json", //21
                "TuernitzerAlpen.json", //22
                "GutensteinerAlpen.json", //23
                "Wienerwald" //24
        );
        Stream<String> zentraleostalpen = Stream.of(
                "Raetikon.json", //25
                "Silvretta.json", //26
                "Samnaun.json", //27
                "Verwall.json", //28
                "Sesvenna", //29
                "Oetztaler-Alpen", //30
                "Stubaier-Alpen", //31
                "Sarntaler-Alpen", //32
                "Tuxeralpen", //33
                "KitzbühelerAlpen.json", //34
                "Zillertaler-Alpen", //35
//                "Venedigergruppe", //36
//                "Rieserfernergruppe", //37
//                "VillgratnerBerge",//38
//                "Granatspitzgruppe",//39
//                "Glocknergruppe",//40
//                "Schobergruppe",//41
//                "Goldberggruppe",//42
//                "Kreuzeckgruppe",//43
//                "Ankogelgruppe",//44
                "Radstätter-Tauern.json",//45a
                "Schladminger-Tauern.json",//45b
                "Rottenmanner-und-Woelzener-Tauern.json",//45c
                "Seckauer-Tauern.json",//45d
                "Gurktaler-Alpen",//46a
                "LavanttalerAlpen.json",//46b
                "RandgebirgeOestlichDerMur.json" //47
        );

        Stream<String> suedlicheostalpen = Stream.of(
//                "OrtlerAlpen",//48a
//                "Sobretta-Gravia-Gruppe",//48b
                "Nonsberggruppe",//48c
//                "Adamello-Presanella-Alpen",//49
                "Gardasee-Berge", // 50
//                "Brenta-Gruppe", // 51
                "Dolomiten", // 52
                "Fleimstaler-Alpen", // 53
                "Vizentiner-Alpen", // 54
//                "Gailtaler-Alpen", // 56
//                "Karnischer-Hauptkamm", // 57
                "SuedlicheKarnischeAlpen.json", //57b
                "JulischeAlpen.json", //58
                "KarawankenUndBacherGebirge.json", //59
                "SteinerAlpen.json" // 60

        );

        Stream<String> westlicheostalpen = Stream.of(
                "Plessur-Alpen", //63
                "Plattagruppe", //64
                "Albula-Alpen", //65
                "Bernina-Alpen", //66
                "Livigno-Alpen", //67
                "Bergamasker-Alpen" //68
        );

        Stream<String> doing = Stream.of(
                "Fleimstaler-Alpen", // 53
                "Dolomiten", // 53
//                "Gardasee-Berge", // 50
                "Oetztaler-Alpen", //30
                "Stubaier-Alpen", //31
                "Zillertaler-Alpen", //35
                "Sarntaler-Alpen", //32
                "Nonsberggruppe"//48c
        );
        Stream<String> ostalpen = Stream.of(noerdlicheostalpen, zentraleostalpen, suedlicheostalpen, westlicheostalpen).reduce(Stream::concat).orElseGet(Stream::empty);
        Stream<String> alle = Stream.of(
//                ostalpen,
                doing,
                Stream.<String>empty()
        ).reduce(Stream::concat).orElseGet(Stream::empty);

        Stream<Definition> definitions = alle
                .map(name -> Definitions.readDefinintionFromClassPath(String.format("/generate/%s", name.endsWith(".json") ? name : name + ".yaml")));
        return StreamUtils.zip(definitions, DistinctColors.distinctColors(), (Definition def, String color) -> {
            def.properties.put("stroke", "#000000");
            def.properties.put("strokeWidth", "1.0");
            def.properties.put("strokeOpacity", "1.0");
            def.properties.put("fillOpacity", "0.4");
            if (def.properties.get("lage") != null) {
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
            } else {
                def.properties.put("fill", color);
            }
            return def;
        });
    }
}
