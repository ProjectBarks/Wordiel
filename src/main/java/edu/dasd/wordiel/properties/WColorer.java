package edu.dasd.wordiel.properties;

import edu.dasd.wordiel.data.Word;
import java.awt.Color;

/**
 *
 * @author Brandon Barker
 */
public interface WColorer extends WProperty {

    public Color generateColor(Word word);
}
