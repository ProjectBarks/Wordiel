package edu.dasd.wordiel;

import edu.dasd.wordiel.data.EngineWord;
import edu.dasd.wordiel.geom.Vector;
import edu.dasd.wordiel.data.Word;
import edu.dasd.wordiel.geom.QuadTree;
import edu.dasd.wordiel.properties.PlacementGenerator;
import edu.dasd.wordiel.properties.WColorer;
import edu.dasd.wordiel.properties.WEngineProperty;
import edu.dasd.wordiel.properties.WRotator;
import edu.dasd.wordiel.properties.WSizer;
import edu.dasd.wordiel.utils.WordShaper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Brandon Barker
 */
public class WordielEngine {
    
    private static ExecutorService service = null;
    //TODO implement later
    //public static final int DEFAULT = -1, LOWERCASE = 0, UPPERCASE = 1;
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
    @Getter
    private GeneratorStatus status;
    private double completion = 0;
    private Map<WEngineProperty, Double> properties;
    
    public WordielEngine(Shape shape) {
        this.shape = shape;
        shapeBounds = shape.getBounds2D();
        tree = new QuadTree<EngineWord>(0, 0, shapeBounds.getMaxX(), shapeBounds.getMaxY());
        properties = new EnumMap<WEngineProperty, Double>(WEngineProperty.class);
        words = new ArrayList<Word>();
        Default def = new Default();
        status = GeneratorStatus.INACTIVE;
        colorer = def;
        rotator = def;
        sizer = def;
        
    }
    
    public BufferedImage draw() {
        BufferedImage buffer = new BufferedImage((int) shapeBounds.getWidth(), (int) shapeBounds.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Creating graphics to do our dirty work ;)
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean fill = this.getProperty(WEngineProperty.FILL_SHAPE) >= 1;
        for (EngineWord word : tree.values()) {
            g2.setColor(word.getWord().getColor());
            if (fill) {
                g2.fill(new GeneralPath(word.getShape()));
            } else {
                g2.draw(new GeneralPath(word.getShape()));
            }
        }
        return buffer;
    }

    /*public void generate() { TODO reimplement
     //generate(new RectSpiralGenerator(new Vector(shapeBounds.getWidth() / 2, shapeBounds.getHeight() / 2)));
     }*/
    public void generate(final PlacementGenerator gen) {
        if (service == null) {
            service = Executors.newCachedThreadPool();
        }
        status = GeneratorStatus.ACTIVE;
        service.submit(new Runnable() {
            @Override
            public void run() {
                tree.clear();
                long start = System.currentTimeMillis();
                ArrayList<EngineWord> engineWords = toEngineWords();
                
                @SuppressWarnings("unchecked")
                Rectangle2D rect = new TreeSet<EngineWord>(engineWords).last().getShape().getBounds2D();
                double radi = rect.getWidth() > rect.getHeight() ? rect.getWidth() : rect.getHeight();
                radi *= 2;
                
                double completed = 0, total = engineWords.size();
                for (EngineWord word : engineWords) {
                    completed++;
                    completion = completed / total;
                    placeWord(gen, word, calculateMaxAttemptsFromWordWeight(word), radi);
                }
                status = GeneratorStatus.INACTIVE;
                System.out.println(System.currentTimeMillis() - start);
            }
        });
    }
    
    public boolean isGeneratorActive() {
        return status == GeneratorStatus.ACTIVE;
    }
    
    public double getPercentCompleted() {
        if (status != GeneratorStatus.ACTIVE) {
            return -1;
        } else {
            return completion;
        }
    }
    
    private int placeWord(PlacementGenerator gen, EngineWord eWord, int maxAttempts, double radi) {
        
        EngineWord lastCollidedWith = null;
        Rectangle2D b = eWord.getShape().getBounds();
        attempter:
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            
            Vector v = gen.place(attempt, (int) b.getWidth(), (int) b.getHeight(), shapeBounds);
            
            eWord.setLocation(v);
            eWord.finalizeLocation();
            
            if (!shape.contains(eWord.getShape().getBounds())) {
                continue;
            }
            
            if (lastCollidedWith != null && eWord.overlaps(lastCollidedWith)) {
                continue;
            }
            
            for (EngineWord otherWord : tree.get(v.getX(), v.getY(), radi)) {
                if (eWord.overlaps(otherWord)) {
                    lastCollidedWith = otherWord;
                    continue attempter;
                }
            }
            
            tree.put(eWord.getX(), eWord.getY(), eWord);
            return attempt;
        }
        return -1;
    }
    
    private ArrayList<EngineWord> toEngineWords() {
        ArrayList<EngineWord> eWords = new ArrayList<EngineWord>();
        int minSize = (int) this.getProperty(WEngineProperty.MIN_SIZE), swelling = (int) this.getProperty(WEngineProperty.SWELLING);
        for (Word w : words) {
            
            w.applyProperty(colorer);
            w.applyProperty(rotator);
            w.applyProperty(sizer);
            
            EngineWord engineWord = new EngineWord(w);
            Shape s = WordShaper.getShapeFor(w.getWord(), w.getFont(), w.getWeight(), w.getAngle());
            //TODO add properties
            if (!isTooSmall(s,  minSize)) {
                //TODO Add Properties
                engineWord.setShape(s,  swelling);
                eWords.add(engineWord); // DON'T add eWords with no shape.
            }
        }
        //Collections.shuffle(eWords);
        return eWords;
    }
    
    private int calculateMaxAttemptsFromWordWeight(EngineWord word) {
        return Math.abs((int) ((1.0 - word.getWord().getWeight()) * 600) + 100);
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
    
    public void setProperty(WEngineProperty property, Double value) {
        if (property == null || value == null) {
            return;
        }
        properties.put(property, value);
    }
    
    public double getProperty(WEngineProperty property) {
        if (property == null) {
            throw new NullPointerException("property cannot be null");
        }
        
        return properties.containsKey(property) ? properties.get(property) : property.getDefault();
    }
    
    private class Default implements WColorer, WRotator, WSizer {
        
        private static final int tiny = 5, small = 8, medium = 60, larger = 35, largest = 40;
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
            //return numGen.nextInt(50);


            //return 20;

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
            return medium;
        }
    }
}
