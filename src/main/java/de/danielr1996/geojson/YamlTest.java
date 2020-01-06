package de.danielr1996.geojson;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlTest {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        InputStream is = YamlTest.class.getClassLoader().getResourceAsStream("generate/Gurktaler-Alpen.json");
        Object obj = yaml.load(is);
        System.out.println(obj);
    }
}
