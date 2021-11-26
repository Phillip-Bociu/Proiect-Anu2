package tiles;

public class BasicSolidTile extends BasicTile{

	/**
	 * @param id The id which the tile should get !Try setting the Tiles in the Tile class!
	 * @param x The x coordinate of the Tile in the sprite sheet
	 * @param y The y coordinate of the Tile in the sprite sheet
	 * @param tileColour
	 * @param mapColour The rgb of the pixel from the map file
	 */
	public BasicSolidTile(int id, int damage, int x, int y, int tileColour,int mapColour) {
		super(id, damage, x, y, tileColour, mapColour);
		this.solid = true;
	}

}
