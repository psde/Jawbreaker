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
				this.gamemap[x][y] = rng.nextInt(4);
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
		return false;
	}
	
	public Statistics getStatistics()
	{
		return this.stats;
	}
	
	private boolean isInGamebounds(int x, int y)
	{
		if(x < 0 || x >= this.width)
			return false;
		
		if(y < 0 || y >= this.height)
			return false;
		
		return true;
	}
	
	private void selectNeighbours(int x, int y)
	{		
		int color = this.gamemap[x][y];
		if(color == -1 || this.selectmap[x][y]) return;
		
		this.selectmap[x][y] = true;
		
		if(isInGamebounds(x-1, y) && this.gamemap[x-1][y] == color)
			this.selectNeighbours(x-1, y);
		
		if(isInGamebounds(x+1, y) && this.gamemap[x+1][y] == color)
			this.selectNeighbours(x+1, y);
		
		if(isInGamebounds(x, y-1) && this.gamemap[x][y-1] == color)
			this.selectNeighbours(x, y-1);
		
		if(isInGamebounds(x, y+1) && this.gamemap[x][y+1] == color)
			this.selectNeighbours(x, y+1);
	}
	
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
			this.doRowGravity(x);
		}
	}
	
	private void doRowGravity(int row)
	{
		for(int y=height-1; y >= 0; y--)
		{
			if(this.gamemap[row][y] == -1)
			{
				for(int y2=y; y2 >= 0; y2--)
				{
					if(this.gamemap[row][y2] != -1)
					{
						this.gamemap[row][y] = this.gamemap[row][y2];
						this.gamemap[row][y2] = -1;
						break;
					}
				}
			}
		}
	}
	
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
						this.gamemap[x] = this.gamemap[x2];
						this.emptyRow(x2);
						break;
					}
				}
			}
		}
		/*
		for(int x=0; x < width; x++)
		{
			this.doRowShift(x);
		}*/
	}
	
	private void doRowShift(int row)
	{
		
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
	
	private void emptyRow(int row)
	{
		for(int y=0; y < height; y++)
		{
			this.gamemap[row][y]= -1;
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
