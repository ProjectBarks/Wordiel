package edu.dasd.wordielv2.generator.properties;

import java.awt.geom.AffineTransform;
import edu.dasd.wordielv2.generator.Word;

/**
 *
 * @author Brandon Barker
 */
public interface WRotator extends WProperty {

    public float generateRotation(Word word);
}
