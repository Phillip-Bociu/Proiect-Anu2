package entities;

import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;
import tiles.Tile;

public abstract class Mob extends Entity {
	
	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1; // 0 up, 1 down, 2 left, 3 right
	protected int scale = 1;
	protected int health= 7;
	
	 public Mob(Map map, String name, int x, int y, int speed) {
		 super(map);
		 this.name = name;
		 this.x = x;
		 this.y = y;
		 this.speed = speed;
	 }
	 
	 public void move(int xa, int ya) {
		 if(xa != 0 && ya != 0) {
			 move(xa, 0);
			 move(0, ya);
			 numSteps--;
			 return;
		 }
		 numSteps++;
		 if(!hasCollided(xa,ya)) {
			 if(ya < 0) movingDir = 0;
			 if(ya > 0) movingDir = 1;
			 if(xa < 0) movingDir = 2;
			 if(xa > 0) movingDir = 3;
			 
			 x += xa*speed;
			 y += ya*speed;
		 }
	 }
	 
	 public boolean hasCollided(int xa, int ya) {
			int xMin = 0;
			int xMax = 15;
			int yMin = 0;
			int yMax = 15;
			for(int x = xMin;x< xMax;x++) {
				if(isSolidTile(xa,ya,x,yMin))
					return true;
			}
			for(int x = xMin;x< xMax;x++) {
				if(isSolidTile(xa,ya,x,yMax))
					return true;
			}
			for(int y = xMin;y< yMax;y++) {
				if(isSolidTile(xa,ya,xMin,y))
					return true;
			}
			for(int y = xMin;y< yMax;y++) {
				if(isSolidTile(xa,ya,xMax,y))
					return true;
			}
			return false;
		}
	 
	 protected boolean isSolidTile(int xa, int ya, int x, int y) {
		 if(map == null) return false;
		 Tile lastTile = map.getTile((this.x + x)>>4, (this.y + y) >>4);
		 Tile newTile = map.getTile((this.x + x + xa)>>4, (this.y + y + ya)>>4);
		 if(!lastTile.equals(newTile) && newTile.isSolid()) return true;
		 return false;
	 }
	 
	 public void renderHealth(Screen screen, int xPos, int yPos){
		int xTile = 1;
		int yTile = 1;
		for(int i=0;i<10;i++)
			if(i<health)
				screen.render(xPos + i*17 + screen.xOffset, yPos + screen.yOffset, xTile + yTile * 32, Colours.get(-1,000,500,400), 1);
			else
				screen.render(xPos + i*17 + screen.xOffset, yPos + screen.yOffset, xTile + yTile * 32, Colours.get(-1,000,222,333), 1);
	 }
	 public void checkDamagingTile(int xPos,int yPos)
	 {
		 if(map.getTile(xPos>>4,yPos>>4).getDamage() > 0)
				health -= map.getTile(xPos>>4,yPos>>4).getDamage();
	 }
	 
	 public String getName() {
		 return name;
	 }
}
