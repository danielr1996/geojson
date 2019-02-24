package de.danielr1996.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.danielr1996.geojson.HgtReader;
import de.danielr1996.geojson.geojson.Features;
import de.danielr1996.geojson.geojson.GeoJSONCollectors;
import de.danielr1996.geojson.geojson.Polygons;
import de.danielr1996.geojson.geojson.Util;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.util.streamex.StreamEx;
import org.geojson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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

    public static Feature getElevationsForMultiPointFeature(Feature feature){
        MultiPoint raster = Polygons.raster.apply(40).apply((Polygon)feature.getGeometry());
        Feature newFeature = Features.of(feature);

        newFeature.setGeometry(ElevationService.getElevations(raster.getCoordinates()).stream()
                .filter(coord->coord.getAltitude()!= Double.MIN_VALUE)
                .sorted(Comparator.comparingDouble(LngLatAlt::getAltitude)).collect(GeoJSONCollectors.toMultiPoint()));
        return newFeature;
    }
}
