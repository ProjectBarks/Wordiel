package wordielv2.generator.properties;

import wordielv2.generator.Vector;

/**
 *
 * @author 17bbarker
 */
public interface PlacementGenerator {
    
    public Vector nextSegment();
    
    public void reset();
    
}
