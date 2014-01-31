package wordielv2.generator;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class BBTree {

    public static BBTree makeTree(Shape shape, int swelling) {
        Rectangle2D bounds = shape.getBounds2D();
        int minBoxSize = 1;
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int right = x + (int) bounds.getWidth();
        int bottom = y + (int) bounds.getHeight();

        BBTree tree = makeTree(shape, minBoxSize, x, y, right, bottom);
        tree.swell(swelling);
        return tree;
    }

    private static BBTree makeTree(Shape shape, int minBoxSize, int x, int y,
            int right, int bottom) {

        int width = right - x;
        int height = bottom - y;

        if (shape.contains(x, y, width, height)) {
            return new BBTree(x, y, right, bottom);
        } else if (shape.intersects(x, y, width, height)) {
            BBTree tree = new BBTree(x, y, right, bottom);

            boolean tooSmallToContinue = width <= minBoxSize;
            if (!tooSmallToContinue) {
                int centerX = avg(x, right);
                int centerY = avg(y, bottom);

                // upper left
                BBTree t0 = makeTree(shape, minBoxSize, x, y, centerX, centerY);
                // upper right
                BBTree t1 = makeTree(shape, minBoxSize, centerX, y, right, centerY);
                // lower left
                BBTree t2 = makeTree(shape, minBoxSize, x, centerY, centerX, bottom);
                // lower right
                BBTree t3 = makeTree(shape, minBoxSize, centerX, centerY, right, bottom);

                tree.addKids(t0, t1, t2, t3);
            }

            return tree;
        } else { // neither contains nor intersects
            return null;
        }
    }

    private static int avg(int a, int b) {
        // reminder: x >> 1 == x / 2
        // avg = (a+b)/2 = (a/2)+(b/2) = (a>>1)+(b>>1)
        return (a + b) >> 1;
    }
    private int left, top, right, bottom, rootX, rootY, swelling = 0;
    private BBTree[] kids;
    private BBTree parent;

    protected BBTree(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    void addKids(BBTree... _kids) {
        ArrayList<BBTree> kidList = new ArrayList<BBTree>();
        for (BBTree kid : _kids) {
            if (kid != null) {
                kidList.add(kid);
                kid.parent = this;
            }
        }

        kids = kidList.toArray(new BBTree[0]);
    }

    public void setLocation(int left, int top) {
        rootX = left;
        rootY = top;
    }

    private BBTree getRoot() {
        return parent == null ? this : parent.getRoot();
    }

    public boolean overlaps(BBTree otherTree) {
        if (rectCollide(this, otherTree)) {
            if (this.isLeaf() && otherTree.isLeaf()) {
                return true;
            } else if (this.isLeaf()) { // Then otherTree isn't a leaf.
                for (BBTree otherKid : otherTree.kids) {
                    if (this.overlaps(otherKid)) {
                        return true;
                    }
                }
            } else {
                for (BBTree myKid : this.kids) {
                    if (otherTree.overlaps(myKid)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean rectCollide(BBTree aTree, BBTree bTree) {
        int[] a = aTree.getPoints();
        int[] b = bTree.getPoints();

        return a[3] > b[1] && a[1] < b[3] && a[2] > b[0] && a[0] < b[2];
    }

    private int[] getPoints() {
        BBTree root = getRoot();
        return new int[]{
            root.rootX - swelling + left,
            root.rootY - swelling + top,
            root.rootX + swelling + right,
            root.rootY + swelling + bottom
        };
    }

    boolean containsPoint(float x, float y) {
        BBTree root = getRoot();
        return root.rootX + this.left < x
               && root.rootX + this.right > x
               && root.rootY + this.top < y
               && root.rootY + this.bottom > y;
    }

    boolean isLeaf() {
        return kids == null;
    }

    void swell(int extra) {
        swelling += extra;
        if (!isLeaf()) {
            for (int i = 0; i < kids.length; i++) {
                kids[i].swell(extra);
            }
        }
    }
}