package de.danielr1996.geojson;

import de.danielr1996.geojson.geojson.ElevationService;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.MultiLineString;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.io.File;

public class Generate3DMap {
    public static void main(String[] args) {
        FeatureCollection featureCollection = Util.readFeatureCollection(Generate3DMap.class.getResourceAsStream("/generated/FeatureCollection.geo.json"),"FeatureCollection");
        Feature wetterSteinGebirge = featureCollection.getFeatures().stream()
//                .filter(feature -> feature.getProperty("name").equals("Wettersteingebirge"))
                .findFirst().get();


        MultiPoint raster = Polygons.raster.apply(10).apply((Polygon)wetterSteinGebirge.getGeometry());
//        MultiPoint rasterWithHeight = new MultiPoint();
//        raster.getCoordinates().stream().map(ElevationService::getElevations).forEach(rasterWithHeight::add);
//        ElevationService.getElevations(raster.getCoordinates());
        Feature mu = new Feature();
        mu.setGeometry(raster);
        featureCollection.add(mu);
        Util.writeGeoJsonObject(featureCollection, System.out);
    }
}
