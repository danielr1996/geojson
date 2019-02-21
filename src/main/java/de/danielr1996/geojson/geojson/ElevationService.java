package de.danielr1996.geojson.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.geojson.HgtReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.util.streamex.StreamEx;
import org.geojson.LngLatAlt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElevationService {
    public static HgtReader reader = new HgtReader();

    public static LngLatAlt getElevation(LngLatAlt coord) {
        return reader.getElevationFromHgt(coord);
    }

    public static List<LngLatAlt> getElevations(List<LngLatAlt> coords) {
        return coords.stream().map(ElevationService::getElevation).collect(Collectors.toList());
    }
}
