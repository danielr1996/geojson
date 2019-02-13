package de.danielr1996.geojson.topojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Definition {
    public String name;
    public Properties properties;
    public List<Line> lines;
}

class Properties{
    @JsonProperty("stroke")
    String stroke;
    @JsonProperty("stroke-width")
    String strokeWidth;
    @JsonProperty("stroke-opacity")
    String strokeOpacity;
    @JsonProperty("fill")
    String fill;
    @JsonProperty("fill-opacity")
    String fillOpacity;
}
