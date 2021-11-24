package tiles;

public class AnimatedTile extends BasicTile {
	private int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationSwitchDelay;
	
	
	/**
	 * @param id The id which the tile should get !Try setting the Tiles in the Tile class!
	 * @param animationCoords The animation coordinates for the frames
	 * @param tileColour
	 * @param mapColour The rgb of the pixel from the map file
	 * @param animationSwitchDelay Time in milliseconds between the frames
	 */
	public AnimatedTile(int id, int[][] animationCoords, int tileColour, int mapColour, int animationSwitchDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColour, mapColour);
		this.animationTileCoords = animationCoords;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}
	
	
	public void tick() {
		//if animationSwitchDelay passed go to the next frame for the tile
		if(System.currentTimeMillis() - lastIterationTime >= animationSwitchDelay) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
			tileId = animationTileCoords[currentAnimationIndex][0] +animationTileCoords[currentAnimationIndex][1]*32;
		}	
	}
}
