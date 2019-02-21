package de.danielr1996.geojson;

import java.util.List;

public class ElevationServiceResponse {
    public List<Elevation> elevations;
    public String resolution;
}

class Elevation{
    public double lat;
    public double lon;
    public double elevation;
}