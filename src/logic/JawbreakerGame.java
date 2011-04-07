package logic;

import java.awt.Color;
import java.util.Random;

/**
 * Jawbreaker Spiel Implementation.
 * @author Mathias Garbe
 *
 */
public class JawbreakerGame {
	
	/**
	 * Breite des Spielfeldes.
	 */
	private int width;

	/**
	 * Höhe des Spielfeldes.
	 */
	private int height;
	
	/**
	 * Interne Statistik des Spiels.
	 */
	private Statistics stats;
	
	/**
	 * Farben der einzelnen Bälle.
	 */
	private Color[] ballColors;
	
	/**
	 * 2D-Spielfeld, -1 wenn kein Ball an der Position ist, ansonsten
	 * ein Wert zwischen 1 bis 4.
	 */
	private int[][] gamemap;
	
	/**
	 * 2D-Array, der die aktuelle Selektion repräsentiert.
	 */
	private boolean[][] selectmap;
		
	/**
	 * Erstellt das Spielobjekt und die Spielkarte.
	 * @param gameWidth Breite des Spielfelds.
	 * @param gameHeight Höhe des Spielfelds.
	 */
	public JawbreakerGame(final int gameWidth, final int gameHeight) {
		this.width = gameWidth;
		this.height = gameHeight;
		this.stats = new Statistics();
		
		this.ballColors = new Color[4];
		this.ballColors[0] = new Color(0, 255, 0);
		this.ballColors[1] = new Color(0, 255, 255);
		this.ballColors[2] = new Color(255, 255, 0);
		this.ballColors[3] = new Color(255, 0, 0);
		
		Random rng = new Random();
		this.gamemap = new int[gameWidth][gameHeight];
		for (int x = 0; x < gameWidth; x++) {
			for (int y = 0; y < gameHeight; y++) {
				this.gamemap[x][y] = rng.nextInt(4);
			}
		}
		
		this.selectmap = new boolean[gameWidth][gameHeight];
	}
	
	/**
	 * Gibt die Breite des Spielfeldes zurück.
	 * @return Breite des Spielfeldes.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Gibt die Höhe des Spielfeldes zurück.
	 * @return Höhe des Spielfeldes.
	 */
	public final int getHeight() {
		return this.height;
	}
	
	/**
	 * Prüft ob eine bestimme Zelle selektiert ist.
	 * @param x X-Koordinate der Zelle
	 * @param y Y-Koordinate der Zelle
	 * @return True, wenn Zelle selektiert ist, ansonsten False
	 */
	public final boolean isCellSelected(final int x, final int y) {
		return this.selectmap[x][y];
	}
	
	/**
	 * Gibt die Farbe einer Zelle zurück.
	 * @param x X-Koordinate der Zelle
	 * @param y Y-Koordinate der Zelle
	 * @return Zellfarbe bei existierenden Zellen, ansonsten null
	 */
	public final Color getCellColor(final int x, final int y) {
		int piece = this.gamemap[x][y];
		if (piece == -1) {
			return null;
		}
		return this.ballColors[piece];
	}
	
	/**
	 * Verarbeitet Klick-Events.
	 * @param x X-Koordinate der angeklickten Zelle
	 * @param y Y-Koordinate der angeklickten Zelle
	 */
	public final void click(final int x, final int y) {
		if (!isInBound(x, y)) {
			return;
		}
		
		if (this.selectmap[x][y]) {
			this.stats.addToCurrentGamePoints(this.getSelectCount() 
					                          * (this.getSelectCount() - 1));
			this.deleteSelected();
			this.doGravity();
			this.doShift();
			this.stats.setCurrentSelectionPoints(0);
			this.selectmap = new boolean[width][height];
		} 
		else {
			this.selectmap = new boolean[width][height];
			this.selectNeighbours(x, y);
			
			if (this.getSelectCount() == 1) {
				this.selectmap = new boolean[width][height];
			} 
			else {
				this.stats.setCurrentSelectionPoints(this.getSelectCount() 
				                                 * (this.getSelectCount() - 1));
			}
		}
	}
	
