package de.danielr1996.geojson.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;


public class Util {
    public static FeatureCollection readFeatureCollection(File file) {
        try {
            return readFeatureCollection(new FileInputStream(file), file.getName());
        } catch (FileNotFoundException e) {
            System.err.println("Fehler beim Einlesen der FeatureCollection");
        }
        return new FeatureCollection();
    }

    public static FeatureCollection readFeatureCollection(InputStream is, String name) {
        ObjectMapper om = new ObjectMapper();
        try {
            FeatureCollection featureCollection = om.readValue(is, FeatureCollection.class);
            featureCollection.getFeatures().get(0).setProperty("name",name);
            return featureCollection;
        } catch (IOException e) {
            System.err.println("Fehler beim Einlesen der FeatureCollection");
        }
        return new FeatureCollection();
    }

    public static void writeGeoJsonObject(GeoJsonObject geoJsonObject, OutputStream os) {
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(os, geoJsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String geoJsonObjectToString(GeoJsonObject geoJsonObject) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(geoJsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void writeGeoJsonObject(GeoJsonObject geoJsonObject, File file) {
        ObjectMapper om = new ObjectMapper();
        try {
            if (!file.exists()) file.createNewFile();
            om.writeValue(file, geoJsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FeatureCollection reducePoints(FeatureCollection in, int every) {
        FeatureCollection out = new FeatureCollection();

        List<Feature> features = in.getFeatures();
        IntStream.range(0, features.size())
                .filter(n -> n % every == 0)
                .mapToObj(features::get)
                .forEach(out::add);
        return out;
    }

    public static void main(String[] args) {
        FeatureCollection featureCollection = readFeatureCollection(new File("C:\\Users\\Daniel\\Google Drive\\fahrtenbuch (3).json"));
        LineString lineString = LineStrings.fromFeatureCollection(featureCollection);
        writeGeoJsonObject(lineString, new File("C:\\Users\\Daniel\\Desktop\\featurecollection.json"));

    }
}
