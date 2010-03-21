package logic;

import java.awt.Color;
import java.util.Random;


public class JawbreakerGame {
	private int width, height;
	private Statistics stats;
	private Color jawColors[];
	private int gamemap[][];
	private boolean selectmap[][];
	
	
	public JawbreakerGame(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.stats = new Statistics();
		
		this.jawColors = new Color[4];
		this.jawColors[0] = new Color(0, 255, 0);
		this.jawColors[1] = new Color(0, 255, 255);
		this.jawColors[2] = new Color(255, 255, 0);
		this.jawColors[3] = new Color(255, 0, 0);
		
		Random rng = new Random();
		this.gamemap = new int[width][height];
		for(int x=0; x < width; x++)
		{
			for(int y=0; y < height; y++)
			{
				this.gamemap[x][y] = rng.nextInt(2);
			}
		}
		
		this.selectmap = new boolean[width][height];
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public boolean isCellSelected(int x, int y)
	{
		return this.selectmap[x][y];
	}
	
	public Color getCellColor(int x, int y)
	{
		int piece = this.gamemap[x][y];
		if(piece == -1) return null;
		return this.jawColors[piece];
	}
	
	public void click(int x, int y)
	{
		if(this.selectmap[x][y])
		{
			this.stats.addToCurrentGamePoints(this.getSelectCount() * (this.getSelectCount()-1));
			this.deleteSelected();
			this.doGravity();
			this.doShift();
			this.stats.setCurrentSelectionPoints(0);
			this.selectmap = new boolean[width][height];
		}
		else
		{
			this.selectmap = new boolean[width][height];
			this.selectNeighbours(x, y);
			
			if(this.getSelectCount() == 1)
			{
				this.selectmap = new boolean[width][height];
			}
			else
			{
				this.stats.setCurrentSelectionPoints(this.getSelectCount() * (this.getSelectCount()-1));
			}
		}
	}
	
	public boolean isGameOver()
	{
		//if(true) return false;
		
		
		boolean old[][];
		old = this.selectmap.clone();
		
		for(int x=0; x < width; x++)
		{
			for(int y=0; y < height; y++)
			{
				this.selectmap = new boolean[width][height];
				this.selectNeighbours(x, y);
				if(this.getSelectCount() > 1)
				{
					this.selectmap = old;
					return false;
				}
			}
		}

		this.selectmap = old;
		return true;
	}
	
	public Statistics getStatistics()
	{
		return this.stats;
	}
	
	private boolean isInBound(int x, int y)
	{
		if(x < 0 || x >= this.width)
			return false;
		
		if(y < 0 || y >= this.height)
			return false;
		
		return true;
	}
	
	private void selectNeighbours(int x, int y)
	{
		if(!isInBound(x, y))
			return;
		
		this.selectNeighbours(x, y, this.gamemap[x][y]);
	}
	
	private void selectNeighbours(int x, int y, int color)
	{
		if(!isInBound(x, y) || this.gamemap[x][y] != -1 |
			this.gamemap[x][y] != color || this.selectmap[x][y])
			return;

		this.selectmap[x][y] = true;

		this.selectNeighbours(x-1, y, color);
		this.selectNeighbours(x+1, y, color);
		this.selectNeighbours(x, y-1, color);
		this.selectNeighbours(x, y+1, color);
		
	}
	
	/*private void selectNeighbours_old(int x, int y)
	{	
		
		int color = this.gamemap[x][y];
		if(color == -1 || this.selectmap[x][y]) return;
		
		this.selectmap[x][y] = true;
		
		if(isInBound(x-1, y) && this.gamemap[x-1][y] == color)
			this.selectNeighbours(x-1, y);
		
		if(isInBound(x+1, y) && this.gamemap[x+1][y] == color)
			this.selectNeighbours(x+1, y);
		
		if(isInBound(x, y-1) && this.gamemap[x][y-1] == color)
			this.selectNeighbours(x, y-1);
		
		if(isInBound(x, y+1) && this.gamemap[x][y+1] == color)
			this.selectNeighbours(x, y+1);
	}*/
	
	private void deleteSelected()
	{
		for(int x=0; x < width; x++)
		{
			for(int y=0; y < height; y++)
			{
				if(this.selectmap[x][y]) 
					this.gamemap[x][y] = -1;
			}
		}
	}
	
	private void doGravity()
	{
		for(int x=0; x < width; x++)
		{
			for(int y=height-1; y >= 0; y--)
			{
				if(this.gamemap[x][y] == -1)
				{
					for(int y2=y; y2 >= 0; y2--)
					{
						if(this.gamemap[x][y2] != -1)
						{
							this.gamemap[x][y] = this.gamemap[x][y2];
							this.gamemap[x][y2] = -1;
							break;
						}
					}
				}
			}
		}
	}
	
	/*private void doRowGravity(int row)
	{

	}*/
	
	private void doShift()
	{
		for(int x=width-1; x >= 0; x--)
		{
			if(this.isRowEmpty(x))
			{
				for(int x2=x; x2 >= 0; x2--)
				{
					if(!this.isRowEmpty(x2))
					{
						switchRows(x, x2);
						break;
					}
				}
			}
		}
	}
	
	private boolean isRowEmpty(int row)
	{
		for(int y=0; y < height; y++)
		{
			if(this.gamemap[row][y] != -1)
				return false;
		}
		return true;
	}
	
	private void switchRows(int r1, int r2)
	{
		for(int y=height-1; y >= 0; y--)
		{
			int temp = this.gamemap[r1][y];
			this.gamemap[r1][y] = this.gamemap[r2][y];
			this.gamemap[r2][y] = temp;
		}
	}
	
	private int getSelectCount()
	{
		int count = 0;
		for(int x=0; x < width; x++)
		{
			for(int y=0; y < height; y++)
			{
				if(this.selectmap[x][y]) 
					count++;
			}
		}
		return count;
	}
}
