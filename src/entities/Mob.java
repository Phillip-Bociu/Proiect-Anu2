package entities;

import map.Map;
import tiles.Tile;

public abstract class Mob extends Entity {
	
	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1; // 0 up, 1 down, 2 left, 3 right
	protected int scale = 1;
	
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
	 
	 public abstract boolean hasCollided(int xa, int ya);
	 
	 protected boolean isSolidTile(int xa, int ya, int x, int y) {
		 if(map == null) return false;
		 Tile lastTile = map.getTile((this.x + x)>>4, (this.y + y) >>4);
		 Tile newTile = map.getTile((this.x + x + xa)>>4, (this.y + y + ya)>>4);
		 if(!lastTile.equals(newTile) && newTile.isSolid()) return true;
		 return false;
	 }
	 
	 public String getName() {
		 return name;
	 }
}
