package map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import entities.Entity;
import main.gfx.Screen;
import tiles.Tile;
import networking.*;

public class Map {
	private byte[] tiles;
	public int width;
	public int height;
	public int spawnX = 0,spawnY = 0;
	public List<Entity> entities = new ArrayList<Entity>();
	private String imagePath;
	private BufferedImage image;
	public static PlayerPacket lastReceivedPacket = new PlayerPacket();
	public static PlayerPacket lastSentPacket = new PlayerPacket();
	
	public Map(String mapPath) {
		if(mapPath !=null) {
			this.imagePath = mapPath;
			this.loadMapFromFile();
		}
		else
		{	
			//a basic map for default
			this.width = 64;
			this.height = 64;
			tiles = new byte[width * height];
			this.generateMap();
		}
	}
	
	private void loadMapFromFile() {
		try {
			this.image = ImageIO.read(Map.class.getResourceAsStream(this.imagePath));
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[width * height];
			this.loadTiles();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void loadTiles() {
		//read the rbg values of the pixels
		int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0, width);
		for(int y =0; y<height; y++){
			for(int x =0; x<width;x++) {
				tileCheck : for(Tile t: Tile.tiles) {
					if(t!= null && t.getMapColour() == tileColours[x + y * width]) {
						this.tiles[x+y*width] = t.getId();
						//If the id of the tile is 3, then remember that spot for spawn point
						if(t.getId()==3){
							spawnX = x;
							spawnY = y;
						}
						break tileCheck;
					}
				}
			}
		}
	}
	public void generateMap() {
		for(int y = 0;y<height;y++) {
			for(int x = 0;x<width;x++)
					tiles[x + y*width] = Tile.GRASS.getId();
		}
	}
	
	public void tick() {
		//Let the entities do their animation
		for(Entity e : entities) {
			e.tick();
		}
		
		//Let the tiles do their animation
		for(Tile t : Tile.tiles) {
			if(t == null)
				break;
			t.tick();
		}
	}
	
	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		if(xOffset < 0) xOffset = 0;
		if(xOffset > ((width<<4)-screen.width)) xOffset = ((width<<4) - screen.width);
		if(yOffset < 0) yOffset = 0;
		if(yOffset > ((height<<4)-screen.height)) yOffset = ((height<<4) - screen.height);
	
		screen.setOffset(xOffset, yOffset);
		
		for(int y=(yOffset>>4);y<(yOffset + screen.height >> 4) + 1;y++) {
			for(int x=(xOffset>>4);x<(xOffset + screen.width >> 4) + 1;x++) {
				getTile(x,y).render(screen,this,x<<4,y<<4);
			}
		}
	}
	
	public void renderEntities(Screen screen) {
		for(Entity e: entities) {
			e.render(screen);
		}
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || x>width-1 || y < 0 || y > height-1) return Tile.VOID;
		return Tile.tiles[tiles[x+y*width]];
	}
	
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
}
