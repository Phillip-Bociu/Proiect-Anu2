package map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.Player;
import entities.Projectile;
import main.Game;
import main.gfx.Screen;
import tiles.Tile;
import networking.*;

public class Map {
	private byte[] tiles;
	public int width;
	public int height;
	public int[] spawnX,spawnY;
	public List<Entity> entities = new ArrayList<Entity>();
	private String imagePath;
	private BufferedImage image;
	Game game;
	public static PlayerPacket lastReceivedPlayerPacket = new PlayerPacket();
	public static PlayerPacket lastSentPlayerPacket = new PlayerPacket();
	public static ProjectilePacket lastSentProjectilePacket = new ProjectilePacket();
	public static ArrayList<ProjectilePacket> projQueue = new ArrayList<ProjectilePacket>();
	
	
	public Map(String mapPath, Game game) {
		if(mapPath !=null) {
			spawnX = new int[2];
			spawnY = new int[2];
			spawnX[0]=0;
			spawnX[1]=0;
			spawnY[0]=0;
			spawnY[1]=0;
			this.game = game;
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
		int k=0;
		for(int y =0; y<height; y++){
			for(int x =0; x<width;x++) {
				tileCheck : for(Tile t: Tile.tiles) {
					if(t!= null && t.getMapColour() == tileColours[x + y * width]) {
						this.tiles[x+y*width] = t.getId();
						//If the id of the tile is 3, then remember that spot for spawn point
						if(t.getId()==3){
							spawnX[k] = x;
							spawnY[k] = y;
							k++;
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
		
		synchronized(Map.projQueue)
		{
			if(Map.projQueue.size() != 0)
			{
				for(ProjectilePacket packet : Map.projQueue)
				{
					Projectile p = new Projectile(this);
					p.x = packet.x;
					p.y = packet.y;
					p.damage = packet.damage;
					p.speed = packet.speed;				
					p.directionX = packet.dirX;
					p.directionY = packet.dirY;
					p.immunityID = 1;
					entities.add(p);
				}				
				Map.projQueue.clear();
			}
		}
		
		for(int i=0; i<entities.size();i++) {
			if(entities.get(i).getClass() == Player.class && ((Player)(entities.get(i))).health <=0)
			{
				String message;
				if(i==1)
					message = "YOU WIN";
				else
					message = "YOU LOSE";
				game.initScreenMessage(message);
			}
			entities.get(i).tick();
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
