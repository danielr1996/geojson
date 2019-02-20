package de.danielr1996.geojson.linearalgebra;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vector {
    private double a;
    private double b;

    public Vector substract(Vector that) {
        return Vector.builder()
                .a(that.a - this.a)
                .b(that.b - this.b)
                .build();
    }

    public double pointProduct(Vector that){
        return that.a * this.a+that.b * this.b;
    }

    public double abs(){
        return Math.sqrt(a*a+b*b);
    }

    public double winkel(Vector that){
        Vector u = this;
        Vector v = that;
//        double cos = ;
        System.out.println(u.abs()*v.abs()/u.pointProduct(v));
        System.out.println(u.pointProduct(v)/u.abs()*v.abs());
//        return Math.acwins(Math.floor(u.pointProduct(v)/u.abs()*v.abs()));
        return 0.0;
    }
}
