package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import entities.Player;
import main.gfx.Colours;
import main.gfx.Screen;
import main.gfx.SpriteSheet;
import map.Map;
import menu.Gui;
import networking.*;

/**
 * Main class that operates the game
 *
 */
public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	//Main variables
	public static final int WIDTH=400;
	public static final int HEIGHT=WIDTH*9/16;
	public static final int SCALE=3;
	public static final String NAME = "Octopus Game";
	public static final String ICON = "resource/icon.png";
	public static final String mapPath = "/maps/basic_map.png";
	
	private JFrame frame;
	
	public boolean running = false;
	public int tickCount = 0;
	
	private BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels =((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	//we use 6 shades for every color r, g ,b
	private int[] colours = new int[6*6*6];
	
	private Thread thread;
	private Screen screen;
	public InputH inputH;
	public Map map;
	public Player player, player2;
	public Gui gui;
	public Client client;
	public Server server;
	public boolean menu = true;
	
	/**
	 * Initializing some other variables, mostly JFrame stuff
	 */
	public Game() {
		setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		
		frame = new JFrame(NAME);
		frame.setIconImage(new ImageIcon(ICON).getImage());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void Connect(String ipAddress)
	{
		client = new Client(ipAddress);
		client.start();
	}
	
	public void Host()
	{
		server = new Server();
		server.start();
	}
	
	
	/**
	 * Method to initialize the game with canvas and bond it with the input handler
	 */
	public void init() {
		
		//register every colour in array
		int index = 0;
		for(int r = 0;r<6;r++) {
			for(int g = 0;g<6;g++) {
				for(int b = 0;b<6;b++) {
					int rr = (r*255/5);
					int gg = (g*255/5);
					int bb = (b*255/5);
					
					//first 8 bits for red, next for green and last 8 bits for blue
					colours[index++] = rr<<16 | gg <<8 | bb;
				}
			}
		}
		
		screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("/sprite_sheet.png"));
		inputH = new InputH(this);
		gui = new Gui(this,inputH);
	}
	
	/**
	 * @param host true if the player is the host
	 */
	public void initWorld(boolean host)
	{
		map = new Map(mapPath);
		if(host) {
			player = new Player(map, 16*map.spawnX, 16*map.spawnY, inputH, true, Colours.get(-1,401,502,555), "Player 1");
			player2 = new Player(map, 16*map.spawnX, 16*map.spawnY, inputH, false, Colours.get(-1,204,305,555), "Player 2");
		}
		else
		{
			player = new Player(map, 16*map.spawnX, 16*map.spawnY, inputH, true, Colours.get(-1,204,305,555), "Player 2");
			player2 = new Player(map, 16*map.spawnX, 16*map.spawnY, inputH, false, Colours.get(-1,401,502,555), "Player 1");
		}
		map.addEntity(player);
		map.addEntity(player2);
	}
	
	
	/**
	 * Starting the thread for the game
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	/**
	 * Stopping the game(currently useless)
	 */
	public synchronized void stop() {	
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		System.exit(0);
	}
	
	/**
	 * Simple mechanics of the game
	 * every tick the game is processing (60 ticks/second - standard rate)
	 * every frame the game renders
	 */
	public void run() {
		long certainTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int frames = 0;
		int ticks = 0;
		
		long certainTimer = System.currentTimeMillis();
		//delta adds up the time between the last registered time and the actual time
		double delta = 0;
		
		init();
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - certainTime)/nsPerTick;
			certainTime = now;
			boolean toggleRender = true;
			
			//if delta reaches 1 (around 1/60 of a second) a tick should happen
			while(delta >= 1)
			{
				ticks++;
				tick();
				//delta goes back
				delta -= 1;
				toggleRender = true;
			}

			
			if(toggleRender)
			{
				frames++;
				render();
			}
			
			//reset number of frames and ticks to 0 every second so we can see fps and ticks per second
			if(System.currentTimeMillis() - certainTimer >= 1000)
			{
				System.out.println("fps - " + frames + "    ticks - " + ticks);
				certainTimer +=1000;
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	
	/**
	 * Processes stuff
	 */
	public void tick() {
		tickCount++;
		
		if(menu)
			gui.tick();
		else
			map.tick();
		
	}
	
	/**
	 * Mostly graphics stuff
	 */
	public void render() {
		BufferStrategy bffstrtgy = getBufferStrategy();
		if(bffstrtgy == null) {
			createBufferStrategy(3);
			return;
		}
		
		
		
		if(menu)
			gui.render(screen);
		else
		{
			int xOffset = player.x - (screen.width/2);
			int yOffset = player.y - (screen.height/2);
			map.renderTiles(screen, xOffset, yOffset);
			map.renderEntities(screen);
			player.renderHealth(screen,3,screen.height - 16);
			player2.renderHealth(screen,screen.width - 17*10,screen.height - 16);
		}
		
		
		for(int y = 0;y<screen.height;y++) {
			for(int x = 0;x<screen.width;x++) {
				int colourCode = screen.pixels[x + y * screen.width];
				if(colourCode<255) pixels[x + y * WIDTH] = colours[colourCode];
			}
		}
		
		Graphics graph = bffstrtgy.getDrawGraphics();
	 	graph.drawRect(0,0,getWidth(),getHeight());
		graph.drawImage(img,0,0,getWidth(),getHeight(),null);
		graph.dispose();
		bffstrtgy.show();
	}
	
	public static void main(String args[]) {
		new Game().start();
	}

}
