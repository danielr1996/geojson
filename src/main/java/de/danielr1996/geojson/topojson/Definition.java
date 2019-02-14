package de.danielr1996.geojson.topojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Definition {
    public String name;
    public Properties properties = new Properties();
    public List<Line> lines;
}