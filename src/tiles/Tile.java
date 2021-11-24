package tiles;

import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;

/**
 * Basic class for Tiles in which we store most basic data
 */
public abstract class Tile {
	
	//An array for all tiles
	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(0, -1, -1, -1),0xFF000000);
	public static final Tile STONE = new BasicSolidTile(1,1,0,Colours.get(-1, 333, -1, -1),0xFF555555);
	public static final Tile GRASS = new BasicTile(2,2,0,Colours.get(-1, 131, 141, -1),0xFF00FF00);
	public static final Tile SPAWN = new BasicTile(3,2,0,Colours.get(-1, 131, 141, -1),0xFF696969);//NICE
	public static final Tile WATER = new AnimatedTile(4,new int[][] {{3,0},{4,0},{5,0},{6,0}},Colours.get(-1, 115, 105, -1),0xFF0000FF,900);
	
	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	private int mapColour;
	
	/**
	 * @param id The id which the tile should get !Try setting the Tiles in this class!
	 * @param isSolid
	 * @param isEmitter
	 * @param colour
	 */
	public Tile(int id, boolean isSolid, boolean isEmitter, int colour) {
		this.id = (byte) id;
		if(tiles[id]!=null) throw new RuntimeException("Duplicate tile id on " + id);
		this.solid = isSolid;
		this.emitter = isEmitter;
		tiles[id] = this;
		this.mapColour = colour;
	}
	
	public byte getId() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isEmitter() {
		return emitter;
	}
	public int getMapColour() {
		return mapColour;
	}
	
	public abstract void tick();
	public abstract void render(Screen screen, Map map, int x, int y);
}
