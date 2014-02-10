package wordielv2.generator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import wordielv2.generator.properties.PlacementGenerator;
import wordielv2.generator.properties.WColorer;
import wordielv2.generator.properties.WRotator;
import wordielv2.generator.properties.WSizer;
import wordielv2.generator.quadtree.QuadTree;
import wordielv2.generator.utils.WordShaper;

/**
 *
 * @author Brandon Barker
 */
public class WordielEngine {

    //TODO implement later
    //public static final int DEFAULT = -1, LOWERCASE = 0, UPPERCASE = 1;
    private List<Vector> vec = new ArrayList<Vector>();
    private List<Word> words;
    @Getter
    private Shape shape;
    @Getter
    private Rectangle2D shapeBounds;
    private QuadTree<EngineWord> tree;
    @Getter
    @Setter
    private WColorer colorer;
    @Getter
    @Setter
    private WRotator rotator;
    @Getter
    @Setter
    private WSizer sizer;

    public WordielEngine(Shape shape) {
        this.shape = shape;
        shapeBounds = shape.getBounds2D();
        tree = new QuadTree<EngineWord>(0, 0, shapeBounds.getMaxX(), shapeBounds.getMaxY());
        words = new ArrayList<Word>();
        Default def = new Default();
        colorer = def;
        rotator = def;
        sizer = def;
    }

    public BufferedImage draw() {
        BufferedImage buffer = new BufferedImage((int) shapeBounds.getWidth(), (int) shapeBounds.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Creating graphics to do our dirty work ;)
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (EngineWord word : tree.values()) {
            g2.setColor(word.getWord().getColor());
            g2.fill(new GeneralPath(word.getShape()));
        }
        return buffer;
    }

    public void generate() {
        generate(new RectSpiralGenerator(new Vector(shapeBounds.getWidth() / 2, shapeBounds.getHeight() / 2)));
    }

    public void generate(PlacementGenerator gen) {
        int area = (int) (shapeBounds.getWidth() * shapeBounds.getHeight());
        for (int i = 0; i < area; i++) {
            vec.add(gen.nextSegment());
        }
        tree.clear();
        int lastTested = 0;
        for (EngineWord word : toEngineWords()) {
            int placeWord = placeWord(word, lastTested);
            if (placeWord >= 0) {
                lastTested = placeWord;
            }
        }
    }

    private int placeWord(EngineWord eWord, int lastTested) {

        EngineWord lastCollidedWith = null;
        Rectangle2D rect = eWord.getShape().getBounds2D();
        int area = (int) (rect.getWidth() > rect.getHeight() ? rect.getWidth() : rect.getHeight());

        for (int attempt = lastTested; attempt < vec.size(); attempt++) {
            Vector v = vec.get(attempt);

            eWord.setLocation(v);
            eWord.updateLoc();

            if (lastCollidedWith != null && eWord.overlaps(lastCollidedWith)) {
                continue;
            }

            boolean foundOverlap = false;
            for (EngineWord otherWord : tree.get(v.getX(), v.getY(), 200)) {
                if (eWord.overlaps(otherWord)) {
                    foundOverlap = true;
                    lastCollidedWith = otherWord;
                }
            }

            if (foundOverlap) {
                continue;
            }
            tree.put(v.getX(), v.getY(), eWord.clone());
            return attempt;
        }
        return -1;
    }

    private List<EngineWord> toEngineWords() {
        List<EngineWord> eWords = new ArrayList<EngineWord>();
        for (Word w : words) {

            w.applyProperty(colorer);
            w.applyProperty(rotator);
            w.applyProperty(sizer);

            EngineWord engineWord = new EngineWord(w);
            Shape s = WordShaper.getShapeFor(w.getWord(), w.getFont(), w.getWeight(), w.getAngle());
            System.out.println(s == null);
            //TODO add properties
            if (!isTooSmall(s, 7)) {
                //TODO Add Properties
                engineWord.setShape(s, 5);
                eWords.add(engineWord); // DON'T add eWords with no shape.
            }
        }
        //Collections.shuffle(eWords);
        return eWords;
    }

    private boolean isTooSmall(Shape shape, int minShapeSize) {
        if (minShapeSize < 1) {
            minShapeSize = 1;
        }
        return shape.getBounds().getHeight() < minShapeSize || shape.getBounds().getWidth() < minShapeSize;
    }

    public void addWord(Word word) {
        words.add(word);
    }

    public void removeWord(Word word) {
        words.remove(word);
    }

    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }

    private class Default implements WColorer, WRotator, WSizer {

        private static final int tiny = 5, small = 8, medium = 15, larger = 35, largest = 40;
        private Random numGen = new Random();

        @Override
        public Color generateColor(Word word) {

            return new Color(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
        }

        @Override
        public float generateRotation(Word word) {
            return (float) Math.toRadians(numGen.nextInt(360));
            //return (float) Math.toRadians(((int) (Math.random() * 4)) * 90);
        }

        @Override
        public float generateSize(String word) {
            return numGen.nextInt(50);

            /*
             * return 20;
             
             int probabilityDecider = new Random().nextInt(100);
             if (probabilityDecider <= 5) {
             return tiny;
             } else if (probabilityDecider < 25) {
             return small;
             } else if (probabilityDecider < 60) {
             return medium;
             } else if (probabilityDecider < 75) {
             return larger;
             } else if (probabilityDecider >= 95) {
             return largest;
             }
             return medium;*/
        }
    }
}
