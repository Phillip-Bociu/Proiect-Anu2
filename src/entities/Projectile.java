package entities;

import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;

public class Projectile extends Entity{
	float speed = 0.3f;
	int damage;
	float directionX,directionY;
	public Projectile(Map map, float speed, int damage, float x, float y, int targetX, int targetY) {
		super(map);
		this.speed = speed;
		this.damage = damage;
		this.x = x;
		this.y = y;
		directionX = targetX - x;
		directionY = targetY - y;
		double stabilizer = Math.sqrt((directionX*directionX + directionY*directionY)/(speed*speed));
		directionX /=stabilizer;
		directionY /=stabilizer;
	}
	public void tick() {
		x+=directionX;
		y+=directionY;
	}
	public void render(Screen screen) {
		screen.render(Math.round(x), Math.round(y), 1*32 + 2, Colours.get(-1,444,444,444), 1);
	}
	
}
