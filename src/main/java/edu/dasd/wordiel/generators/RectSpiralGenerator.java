package edu.dasd.wordiel.generators;

import edu.dasd.wordiel.geom.Vector;
import java.awt.geom.Rectangle2D;
import edu.dasd.wordiel.properties.PlacementGenerator;

//TODO implement
public class RectSpiralGenerator implements PlacementGenerator {

    @Override
    public Vector place(int wordIndex, int wordWidth, int wordHeight, Rectangle2D bounds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*  public RectSpiralGenerator(Vector spiralElement) {
     this.spiralElement = spiralElement;
     reset();
     }
     private Vector spiralElement;
     private int count;

     @Override
     public Vector nextSegment() {
     count++;
     return this.getCoordinateInSpiral(count);
     }

     @Override
     public final void reset() {
     count = 0;
     }

    
     public Vector getCoordinateInSpiral(int n) {
     int x = (int) spiralElement.getX(), z = (int) spiralElement.getY();
     if (n - 1 >= 0) {
     int v = (int) Math.floor(Math.sqrt(n + .25) - 0.5);
     int spiralBaseIndex = v * (v + 1);
     int flipFlop = ((v & 1) << 1) - 1;
     int offset = flipFlop * ((v + 1) >> 1);
     x += offset;
     z += offset;
     int cornerIndex = spiralBaseIndex + (v + 1);
     if (n < cornerIndex) {
     x -= flipFlop * (n - spiralBaseIndex + 1);
     } else {
     x -= flipFlop * (v + 1);
     z -= flipFlop * (n - cornerIndex + 1);
     }
     }
     return new Vector(x, z);
     }*/
}
