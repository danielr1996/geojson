package de.danielr1996.geojson.geojson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.geojson.LngLatAlt;

import java.io.IOException;
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