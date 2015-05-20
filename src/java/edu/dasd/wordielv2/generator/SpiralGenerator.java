package edu.dasd.wordielv2.generator;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import edu.dasd.wordielv2.generator.properties.PlacementGenerator;

/**
 *
 * @author Brandon Barker
 */
public class SpiralGenerator implements PlacementGenerator {

    private static double TWO_PI = Math.PI * 2;
    
    public SpiralGenerator(WordielEngine eg) {
        this(eg, new Vector(eg.getShapeBounds().getWidth() / 2, eg.getShapeBounds().getHeight() /2));
    }
    
    public SpiralGenerator(WordielEngine eg, Vector center) {
        this(eg.getShapeBounds(), eg.getWords().size(), center);
    }
    
    public SpiralGenerator(Rectangle2D bounds, int wordAmount, Vector spiral) {
        this.spiralElement = spiral;
        this.fieldWidth = bounds.getWidth();
        this.wordAmount = wordAmount;
        reset();
    }
    
    private Vector spiralElement;
    private int count, wordAmount;
    private double fieldWidth;

    @Override
    public Vector nextSegment() {
        count++;
        return this.getCoordinateInSpiral(count);
    }

    @Override
    public final void reset() {
        count = 0;
    }

    private Vector getCoordinateInSpiral(int wordIndex) {
        float normalizedIndex = (float) wordIndex / wordAmount;

        double theta = normalizedIndex * 6 * TWO_PI;
        double radius = normalizedIndex * fieldWidth / 2f;

        double x = Math.cos(theta) * radius;
        double y = Math.sin(theta) * radius;

        return new Vector(spiralElement.getX() + x, spiralElement.getY() + y);
    }
}
