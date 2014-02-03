package edu.dasd.wordiel.properties;

import edu.dasd.wordiel.data.Vector;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author 17bbarker
 */
public interface PlacementGenerator {

    public Vector place(int wordIndex, int wordWidth, int wordHeight, Rectangle2D bounds);
}