	/**
	 * Prüft ob das Spiel zuende ist. 
	 * @return True wenn kein Zug mehr möglich ist, ansonsten False.
	 */
	public final boolean isGameOver() {
		boolean[][] old;
		old = this.selectmap.clone();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.selectmap = new boolean[width][height];
				this.selectNeighbours(x, y, this.gamemap[x][y]);
								
				if (this.getSelectCount() > 1) {
					this.selectmap = old.clone();
					return false;
				}
			}
		}

		this.selectmap = old;
		return true;
	}
	
	/**
	 * Gibt das @Statistics Objekt zurück, in dem die aktuellen Statistiken 
	 * gespeichert sind.
	 * @return @Statistics Objekt
	 */
	public final Statistics getStatistics() {
		return this.stats;
	}
	
	/**
	 * Prüft ob eine Koordinate im Spielfeld liegt.
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 * @return True wenn die Koordinate gültig ist, ansonsten False.
	 */
	private boolean isInBound(final int x, final int y) {
		if (x < 0 || x >= this.width) {
			return false;
		}
		
		if (y < 0 || y >= this.height) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Selektiert alle benachtbarten Zellen gleicher Farbe.
	 * @param x X-Koordinate der Zelle
	 * @param y Y-Koordinate der Zelle
	 */
	private void selectNeighbours(final int x, final int y) {
		if (!isInBound(x, y) || this.gamemap[x][y] == -1) {
			return;
		}
		
		this.selectNeighbours(x, y, this.gamemap[x][y]);
	}
	
	/**
	 * Selektiert alle benachtbarten Zellen gleicher Farbe.
	 * @param x X-Koordinate der Zelle
	 * @param y Y-Koordinate der Zelle
	 * @param color Zu selektierende Farbe
	 */
	private void selectNeighbours(final int x, final int y, final int color) {
		if (!isInBound(x, y) || this.gamemap[x][y] == -1
			|| this.gamemap[x][y] != color || this.selectmap[x][y]) {
			return;
		}
		this.selectmap[x][y] = true;

		this.selectNeighbours(x - 1, y, color);
		this.selectNeighbours(x + 1, y, color);
		this.selectNeighbours(x, y - 1, color);
		this.selectNeighbours(x, y + 1, color);
	}
	
	/**
	 * Löscht alle selektierten Zellen vom Spielfeld. 
	 */
	private void deleteSelected() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (this.selectmap[x][y]) {
					this.gamemap[x][y] = -1;
				}
			}
		}
	}
	
	/**
	 * Wendet Gravitation auf die Zellen im Spielfeld an.
	 */
	private void doGravity() {
		for (int x = 0; x < width; x++) {
			for (int y = height - 1; y >= 0; y--) {
				if (this.gamemap[x][y] == -1) {
					for (int y2 = y; y2 >= 0; y2--) {
						if (this.gamemap[x][y2] != -1) {
							this.gamemap[x][y] = this.gamemap[x][y2];
							this.gamemap[x][y2] = -1;
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Rückt alle Reihen nach rechts auf.
	 */
	private void doShift() {
		for (int x = width - 1; x >= 0; x--) {
			if (this.isRowEmpty(x)) {
				for (int x2 = x; x2 >= 0; x2--) {
					if (!this.isRowEmpty(x2)) {
						switchRows(x, x2);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Gib zurück, ob eine bestimmte Reihe im Spielfeld leer ist.
	 * @param row Y-Koordinate der Reihe
	 * @return True wenn die Reihe leer ist, ansonsten False.
	 */
	private boolean isRowEmpty(final int row) {
		for (int y = 0; y < height; y++) {
			if (this.gamemap[row][y] != -1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Vertauscht zwei Reihen auf dem Spielfeld.
	 * @param r1 Y-Koordinate der ersten Reihe
	 * @param r2 Y-Koordinate der zweiten Reihe
	 */
	private void switchRows(final int r1, final int r2) {
		for (int y = height - 1; y >= 0; y--) {
			int temp = this.gamemap[r1][y];
			this.gamemap[r1][y] = this.gamemap[r2][y];
			this.gamemap[r2][y] = temp;
		}
	}
	
	/**
	 * Gibt die Anzahl der selektierten Zellen zurück.
	 * @return Anzahl der selektierten Zellen.
	 */
	private int getSelectCount() {
		int count = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (this.selectmap[x][y]) {
					count++;
				}
			}
		}
		return count;
	}
}
