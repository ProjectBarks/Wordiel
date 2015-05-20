package edu.dasd.wordiel.utils;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import lombok.Setter;

public class WordShaper {

    private static FontRenderContext frc = new FontRenderContext(null, true, true);
    @Setter
    private static boolean rightToLeft = false;

    public static Shape getShapeFor(String word, Font font, float fontSize, float angle) {
        Shape shape = makeShape(word, sizeFont(font, fontSize));
        return moveToOrigin(rotate(shape, angle));
    }

    public static Shape getShapeFor(String word, Font font) {
        return getShapeFor(word, font, font.getSize2D(), 0);
    }

    private static Font sizeFont(Font unsizedFont, float fontSize) {
        if (fontSize == unsizedFont.getSize2D()) {
            return unsizedFont;
        }
        return unsizedFont.deriveFont(fontSize);
    }

    private static Shape makeShape(String word, Font font) {
        char[] chars = word.toCharArray();

        // TODO hmm: this doesn't render newlines. Hrm. If your word text is "foo\nbar", you get "foobar".
        GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length,
                                                rightToLeft ? Font.LAYOUT_RIGHT_TO_LEFT : Font.LAYOUT_LEFT_TO_RIGHT);

        return gv.getOutline();
    }

    private static Shape rotate(Shape shape, float rotation) {
        if (rotation == 0) {
            return shape;
        }
        Rectangle2D bounds2D = shape.getBounds2D();
        return AffineTransform.getRotateInstance(rotation, bounds2D.getCenterX(), bounds2D.getCenterY()).createTransformedShape(shape);
    }

    private static Shape moveToOrigin(Shape shape) {
        Rectangle2D rect = shape.getBounds2D();

        if (rect.getX() == 0 && rect.getY() == 0) {
            return shape;
        }

        return AffineTransform.getTranslateInstance(-rect.getX(), -rect.getY()).createTransformedShape(shape);
    }

    private WordShaper() {
    }
}