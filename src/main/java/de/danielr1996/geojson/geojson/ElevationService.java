package de.danielr1996.geojson.geojson;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.geojson.LngLatAlt;

import java.io.IOException;
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
        OkHttpClient client = new OkHttpClient();

        String query = coords.stream().map(coord->String.format("(%s,%s),",coord.getLongitude(), coord.getLatitude())).collect(Collectors.joining(""));
//        System.out.println(query);
        Request request = new Request.Builder().url("https://elevation-api.io/api/elevation?points="+query).build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(new ObjectMapper().readValue(response.body().string(), ElevationServiceResponse.class).elevations.stream()
                    .map(coord->String.format("%s, %s, %s",coord.lat, coord.lon
                    , coord.elevation)).collect(Collectors.joining("\n")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return coords;
    }
}
