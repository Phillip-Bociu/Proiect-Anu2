package entities;



import main.InputH;
import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;

public class Player extends Mob{
	
	private InputH input;
	private int colour = Colours.get(-1,401,502,555);
	private int scale = 1;
	private boolean isYou = true;
	
	public Player(Map map, int x, int y, InputH input, boolean isYou, int colour, String name) {
		super(map, name, x, y, 1);
		this.colour = colour;
		this.input = input;	
		this.speed = 2;
		this.isYou = isYou;
	}
	
	public static boolean toBool(int x)
	{
		if(x==0)
			return false;
		return true;
	}
	public void tick() {
		checkDamagingTile(x+4,y+4);
		
		if(isYou)
		{			
			int xa = 0;
			int ya = 0;
			if(input.up.isPressed()) ya--;
			if(input.down.isPressed()) ya++;
			if(input.left.isPressed()) xa--;
			if(input.right.isPressed()) xa++;
			
			if(xa !=0 || ya!=0) {
				move(xa,ya);
				isMoving = true;
			}else {
				isMoving = false;
			}
			synchronized(Map.lastReceivedPacket)
			{				
				Map.lastSentPacket.x = x;
				Map.lastSentPacket.y = y;
				Map.lastSentPacket.movingDir = movingDir;
				Map.lastSentPacket.numSteps = numSteps;
			}
		} else
		{
			// x, y, movingDir, numsteps
			synchronized(Map.lastReceivedPacket)
			{				
				if(Map.lastReceivedPacket.x != -1)
					x = Map.lastReceivedPacket.x;
				if(Map.lastReceivedPacket.y != -1)	
					y = Map.lastReceivedPacket.y;
				if(Map.lastReceivedPacket.movingDir != -1)
					movingDir = Map.lastReceivedPacket.movingDir;
				if(Map.lastReceivedPacket.numSteps != -1)
					numSteps = Map.lastReceivedPacket.numSteps;
			}
		}
		
}
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 4; // the visual effect of walking
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
		int xOffset = x - modifier/2;
		int yOffset = y - modifier/2 -4;
		screen.render(xOffset + modifier*flip, yOffset, xTile + yTile * 32,colour,toBool(flip),false, scale);
		screen.render(xOffset + modifier*(1-flip), yOffset, xTile + 1 + yTile * 32,colour,toBool(flip),false, scale);
		screen.render(xOffset + modifier*flip, yOffset + modifier, xTile + (yTile + 1) * 32,colour,toBool(flip),false, scale);
		screen.render(xOffset  + modifier*(1-flip), yOffset  + modifier, xTile + 1 + (yTile + 1) * 32, colour,toBool(flip),false, scale);
	}
	
}
