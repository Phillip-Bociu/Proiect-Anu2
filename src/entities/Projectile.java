package entities;

import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;

public class Projectile extends Entity{
	public float speed = 0.3f;
	public int damage=1;
	public float directionX,directionY;
	public int immunityID=0;
	
	public Projectile(Map map)
	{
		super(map);
		this.immunityID = 0;
		this.speed = 0;
		this.x = 0;
		this.y = 0;
		this.directionX = 0;
		this.directionY = 0;
	}
	
	public Projectile(Map map,int immunityID, float speed, float x, float y, int targetX, int targetY) {
		super(map);
		this.immunityID = immunityID;
		this.speed = speed;
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
		for(int i=0;i<map.entities.size() && map.entities.get(i).getClass()==Player.class;i++)
			if(immunityID!=i && (y - map.entities.get(i).y > -14 && y - map.entities.get(i).y < 8) &&  (Math.abs(x - map.entities.get(i).x)) < 8  )
				{
					((Mob)(map.entities.get(i))).modifyHealth(-1);
					map.entities.remove(this);
					break;
				}
			
	}
	public void render(Screen screen) {
		screen.render(Math.round(x), Math.round(y), 1*32 + 2, Colours.get(-1,444,444,444), 1);
	}
	
}
