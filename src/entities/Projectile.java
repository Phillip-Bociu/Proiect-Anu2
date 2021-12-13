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
		if(map.getTile(Math.round(x+8)>>4,Math.round(y+8)>>4).isSolid())
		{
			map.entities.remove(this);
		}
		if((y - map.entities.get(1).y > -14 && y - map.entities.get(1).y < 8) &&  (Math.abs(x - map.entities.get(1).x)) < 8  )
		{
			((Mob)(map.entities.get(1))).modifyHealth(-1);
			map.entities.remove(this);
		}
			
	}
	public void render(Screen screen) {
		screen.render(Math.round(x), Math.round(y), 1*32 + 2, Colours.get(-1,444,444,444), 1);
	}
	
}
