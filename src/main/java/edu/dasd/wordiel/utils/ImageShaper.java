package edu.dasd.wordiel.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageShaper {

    public static Area toArea(URL url, Color color, int tolerance) {
        return toArea(toBufferedImage(url), color, tolerance);
    }

    public static Area toArea(Image image, Color color, int tolerance) {
        return toArea(toBufferedImage(image), color, tolerance);
    }

    /**
     * Creates an Area with PixelPerfect precision
     *
     * @param image
     * @param color The color that is draws the Custom Shape
     * @param tolerance The color tolerance
     * @return Area
     */
    public static Area toArea(BufferedImage image, Color color, int tolerance) {
        if (image == null) {
            return null;
        }
        Area area = new Area();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixel = new Color(image.getRGB(x, y));
                if (isIncluded(color, pixel, tolerance)) {
                    Rectangle r = new Rectangle(x, y, 1, 1);
                    area.add(new Area(r));
                }
            }
        }

        return area;
    }

    public static Area toArea(URL url) {
        return toArea(toBufferedImage(url));
    }

    public static Area toArea(Image image) {
        return toArea(toBufferedImage(image));
    }

    public static Area toArea(BufferedImage image) {
        //Assumes Black as Shape Color
        if (image == null) {
            return null;
        }

        Area area = new Area();
        Rectangle r;
        int y1, y2;

        for (int x = 0; x < image.getWidth(); x++) {
            y1 = 99;
            y2 = -1;
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixel = new Color(image.getRGB(x, y));
                //-16777216 entspricht RGB(0,0,0)
                if (pixel.getRGB() == -16777216) {
                    if (y1 == 99) {
                        y1 = y;
                        y2 = y;
                    }
                    if (y > (y2 + 1)) {
                        r = new Rectangle(x, y1, 1, y2 - y1);
                        area.add(new Area(r));
                        y1 = y;
                        y2 = y;
                    }
                    y2 = y;
                }
            }
            if ((y2 - y1) >= 0) {
                r = new Rectangle(x, y1, 1, y2 - y1);
                area.add(new Area(r));
            }
        }

        return area;
    }

    private static boolean isIncluded(Color target, Color pixel, int tolerance) {
        int rT = target.getRed();
        int gT = target.getGreen();
        int bT = target.getBlue();
        int rP = pixel.getRed();
        int gP = pixel.getGreen();
        int bP = pixel.getBlue();
        return ((rP - tolerance <= rT) && (rT <= rP + tolerance)
                && (gP - tolerance <= gT) && (gT <= gP + tolerance)
                && (bP - tolerance <= bT) && (bT <= bP + tolerance));
    }

    public static BufferedImage toBufferedImage(Image image) {
        BufferedImage buffer = new BufferedImage(image.getHeight(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        buffer.createGraphics().drawImage(image, null, null);
        return buffer;
    }

    public static BufferedImage toBufferedImage(URL url) {
        try {
            return toBufferedImage(ImageIO.read(url));
        } catch (IOException ex) {
            return null;
        }
    }

    private ImageShaper() {
    }
}