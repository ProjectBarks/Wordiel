package edu.dasd.wordiel.generators;

import edu.dasd.wordiel.data.Vector;
import edu.dasd.wordiel.WordielEngine;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import edu.dasd.wordiel.properties.PlacementGenerator;

/**
 *
 * @author Brandon Barker
 */
public class SpiralGenerator implements PlacementGenerator {

    private static double TWO_PI = Math.PI * 2;

    public SpiralGenerator(WordielEngine eg) {
        this(eg, new Vector(eg.getShapeBounds().getWidth() / 2, eg.getShapeBounds().getHeight() / 2));
    }

    public SpiralGenerator(WordielEngine eg, Vector center) {
        this(eg.getShapeBounds(), eg.getWords().size(), center);
    }

    public SpiralGenerator(Rectangle2D bounds, int wordAmount, Vector spiral) {
        this.spiralElement = spiral;
        this.wordAmount = wordAmount;
    }
    private Vector spiralElement;
    private int wordAmount;

    private Vector getCoordinateInSpiral(int wordIndex, int fieldWidth) {
        float normalizedIndex = (float) wordIndex / wordAmount;

        double theta = normalizedIndex * 6 * TWO_PI;
        double radius = normalizedIndex * fieldWidth / 2f;

        double x = Math.cos(theta) * radius;
        double y = Math.sin(theta) * radius;

        return new Vector(spiralElement.getX() + x, spiralElement.getY() + y);
    }

    @Override
    public Vector place(int wordIndex, int wordWidth, int wordHeight, Rectangle2D bounds) {
        return getCoordinateInSpiral(wordIndex, wordWidth);
    }
}
