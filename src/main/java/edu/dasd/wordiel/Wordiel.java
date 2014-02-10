package edu.dasd.wordiel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import edu.dasd.wordiel.generators.SpiralGenerator;
import edu.dasd.wordiel.data.Word;

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
        frame.setSize(1050, 600);
        frame.setVisible(true);
        drew = true;
    }

    public static class drawler extends JComponent {

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            try {
                WordielEngine wordielGenerator = new WordielEngine(new Ellipse2D.Double(0, 0, 500, 500));
                for (int i = 0; i < 500; i++) {
                    wordielGenerator.addWord(new Word("" + i, new Font("Lucid", Font.PLAIN, 20), 30));
                }
                wordielGenerator.generate(new SpiralGenerator(wordielGenerator));
                while (wordielGenerator.isGeneratorActive()) {
                    System.out.println(wordielGenerator.getPercentCompleted());
                }
                g2.drawRect(0, 0, WIDTH, HEIGHT);
                g2.drawImage(wordielGenerator.draw(), 0, 0, null);
                System.out.println("DRW");
            } catch (Exception ex) {
                Logger.getLogger(Wordiel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
