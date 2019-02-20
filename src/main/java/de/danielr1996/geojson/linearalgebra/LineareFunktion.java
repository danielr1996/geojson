package de.danielr1996.geojson.linearalgebra;

import java.util.function.Function;

public class LineareFunktion {
    public static final Function<Double, Function<Double, Function<Double, Double>>> FUNKTION = m -> t -> x -> m * x * t;
}
