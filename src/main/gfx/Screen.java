package main.gfx;

public class Screen {
	//Screen variables
	public static final int MAP_WIDTH = 256;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	
	public int[] pixels;
	
	public int xOffset = 0;
	public int yOffset = 0;
	
	public int width;
	public int height;
	
	public SpriteSheet sheet;
	
	/**
	 * Initializing the screen
	 * @param width - Width of the Screen
	 * @param height - Height of the Screen
	 * @param sheet - The sprite sheet we are working with
	 */
	public Screen(int width, int height, SpriteSheet sheet)
	{
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		
		pixels = new int[width*height];
	}
	
	

	/**
	 * It renders the wanted tile
	 * @param xPos
	 * @param yPos
	 * @param tile The number of the tile we wish to render
	 * @param colour the number you get by calling Colours.get(w,x,y,z)
	 * @param xMirror set true if you want to flip it vertically
	 * @param yMirror set true if you want to flip it horizontally
	 */
	public void render(int xPos,int yPos, int tile, int colour, boolean xMirror, boolean yMirror, int scale) {
		xPos -= xOffset;
		yPos -= yOffset;
		
		int scaleMap = scale - 1;
		// a way to get the number of the tile
		int xTile = tile %32;
		int yTile = tile /32;
		int tileOffset = (xTile<<4) + (yTile<<4)*sheet.width;
		for(int y=0;y<16;y++) {
			int ySheet = y;
			if(yMirror) ySheet = 15 - ySheet;
			int yPixel = y + yPos + (y* scaleMap) - ((scaleMap << 4)/2);
			
			for(int x=0;x<16;x++) {
				int xSheet = x;
				if(xMirror) xSheet = 15 - xSheet;
				int xPixel = x + xPos + (x* scaleMap) - ((scaleMap << 4)/2);
				int col = (colour >> (sheet.pixels[xSheet + ySheet*sheet.width + tileOffset]*8))&255;
				if(col<255) {
					for(int yScale = 0; yScale < scale; yScale++) {
						if(yPixel + yScale < 0 || yPixel+yScale >=height) continue;
						for(int xScale = 0; xScale < scale; xScale++) {
							
							if(xPixel+xScale < 0 || xPixel+xScale >=width) continue;
							pixels[(xPixel+xScale)+(yPixel+yScale)*width] = col;
						}	
					}
				}
			}
		}
	}
	public void render(int xPos,int yPos, int tile, int colour, int scale) {
		render(xPos, yPos, tile, colour, false, false, scale);
	}
	public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
