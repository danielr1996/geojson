package de.danielr1996.geojson.topojson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Definitions {
    public static Definition read(File f) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(f, Definition.class);
        } catch (IOException e) {
            System.err.println("Fehler beim einlesen der Definition");
        }
        return new Definition();
    }

    public static Definition read(InputStream is, String name) {
        if(is==null){
            System.err.println("Fehler beim einlesen der Definition, InputStream==null");
            return null;
        }
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(is, Definition.class);
        } catch (IOException e) {
            System.err.println("Fehler beim einlesen der Definition " + name);
        }
        return new Definition();
    }
}
