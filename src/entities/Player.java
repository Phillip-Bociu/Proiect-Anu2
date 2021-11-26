package entities;

import main.InputH;
import main.gfx.Colours;
import main.gfx.Screen;
import map.Map;

public class Player extends Mob{
	
	private InputH input;
	private int colour = Colours.get(-1,401,502,555);
	private int scale = 1;
	
	public Player(Map map, int x, int y, InputH input) {
		super(map, "Player", x, y, 1);
		this.input = input;	
		this.speed = 2;
	}
	
	public static boolean toBool(int x)
	{
		if(x==0)
			return false;
		return true;
	}
	public void tick() {
		checkDamagingTile(x+4,y+4);
		
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
