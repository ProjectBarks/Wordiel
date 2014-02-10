package wordielv2.generator;

import java.awt.Color;
import java.awt.Font;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import wordielv2.generator.properties.WColorer;
import wordielv2.generator.properties.WProperty;
import wordielv2.generator.properties.WRotator;
import wordielv2.generator.properties.WSizer;

/**
 *
 * @author Brandon Barker
 */
public class Word implements Comparable<Word> {

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private String word;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private float weight = 0.0f, angle = 0.0f;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Color color = Color.BLACK;
    @Getter
    private Font font;

    public Word(String word, Font font) {
        this(word, font, font.getSize());
    }

    public Word(String word, Font font, float weight) {
        if (word == null || font == null) {
            throw new NullPointerException("Variables cannot be null!");
        }
        this.word = word;
        this.font = font;
        this.weight = weight;
    }

    public void setFont(Font font) {
        if (font == null) {
            throw new NullPointerException("Font cannot be null");
        }
        this.font = font;
    }

    public void applyProperty(WColorer w) {
        setColor(w.generateColor(this));
    }

    public void applyProperty(WRotator w) {
        setAngle(w.generateRotation(this));
    }

    public void applyProperty(WSizer w) {
        setWeight(w.generateSize(word));
    }

    @Override
    public int compareTo(Word o) {
        return Float.compare(weight, o.getWeight());
    }
}
