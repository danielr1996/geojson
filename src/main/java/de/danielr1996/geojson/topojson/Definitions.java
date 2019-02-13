package de.danielr1996.geojson.topojson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Definitions {
    public static Definition read(File f){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(f, Definition.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Definition read(InputStream is){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(is, Definition.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
