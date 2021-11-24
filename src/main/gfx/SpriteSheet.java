package main.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Class for the sprite sheet, which contains the tiles
 */
public class SpriteSheet {
	//variables
	public String path;
	public int width;
	public int height;
	
	public int[] pixels;
	
	public SpriteSheet(String path) {
		BufferedImage image = null;
		
		//Check for the sprite sheet
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null) {
			return;
		}
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		//extract the pixel colours from the spritesheet
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		//we divide it into 4 types so we can store it late in just one int
		for(int i=0;i<pixels.length;i++)
		{
			pixels[i] = (pixels[i] & 0xff) / 65;
		}
	}

}
