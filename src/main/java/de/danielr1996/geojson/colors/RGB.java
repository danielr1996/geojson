package de.danielr1996.geojson.colors;

import java.util.Objects;

class RGB {
    int red, green, blue;

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public RGB(double red, double green, double blue) {
        this.red = (int) Math.round(red*255);
        this.blue = (int) Math.round(blue*255);
        this.green = (int) Math.round(green*255);
    }

    public String toHex(){
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    @Override
    public String toString() {
        return "RGB{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGB rgb = (RGB) o;
        return red == rgb.red &&
                green == rgb.green &&
                blue == rgb.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
}