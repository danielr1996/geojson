package de.danielr1996.geojson.colors;

import java.util.Objects;

class HSV {
    double s, v, h;


    public HSV(double h, double s, double v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }

    public static HSV pure(double h) {
        return new HSV(h, 1, 1);
    }

    public RGB toRGB() {
        double hi = Math.floor(this.h / 60.0);
        double f = ((h / 60.0) - hi);
        double p = v * (1 - s);
        double q = v * (1 - s * f);
        double t = v * (1 - s * (1 - f));

        switch ((int) hi) {
            case 0:
                return new RGB(v, t, p);
            case 1:
                return new RGB(q, v, p);
            case 2:
                return new RGB(p, v, t);
            case 3:
                return new RGB(p, q, v);
            case 4:
                return new RGB(t, p, v);
            case 5:
                return new RGB(v, p, q);
            case 6:
                return new RGB(v, t, p);
            default:
                return new RGB(0, 0, 0);
        }
    }

    @Override
    public String toString() {
        return "HSV{" +
                "s=" + s +
                ", v=" + v +
                ", h=" + h +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HSV hsv = (HSV) o;
        return Double.compare(hsv.s, s) == 0 &&
                Double.compare(hsv.v, v) == 0 &&
                h == hsv.h;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s, v, h);
    }
}
