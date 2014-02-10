package wordielv2.generator.properties;

import java.awt.geom.AffineTransform;
import wordielv2.generator.Word;

/**
 *
 * @author Brandon Barker
 */
public interface WRotator extends WProperty {

    public float generateRotation(Word word);
}
