package wordielv2.generator;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import wordielv2.generator.utils.WordShaper;

/**
 *
 * @author Brandon Barker
 */
public class EngineWord implements Comparable<EngineWord>, Cloneable {

    @Getter
    private Shape shape;
    private Rectangle2D bounds;
    private Shape template;
    protected BBTree bBTree;
    @Getter
    private Word word;
    @Getter
    @Setter
    private Vector location;

    public EngineWord(Word word) {
        this.word = word;
        location = new Vector(0, 0);
    }

    public void setShape(int swelling) {
        setShape(WordShaper.getShapeFor(word.getWord(), word.getFont(), word.getWeight(), word.getAngle()), swelling);
    }

    public void setShape(Shape shape, int swelling) {
        this.template = shape;
        this.shape = shape;
        this.bounds = shape.getBounds();
        this.bBTree = BBTree.makeTree(shape, swelling);
    }

    public boolean overlaps(EngineWord other) {
        return bBTree.overlaps(other.bBTree);
    }

    public boolean containsPoint(float x, float y) {
        return bBTree.containsPoint(x, y);
    }

    public void updateLoc() {
        bBTree.setLocation((int) location.getX(), (int) location.getY());
    }

    public void finalizeLocation() {
        AffineTransform transform = location.toAffineTransform();
        shape = transform.createTransformedShape(template);
        updateLoc();
    }

    @Override
    public int compareTo(EngineWord t) {
        return this.word.compareTo(t.getWord());
    }
    
    @Override
    public EngineWord clone() {
        EngineWord ew = new EngineWord(word);
        ew.setLocation(location.clone());
        ew.setShape(0);
        ew.finalizeLocation();
        return ew;
    }
}
