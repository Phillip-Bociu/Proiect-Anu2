package main.gfx;

public class Font {
	//All the characters that we'll use
	private static String font = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,:;'\"!?$%()-=+/      ";
	
	/**
	 * Render Function for the wanted message
	 * @param msg the wanted string
	 * @param screen the screen on which you want to render
	 * @param x coordinate
	 * @param y coordinate
	 * @param colour the specific color for the font
	 * @param scale the scale of the font (minimum 1 - a letter is as big as a tile)
	 */
	public static void render(String msg, Screen screen, int x, int y, int colour, int scale) {
		msg = msg.toUpperCase();
		
		for(int i=0;i<msg.length();i++) {
			int charIndex = font.indexOf(msg.charAt(i));
			if(charIndex >= 0) screen.render(x + (i*16*scale), y, charIndex + 30*32, colour, scale);
		}
	}
}
