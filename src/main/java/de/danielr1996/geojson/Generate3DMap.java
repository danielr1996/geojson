package de.danielr1996.geojson;

import de.danielr1996.geojson.geojson.FeatureCollections;
import de.danielr1996.geojson.geojson.GeoJSONCollectors;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Generate3DMap {
  /*  public static void main(String[] args) throws IOException {
//        FeatureCollection featureCollection = Util.readFeatureCollection(Generate3DMap.class.getResourceAsStream("/generated/FeatureCollection.geo.json"),"FeatureCollection");
        Feature wetterSteinGebirge = featureCollection.getFeatures().stream()
                .filter(feature -> feature.getProperty("name").equals("Wettersteingebirge"))
                .findFirst().get();


        MultiPoint raster = Polygons.raster.apply(100).apply((Polygon)wetterSteinGebirge.getGeometry());

        *//*String csv = ElevationService.getElevations(raster.getCoordinates()).stream()
                .filter(coord->coord.getAltitude()!= Double.MIN_VALUE)
                .sorted(Comparator.comparingDouble(LngLatAlt::getAltitude))
                .map(coord -> String.format("%s, %s, %s", coord.getLongitude(), coord.getLatitude()
                        , coord.getAltitude())).collect(Collectors.joining("\n"));

        Files.write(Paths.get("C:/Users/Daniel/Desktop/wetterstein.csv"), csv.getBytes());*//*
        Feature mu = new Feature();
        mu.setGeometry(ElevationService.getElevations(raster.getCoordinates()).stream()
                .filter(coord->coord.getAltitude()!= Double.MIN_VALUE)
                .sorted(Comparator.comparingDouble(LngLatAlt::getAltitude)).collect(GeoJSONCollectors.toMultiPoint()));
        FeatureCollection mewF = new FeatureCollection();
        mewF.add(mu);
        Util.writeGeoJsonObject(mewF, System.out);
    }*/
}
