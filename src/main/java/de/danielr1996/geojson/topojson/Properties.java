package de.danielr1996.geojson.topojson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Properties {
    private String stroke = "#000000";
    private String strokeWidth = "1.0";
    private String strokeOpacity = "1.0";
    private String fill;
    private String fillOpacity = "0.4";

    @JsonGetter("stroke")
    public String getStroke() {
        return stroke;
    }

    @JsonSetter("stroke")
    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    @JsonGetter("stroke-width")
    public String getStrokeWidth() {
        return strokeWidth;
    }

    @JsonSetter("stroke-width")
    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @JsonGetter("stroke-opacity")
    public String getStrokeOpacity() {
        return strokeOpacity;
    }

    @JsonSetter("stroke-opacity")
    public void setStrokeOpacity(String strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    @JsonGetter("fill")
    public String getFill() {
        return fill;
    }

    @JsonSetter("fill")
    public void setFill(String fill) {
        this.fill = fill;
    }

    @JsonGetter("fill-opacity")
    public String getFillOpacity() {
        return fillOpacity;
    }

    @JsonSetter("fill-opacity")
    public void setFillOpacity(String fillOpacity) {
        this.fillOpacity = fillOpacity;
    }
}