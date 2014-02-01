package wordielv2;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import wordielv2.generator.SpiralGenerator;
import wordielv2.generator.Word;
import wordielv2.generator.WordielEngine;

/**
 *
 * @author Brandon Barker
 */
public class Wordiel {

    public static boolean drew = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new drawler());
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        drew = true;
    }

    public static class drawler extends JComponent {

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            WordielEngine wordielGenerator = new WordielEngine(new Ellipse2D.Double(0, 0, 1000, 1000));
            for (int i = 0; i < 500; i++) {
                wordielGenerator.addWord(new Word("" + i, new Font("Lucid", Font.PLAIN, 20), 30));
            }
            wordielGenerator.generate(new SpiralGenerator(wordielGenerator));
            g2.drawImage(wordielGenerator.draw(), 0, 0, null);
        }
    }
}
