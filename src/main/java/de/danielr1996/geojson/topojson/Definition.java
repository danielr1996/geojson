package de.danielr1996.geojson.topojson;

import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Definition {
    public Map<String, Object> properties = new HashMap<>();
    public List<String> lines;
}