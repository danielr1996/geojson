package de.danielr1996.geojson.topojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.geojson.YamlTest;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

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

    public static Definition readDefinintionFromClassPath(String name) {
        InputStream is = Definitions.class.getResourceAsStream(name);
        if (name.endsWith(".yaml")) {
            Yaml yaml = new Yaml(new Constructor(Definition.class));
            try {
                return yaml.load(is);
            } catch (YAMLException e) {
                System.err.println("Cannot parse YAML " + name);
            }
        } else if (name.endsWith(".json")) {
            ObjectMapper om = new ObjectMapper();
            try {
                return om.readValue(is, Definition.class);
            } catch (IOException e) {
                System.err.println("Cannot parse JSON " + name);
            }
        }
        return new Definition();
    }
}
