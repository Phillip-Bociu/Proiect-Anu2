package tiles;

import main.gfx.Screen;
import map.Map;

public class BasicTile extends Tile {
	protected int tileId;
	protected int tileColour;
	
	/**
	 * @param id The id which the tile should get !Try setting the Tiles in the Tile class!
	 * @param x The x coordinate of the Tile in the sprite sheet
	 * @param y The y coordinate of the Tile in the sprite sheet
	 * @param tileColour
	 * @param mapColour The rgb of the pixel from the map file
	 */
	public BasicTile(int id, int damage, int x, int y, int tileColour, int mapColour){
		super(id, damage, false, false, mapColour);
		this.tileId = x + y*32;
		this.tileColour = tileColour;
	}
	public void render(Screen screen, Map map, int x, int y) {
		screen.render(x, y, tileId, tileColour, 1);
	}
	public void tick() {
	}
}
