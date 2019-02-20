package de.danielr1996.geojson.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public static LngLatAlt getElevation(LngLatAlt coord) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://elevation-api.io/api/elevation?points=(" + coord.getLongitude() + "," + coord.getLatitude() + ")").build();
        try {
            Response response = client.newCall(request).execute();
            coord.setAltitude(new ObjectMapper().readValue(response.body().string(), ElevationServiceResponse.class).elevations.get(0).elevation);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return coord;
    }

    public static List<LngLatAlt> getElevations(List<LngLatAlt> coords) {
        List<LngLatAlt> newList = new ArrayList<>();
        int gesamtSize = coords.size();
        int chunks = gesamtSize/10;
        for (int i = 0; i < coords.size(); i += 10) {
            System.out.printf("Chunk %d of %d\n", i, chunks);
            int toIndex = i + 10;
            if (toIndex > coords.size()) toIndex = coords.size() - 1;
            newList.addAll(get10Elevations(coords.subList(i, toIndex)));
        }
        return newList;
    }


    private static List<LngLatAlt> get10Elevations(List<LngLatAlt> coords) {
        OkHttpClient client = new OkHttpClient();

        String query = coords.stream().map(coord -> String.format("(%s,%s),", coord.getLongitude(), coord.getLatitude())).collect(Collectors.joining(""));
        Request request = new Request.Builder().url("https://elevation-api.io/api/elevation?points=" + query).build();
        try (Response response = client.newCall(request).execute()) {
            ElevationServiceResponse elevation = new ObjectMapper().readValue(response.body().string(), ElevationServiceResponse.class);
            return StreamEx.zip(coords, elevation.elevations, (coord, el) -> {
                coord.setAltitude(el.elevation);
                return coord;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coords;
    }
}
