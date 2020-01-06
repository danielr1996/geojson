package de.danielr1996.geojson.topojson;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Definition {
    public Map<String, Object> properties = new HashMap<>();
    public List<String> lines;
}