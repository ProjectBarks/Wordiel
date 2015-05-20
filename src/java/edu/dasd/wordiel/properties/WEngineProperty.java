package edu.dasd.wordiel.properties;

/**
 *
 * @author Brandon Barker
 */
public enum WEngineProperty {
     SWELLING(1),
     MIN_SIZE(7),
     FILL_SHAPE(1);
     
     private double def; 
     
     private WEngineProperty(double def) {
         this.def = def;
    }
     
     public double getDefault() {
         return def;
     }
}
