package de.danielr1996.geojson.geojson;

import org.geojson.LngLatAlt;
import org.geojson.Point;

import java.util.Comparator;

public class LngLatAltComparator implements Comparator<LngLatAlt> {
    @Override
    public int compare(LngLatAlt p1, LngLatAlt p2) {
        LngLatAlt coord1 = p1;
        LngLatAlt coord2 = p2;

        if (coord1.getLongitude() == coord2.getLongitude() && coord1.getLatitude() == coord2.getLatitude()) {
            return 0;
        } else if (coord1.getLongitude() == coord2.getLongitude()) {
            return Double.compare(coord2.getLatitude(), coord1.getLatitude());
        } else if (coord1.getLatitude() == coord2.getLatitude()) {
            return Double.compare(coord2.getLongitude(), coord1.getLongitude());
        } else {
            return Double.compare(winkel(coord1), winkel(coord2));
        }
    }

    private double winkel(LngLatAlt coord) {
        double x = coord.getAltitude();
        double y = coord.getLatitude();
        double hyp = Math.sqrt(x * x + y * y);
        double winkel = Math.asin(y / hyp);
        return winkel;
    }
}
