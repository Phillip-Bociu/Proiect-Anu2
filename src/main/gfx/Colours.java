package main.gfx;

public class Colours {
	
	/**
	 * Each colour will be between 000-555 the numbers represents rgb with 6 shades each
	 * @param colour1 first color
	 * @param colour2 second
	 * @param colour3 third
	 * @param colour4 forth
	 * @return We get the code for the specific colour set
	 */
	public static int get(int colour1, int colour2, int colour3, int colour4) {
		return (get(colour4)<<24) + (get(colour3)<<16) + (get(colour2)<<8) + (get(colour1));
	}
	/**
	 * @param colour will be between 000-555 the numbers represents rgb with 6 shades each
	 * @return The parsed colour
	 */
	private static int get(int colour) {
		if(colour < 0) return 255;
		int r = colour/100%10;
		int g = colour/10%10;
		int b = colour%10;
		return r*36 + g*6 + b;
	}
	
}
