package de.danielr1996.geojson.colors;

import java.util.Random;
import java.util.stream.Stream;

public class GoldenNumbers {
    // Verhältniss zweier Kreisbögen im Goldenen Schnitt
    public static double PHI = 137.5;
    public static Stream<Double> goldenerWinkel() {
        return Stream.iterate(0.0, e -> e + 137.5)
                .map(winkel -> winkel % 360);
    }

    public static int random(int high, int low){
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }
}
