package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.JawbreakerGame;

/**
 * Visualisierung des Jawbreaker-Spiels.
 * 
 * @author Holger Vogelsang
 */
public class JawbreakerPanel extends JPanel implements MouseListener {

    /**
     * Referenz auf das Spiel des Benutzers.
     */
    private JawbreakerGame breakerGame;
    private JLabel         scoreLabel;
    private Point          clickedCell = null;
    private Font           font = null;
    
    /**
     * Cache fuer die Farben und deren Abstufungen, um nicht permanent neue Farbobjekte
     * mit brighter() und darker() zu erzeugen.
     */
    private HashMap<Color, Color> selectedColors     = new HashMap<Color, Color>();
    private HashMap<Color, Color> darkerColors       = new HashMap<Color, Color>(); 
    private HashMap<Color, Color> brighterColors     = new HashMap<Color, Color>(); 

    /**
     * Erzeugt ein neues Panel.
     * Die Größe des Panels berechnet sich aus der Anzahl
     * der Zellen im Spiel sowie der Größe der einzelnen Zellen.
     *
     * @param nBreakerGame Referenz auf das Spiel, das angezeigt werden soll.
     */
      public JawbreakerPanel(JawbreakerGame nBreakerGame) {
        breakerGame = nBreakerGame;
        addMouseListener(this);
        setPreferredSize(new Dimension(600, 605));
      }

      /**
       * Neuzeichnen des Panel.
       * Die Zeichenmethode wird intern vom Browser aufgerufen,
       * wenn ein Neuzeichnen notwendig ist.
       *
       * @param gr Referenz auf den Grafikkontext.
       */
      public void paintComponent(Graphics gr) {
        Graphics2D graphics  = (Graphics2D) gr;
        boolean    selection = false;
        
        int cellSize = getCellSize();

        // Hintergrund löschen
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, getSize().width, getSize().height);

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                  RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (int x = 0; x < breakerGame.getWidth(); x++) {
          for (int y = 0; y < breakerGame.getHeight(); y++) {
            Color col = breakerGame.getCellColor(x, y);
            if (col != null) {
                if (breakerGame.isCellSelected(x, y)) {
                    Color selectedCellColor = selectedColors.get(col);
                    if (selectedCellColor == null) {
                        selectedCellColor = col.darker().darker().darker();
                        selectedColors.put(col, selectedCellColor);
                    }
                    col = selectedCellColor;
                    selection = true;
                }
                // Dunklere Farbe erzeugen bzw. lesen
                Color darkerColor = darkerColors.get(col);
                if (darkerColor == null) {
                    darkerColor = col.darker();
                    darkerColors.put(col, darkerColor);
                }
                // Hellere Farbe erzeugen bzw. lesen
                Color brighterColor = brighterColors.get(col);
                if (brighterColor == null) {
                    brighterColor = col.brighter();
                    brighterColors.put(col, brighterColor);
                }
                graphics.setPaint(new GradientPaint(2 + x * cellSize, 2 + y * cellSize, darkerColor, 
                                                    2 + x * cellSize + cellSize - 2,
                                                    2 + y * cellSize + cellSize - 2, brighterColor));
                graphics.fillOval(2 + x * cellSize,
                                  2 + y * cellSize,
                                  cellSize - 2, cellSize - 2);
                
                graphics.setColor(darkerColor);
                graphics.drawOval(2 + x * cellSize,
                        2 + y * cellSize,
                        cellSize - 2, cellSize - 2);
            }
          }
        }
        
        // Mindestens eine Zelle ist selektiert: Punkte anzeigen
        if (selection) {
            String selectedCellPoints = String.valueOf(breakerGame.getStatistics().getCurrentSelectionPoints());
            graphics.setColor(Color.white);
            if (font == null || font.getSize() != cellSize / 3) {
                font = new Font(Font.SANS_SERIF, Font.BOLD, cellSize / 3);
            }
            graphics.setFont(font);
            FontMetrics fm = graphics.getFontMetrics();
            graphics.drawString(selectedCellPoints, 
                                (int) (clickedCell.x * cellSize + (cellSize - fm.stringWidth(selectedCellPoints)) / 2), 
                                (int) (clickedCell.y * cellSize + (cellSize - fm.getHeight())));
            selection = false;
        }
      }

    /**
     * Ermittelt die Ausmaße einer Zelle. 
     * @return Die Ausmaße einer Zelle.
     */
    private int getCellSize() {
        int cellSize = Math.min(getSize().width  / breakerGame.getWidth(),
                                getSize().height / breakerGame.getHeight());
        return cellSize;
    }

    /**
     * Mausklick: Bildschirmposition in Feldposition umrechnen.
     * @param event Systemereignis
     */
    public void mouseClicked(MouseEvent event) {
        int cellSize = getCellSize();
        int xField = event.getX() / cellSize;
        int yField = event.getY() / cellSize;

        // Click außerhalb?
        if ((xField >= breakerGame.getWidth()) || (yField >= breakerGame.getHeight())) {
            return;
        }

        if (clickedCell == null) {
            clickedCell = new Point(xField, yField);
        }
        else {
            clickedCell.x = xField;
            clickedCell.y = yField;
        }
        
        breakerGame.click(xField, yField);
        
        // Aktuelle Punktezahl anzeigen
        scoreLabel.setText(String.valueOf(breakerGame.getStatistics().getCurrentGamePoints()));
        
        repaint();

        if (breakerGame.isGameOver()) {
            scoreLabel.setText(String.valueOf(breakerGame.getStatistics().getScoreLastGame()));
            JOptionPane.showMessageDialog(null, "Game over!");
            removeMouseListener(this);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void setScoreLabel(JLabel points) {
        scoreLabel = points;
    }
}