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
                "Bregenzerwaldgebirge",
                "AllgäuerAlpen",
                "Lechquellengebirge",
                "LechtalerAlpen",
                "Wettersteingebirge",
                "MiemingerKette",
                "Karwendel",
                "BrandenbergerAlpen",
                "AmmergauerAlpen",
                "BayrischeVoralpen",
                "Kaisergebirge",
                "LofererUndLeogangerSteinberge",
                "BerchtesgadenerAlpen",
                "ChiemgauerAlpen",
                "SalzburgerSchieferAlpen",
                "Tennengebirge",
                "Dachsteingebirge",
                "TotesGebirge",
                "EnnstalerAlpen",
                "Salzkammergutberge",
                "OberoesterreichischeVoralpen",
                "HochschwabGruppe",
                "MürzstegerAlpen",
                "RaxSchneeberggruppe",
                "YbbstalerAlpen",
                "TuernitzerAlpen",
                "GutensteinerAlpen",
                "Wienerwald"
        );
        Stream<String> zentraleostalpen = Stream.of(
                "OetztalerAlpen",
                "Samnaun",
                "Verwall",
                "Raetikon",
                "Silvretta",
                "Sesvenna",
                "ZillertalerAlpen",
                "Tuxeralpen",
                "KitzbühelerAlpen",
                "Stubaieralpen",
                "RandgebirgeOestlichDerMur"
        );
        Stream<String> westlicheostalpen = Stream.of(
                "Plessuralpen",
                "Plattagruppe",
                "Albulaalpen",
                "Berninaalpen",
                //"Livignoalpen",
                "Bergamaskeralpen"
        );
        Stream<String> suedlicheostalpen = Stream.of(
                "Dolomiten",
                "VizentinerAlpen",
                "GardaseeBerge",
                "SuedlicheKarnischeAlpen",
                "JulischeAlpen",
                "SteinerAlpen",
                "KarawankenUndBacherGebirge"

        );

        Stream<String> doing = Stream.of(
//                "EnnstalerAlpen",
//                "KarawankenUndBacherGebirge",
                "HochschwabGruppe",
                "RaxSchneeberggruppe",
                "RandgebirgeOestlichDerMur"
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
            def.properties.put("stroke","#000000");
            def.properties.put("strokeWidth","1.0");
            def.properties.put("strokeOpacity","1.0");
            def.properties.put("fill",color);
            def.properties.put("fillOpacity","0.4");

            return def;
        });
    }
}
