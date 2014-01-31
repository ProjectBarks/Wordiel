package wordielv2.generator.properties;

import java.awt.Color;
import wordielv2.generator.Word;

/**
 *
 * @author Brandon Barker
 */
public interface WColorer extends WProperty {

    public Color generateColor(Word word);
}
