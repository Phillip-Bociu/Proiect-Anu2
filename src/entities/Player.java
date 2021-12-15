package entities;



import main.Game;
import main.InputH;
import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;

public class Player extends Mob{
	
	private InputH input;
	private int colour = Colours.get(-1,401,502,555);
	private int scale = 1;
	private boolean isYou = true;
	Screen screen;
	
	public Player(Map map, Screen screen, int x, int y, InputH input, boolean isYou, int colour, String name) {
		super(map, name, x, y, 1);
		this.colour = colour;
		this.input = input;	
		this.speed = 2f;
		this.isYou = isYou;
		this.screen = screen;
	}
	
	public static boolean toBool(int x)
	{
		if(x==0)
			return false;
		return true;
	}
	public void tick() {
		checkDamagingTile((int)(x+4),(int)(y+4));
		
		if(isYou)
		{			
			int xa = 0;
			int ya = 0;
			if(input.up.isPressed()) ya--;
			if(input.down.isPressed()) ya++;
			if(input.left.isPressed()) xa--;
			if(input.right.isPressed()) xa++;
			if(input.leftClick.pressed()){
				int targetX = input.leftClick.getX()/Game.SCALE+screen.xOffset - 8;
				int targetY = input.leftClick.getY()/Game.SCALE + screen.yOffset -8;
				Projectile p = new Projectile(map,0,2f,x,y, targetX, targetY);	
				map.addEntity(p);
				synchronized(Map.lastSentProjectilePacket)
				{
					Map.lastSentProjectilePacket.x = p.x;
					Map.lastSentProjectilePacket.y = p.y;
					Map.lastSentProjectilePacket.dirX = p.directionX;
					Map.lastSentProjectilePacket.dirY = p.directionY;
					Map.lastSentProjectilePacket.damage = p.damage;
					Map.lastSentProjectilePacket.speed = p.speed;
				}
			}
			
			if(xa !=0 || ya!=0) {
				move(xa,ya);
				isMoving = true;
			}else {
				isMoving = false;
			}
			synchronized(Map.lastReceivedPlayerPacket)
			{				
				Map.lastSentPlayerPacket.x = (int)(x);
				Map.lastSentPlayerPacket.y = (int)(y);
				Map.lastSentPlayerPacket.movingDir = movingDir;
				Map.lastSentPlayerPacket.numSteps = numSteps;
			}
		} else
		{
			// x, y, movingDir, numSteps
			synchronized(Map.lastReceivedPlayerPacket)
			{				
				if(Map.lastReceivedPlayerPacket.x != -1)
					x = Map.lastReceivedPlayerPacket.x;
				if(Map.lastReceivedPlayerPacket.y != -1)	
					y = Map.lastReceivedPlayerPacket.y;
				if(Map.lastReceivedPlayerPacket.movingDir != -1)
					movingDir = Map.lastReceivedPlayerPacket.movingDir;
				if(Map.lastReceivedPlayerPacket.numSteps != -1)
					numSteps = Map.lastReceivedPlayerPacket.numSteps;
			}
		}
		
}
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 8/Math.round(speed); // the visual effect of walking
		int flip = 0;
		
		if(movingDir == 0) {
			xTile += ((numSteps >> walkingSpeed) & 1) * 2;
		}
		if(movingDir == 1) {
			xTile += 4 +((numSteps >> walkingSpeed) & 1) * 2;
		}else if(movingDir > 1) {
			xTile +=8 + ((numSteps >> walkingSpeed) & 1) * 2;
			flip = (movingDir-1)%2;
		}
			
		int modifier = 16 * scale;
		int xOffset = (int)(x) - modifier/2;
		int yOffset = (int)(y) - modifier/2 -4;
		screen.render(xOffset + modifier*flip, yOffset, xTile + yTile * 32,colour,toBool(flip),false, scale);
		screen.render(xOffset + modifier*(1-flip), yOffset, xTile + 1 + yTile * 32,colour,toBool(flip),false, scale);
		screen.render(xOffset + modifier*flip, yOffset + modifier, xTile + (yTile + 1) * 32,colour,toBool(flip),false, scale);
		screen.render(xOffset  + modifier*(1-flip), yOffset  + modifier, xTile + 1 + (yTile + 1) * 32, colour,toBool(flip),false, scale);
	}
	
}
