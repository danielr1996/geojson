package de.danielr1996.geojson;

import de.danielr1996.geojson.topojson.Definition;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class YamlTest {
    public static void main(String[] args) {
        Yaml yaml = new Yaml(new Constructor(Definition.class));
        InputStream is = YamlTest.class.getClassLoader().getResourceAsStream("generate/Gurktaler-Alpen.json2");
        Definition obj = yaml.load(is);
        System.out.println(obj);
    }
}
