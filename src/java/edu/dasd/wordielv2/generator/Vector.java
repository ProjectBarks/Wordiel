package edu.dasd.wordielv2.generator;

import java.awt.geom.AffineTransform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Vector implements Cloneable {

    @Getter
    @Setter
    private double x, y;

    public AffineTransform toAffineTransform() {
        AffineTransform alignment = new AffineTransform();
        alignment.translate(x, y);
        return alignment;
    }

    @Override
    public Vector clone() {
        return new Vector(x, y);
    }
}