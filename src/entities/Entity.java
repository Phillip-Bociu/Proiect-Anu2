package entities;

import main.gfx.Screen;
import map.Map;

public abstract class Entity {
	 public int x,y;
	 protected Map map;
	 
	 public Entity(Map map) {
		init(map); 
	 }
	 
	 public final void init(Map map) {
		 this.map = map;
	 }
	 
	 public abstract void tick();
	 
	 public abstract void render(Screen screen);
}
