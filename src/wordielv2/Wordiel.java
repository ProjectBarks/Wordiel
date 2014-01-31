package wordielv2;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import wordielv2.generator.Word;
import wordielv2.generator.WordielEngine;

/**
 *
 * @author Brandon Barker
 */
public class Wordiel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new drawler());
        frame.setSize(700, 700);
        frame.setVisible(true);
    }

    public static class drawler extends JComponent {

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            WordielEngine wordielGenerator = new WordielEngine(new Ellipse2D.Double(0, 0, 500, 500));
            for (int i = 0; i < 100; i++) {
                wordielGenerator.addWord(new Word("Java", new Font("Lucid", Font.PLAIN, 20), 30));
            }
            wordielGenerator.generate();
            g2.drawImage(wordielGenerator.draw(), 0, 0, null);
            System.out.println("Draw");
        }
    }
}
