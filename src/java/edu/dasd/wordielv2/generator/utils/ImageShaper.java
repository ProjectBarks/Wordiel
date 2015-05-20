package edu.dasd.wordielv2.generator.utils;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author 17bbarker
 */
public class ImageShaper {

    public static Shape shape(Image image, int color) {
        //RectTree tree = new RectTree(0, 0, image.width, image.height);
        BufferedImage img = (BufferedImage) image;
        RectTree tree = new RectTree(0, 0, img.getWidth(), img.getHeight());
        return tree.toShape(img, color);
    }

    private static class RectTree {

        private ArrayList<RectTree> kids = null;
        private int left, top, right, bottom, width, height;

        public RectTree(int l, int t, int r, int b) {
            left = l;
            top = t;
            right = r;
            bottom = b;
            width = right - left;
            height = bottom - top;  // Yep: upside-down.

            split();
        }

        private void split() {

            /*
             * Saying width < 2 OR height < 2 means we miss a few odd pixels. Saying
             * width < 2 AND height < 2 means we get them, but it goes a bit slower.
             * For now, we'll go with faster.
             */
            if (width < 2 || height < 2) {
                return;
            }

            int centerX = avg(left, right);
            int centerY = avg(top, bottom);
            kids = new ArrayList<RectTree>();
            kids.add(new RectTree(left, top, centerX, centerY));
            kids.add(new RectTree(centerX, top, right, centerY));
            kids.add(new RectTree(left, centerY, centerX, bottom));
            kids.add(new RectTree(centerX, centerY, right, bottom));
        }

        private int avg(int a, int b) {
            // reminder: x >> 1 == x / 2
            // avg = (a+b)/2 = (a/2)+(b/2) = (a>>1)+(b>>1)
            return (a + b) >> 1;
        }

        public Shape toShape(BufferedImage img, int color) {
            Area area = new Area();
            if (isAllCovered(img, color)) {
                area.add(new Area(new Rectangle(left, top, width, height)));
            } else if (kids != null) {
                for (RectTree kid : kids) {
                    area.add(new Area(kid.toShape(img, color)));
                }
            }
            return area;
        }
        private Boolean isAllCoveredMemo;

        private boolean isAllCovered(BufferedImage img, int color) {
            if (isAllCoveredMemo == null) {
                if (kids == null) {
                    isAllCoveredMemo = img.getRGB(left, top) == color;
                } else {
                    // isAllCoveredMemo = kids.all?(&:isAllCovered);
                    isAllCoveredMemo = true;
                    for (RectTree kid : kids) {
                        if (!kid.isAllCovered(img, color)) {
                            isAllCoveredMemo = false;
                            break;
                        }
                    }
                }
            }
            return isAllCoveredMemo;
        }
    }

    private ImageShaper() {
    }
}
