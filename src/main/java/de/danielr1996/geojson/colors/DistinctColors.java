package de.danielr1996.geojson.colors;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DistinctColors {
    public static Stream<String> distinctColors(){
        return GoldenNumbers.goldenerWinkel()
                .map(HSV::pure)
                .map(HSV::toRGB)
                .map(RGB::toHex);
    }
}
