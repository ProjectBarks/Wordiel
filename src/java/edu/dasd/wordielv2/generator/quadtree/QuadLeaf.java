package edu.dasd.wordielv2.generator.quadtree;

import java.util.ArrayList;

public class QuadLeaf<T> {

    public final double x;
    public final double y;
    public final ArrayList<T> values;

    public QuadLeaf(double x, double y, T value) {
        this.x = x;
        this.y = y;
        this.values = new ArrayList<T>(1);
        this.values.add(value);
    }
}