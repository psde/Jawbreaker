/*
 * Created on 10.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package logic;

/**
 * 
 * @author holger
 *
 * Statistiken aller Spiele.
 */
public class Statistics {
    private int gamesPlayed            = 0;
    private int maxPoints              = 0;
    private int prevGamePoints         = 0;
    private int currentSelectionPoints = 0;
    private int currentGamePoints      = 0;
    private int allPoints              = 0;
    
    /**
     * Löscht die komplette Statistik.
     */
    public void clear() {
        gamesPlayed            = 0;
        maxPoints              = 0;
        prevGamePoints         = 0;
        currentSelectionPoints = 0;
        currentGamePoints      = 0;
        allPoints              = 0;
    }
    
    /**
     * Liest die Anzahl gespielter Runden.
     * @return Die Anzahl gespielter Runden.
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Liest den höchsten Spielstand aller Spiele.
     * @return Der höchste Spielstand.
     */
    public int getHighscore() {
        return maxPoints;
    }
    
    /**
     * Liest den Spielstand des letzten Spiels.
     * @return Spielstand des letzten Spiels.
     */
    public int getScoreLastGame() {
        return prevGamePoints;
    }
    
    /**
     * Beendet ein Spiel. Der Spielstand des laufenden Spiels
     * wird als Ergebnis übernommen. 
     * Dabei wird der Highscore neu gesetzt, falls in der letzten
     * Runde mehr Punkte als beim bisherigen Höchststand erreicht
     * wurden. Weiterhin wird die Anzahl der gespielten Spiele erhöht.
     */
    public void finishGame() {
        this.prevGamePoints = currentGamePoints;
        allPoints += currentGamePoints;
        if (prevGamePoints > maxPoints) {
            maxPoints = prevGamePoints;
        }
        gamesPlayed++;
        currentGamePoints = 0;
    }
    
    /**
     * Liest die Punkte der aktuell selektierten Zellen.
     * @return Punkte der aktuell selektierten Zellen.
     */
    public int getCurrentSelectionPoints() {
        return currentSelectionPoints;
    }
    
    /**
     * Schreibt die Punkte der aktuell selektierten Zellen.
     * @param currentSelectionPoints Punkte für die aktuelle Selektion.
     */
    public void setCurrentSelectionPoints(int currentSelectionPoints) {
        this.currentSelectionPoints = currentSelectionPoints;
    }

    /**
     * Ermittelt die Anzahl Punkte im jetzt laufenden Spiel.
     * @return Anzahl Punkte im laufenden Spiel.
     */
    public int getCurrentGamePoints() {
        return currentGamePoints;
    }

    /**
     * Addiert einen Wert zur Anzahl Punkte im laufenden Spiel.
     * @param addGamePoints Anzahl zu addierender Punkte.
     */
    public void addToCurrentGamePoints(int addGamePoints) {
        this.currentGamePoints += addGamePoints;
    }
    
    /**
     * Ermittelt den Mittelwert aller Punkte.
     * @return Mittelwert.
     */
    public int getAverage() {
        return gamesPlayed > 0 ? allPoints / gamesPlayed : 0;
    }
}
