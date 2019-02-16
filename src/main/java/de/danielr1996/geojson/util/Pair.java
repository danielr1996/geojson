package de.danielr1996.geojson.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pair<U, V> {
    private U first;
    private V second;
}
