package de.danielr1996.geojson.geojson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rectangle {
    private LngLatAlt northEast;
    private LngLatAlt northWest;
    private LngLatAlt southEast;
    private LngLatAlt southWest;

    public Supplier<MultiPoint> toMultipoint = () -> {
        MultiPoint multiPoint = new MultiPoint();
        multiPoint.add(northEast);
        multiPoint.add(northWest);
        multiPoint.add(southEast);
        multiPoint.add(southWest);
        return multiPoint;
    };
    public Supplier<Polygon> toPolygon = () -> {
        Polygon polygon = new Polygon();
        polygon.setExteriorRing(Arrays.asList(northEast, northWest, southWest, southEast));
        return polygon;
    };


    public static Function<Integer, Function<Rectangle, MultiPoint>> raster = steps ->(rectangle) -> {
        MultiPoint multiPoint = new MultiPoint();
        double hDiff = rectangle.northWest.getLatitude() - rectangle.southWest.getLatitude();
        double wDiff = rectangle.northWest.getLongitude() - rectangle.northEast.getLongitude();
        double hOffset = hDiff / steps;
        double wOffset = wDiff / steps;

        for (int i = 0; i <= steps; i++) {
            for (int j = 0; j <= steps; j++) {
                multiPoint.add(Coordinates.moveSouth.apply(hOffset * i)
                        .compose(Coordinates.moveWest.apply(wOffset * j))
                        .apply(rectangle.northWest));
            }
        }


        return multiPoint;
    };
}
