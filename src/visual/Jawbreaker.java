package visual;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import logic.JawbreakerGame;

/**
 * Erzeugen des Spiels in einem Fenster.
 * 
 * @author Holger Vogelsang
 */
public class Jawbreaker extends JFrame {

    /** 
     * Feldbreite in Zellen.
     */
    private static final int WIDTH = 5; //11;

    /** 
     * Feldhöhe in Zellen.
     */
    private static final int HEIGHT = 5; //12;

    /**
     * Referenz auf die Spiellogik.
     */
    private JawbreakerGame breakerGame;

    /**
     * Referenz auf das Panel, das die Ausgaben vornimmt.
     */
    private JawbreakerPanel breakerPanel;

    /**
     * Erzeugt das komplette Spiel.
     */
    public Jawbreaker() {
        super("Jawbreaker");
        breakerGame  = new JawbreakerGame(WIDTH, HEIGHT);
        breakerPanel = new JawbreakerPanel(breakerGame);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(breakerPanel);
        
        JLabel points = new JLabel("0");
        breakerPanel.setScoreLabel(points);
        points.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(points, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    /**
     * Die Methode wird beim Start des Programms aufgerufen.
     * @param args Kommandozeilenparameter
     */
    public static void main(String[] args) {
        Jawbreaker breaker = new Jawbreaker();
        breaker.pack();
        breaker.setVisible(true);
    }
}
