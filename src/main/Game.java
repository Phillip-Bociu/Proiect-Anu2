package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import entities.Player;
import main.gfx.Colours;
import main.gfx.Screen;
import main.gfx.SpriteSheet;
import map.Map;
import menu.Gui;
import menu.Lobby;
import menu.Login;
import menu.ScreenMessage;
import networking.*;

/**
 * Main class that operates the game
 *
 */
enum gameState{
	battle,
	menu,
	gameOver,
	lobby
}
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
	public Map map;
	public Player player, player2;
	public Gui gui;
	public ScreenMessage screenMessage;
	public Lobby lobby;
	public Client client;
	public Server server;
	public String fpsCounter;
	public boolean host;
	public gameState state;
	public Account account;
	public DatagramSocket socket;
	public InetAddress serverAddress;
	public int userID;
	/**
	 * Initializing some other variables, mostly JFrame stuff
	 */
	public Game(String username, int elo, int ID, DatagramSocket socket, InetAddress serverAddress) {
		this.account = new Account(username, elo);
		this.socket = socket;
		this.serverAddress = serverAddress;
		this.userID = ID;
		
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
	
	public void QueueUp()
	{
		QueueUpPacket qu = new QueueUpPacket();
		qu.ID = userID;
		try {			
			DatagramPacket p = new DatagramPacket(qu.toBytes(), qu.toBytes().length, serverAddress, 1331);
			socket.send(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DeQueue()
	{
		DeQueuePacket dq = new DeQueuePacket();
		dq.ID = userID;
		try {			
			DatagramPacket p = new DatagramPacket(dq.toBytes(), dq.toBytes().length, serverAddress, 1331);
			socket.send(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		//account = new Account("JohnSeed",312);
		initGui();
		
	}
	
	/**
	 * @param host true if the player is the host
	 */
	public void initGui()
	{
		screen.xOffset = 0;
		screen.yOffset = 0;
		state = gameState.menu;
		screenMessage = null;
		lobby = null;
		gui = new Gui(this,new InputH(this,true,false));
	}
	public void initLobby()
	{
		host = true;
		screen.xOffset = 0;
		screen.yOffset = 0;
		state = gameState.lobby;
		screenMessage = null;
		gui = null;
		lobby = new Lobby(this, new InputH(this, true, true), host);
	}
	public void initScreenMessage(String message)
	{
		screen.xOffset = 0;
		screen.yOffset = 0;
		state = gameState.gameOver;
		map=null;
		screenMessage = new ScreenMessage(this,message,new InputH(this,true,false));
	}
	public void initWorld(boolean host)
	{
		this.host = host;
		state = gameState.battle;
		map = new Map(mapPath,this);
		if(host) {
			player = new Player(map, account.username, screen, 16*map.spawnX[0], 16*map.spawnY[0], new InputH(this,true,true), true, Colours.get(-1,401,502,555));
			player2 = new Player(map, lobby.opponentUsername, screen, 16*map.spawnX[1], 16*map.spawnY[1], new InputH(this,false,false), false, Colours.get(-1,204,305,555));
		}
		else
		{
			player = new Player(map, account.username, screen, 16*map.spawnX[1], 16*map.spawnY[1], new InputH(this,true,true), true, Colours.get(-1,204,305,555));
			player2 = new Player(map, lobby.opponentUsername, screen, 16*map.spawnX[0], 16*map.spawnY[0], new InputH(this,false,false), false, Colours.get(-1,401,502,555));
		}
		lobby = null;
		map.addEntity(player);
		map.addEntity(player2);
		//384 - 432
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
				fpsCounter = "fps - " + frames + "    ticks - " + ticks;
				//System.out.println(fpsCounter);
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
		switch(state){
			case menu:
			{
				gui.tick();
				break;
			}
			case lobby:
			{
				lobby.tick();
				break;
			}
			case battle:
			{
				map.tick();
				break;
			}
			case gameOver:
			{
				screenMessage.tick();
				break;
			}
		default:
			break;
		}
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
		switch(state){
			case menu:
			{
				gui.render(screen);
				break;
			}
			case lobby:
			{
				lobby.render(screen);
				break;
			}
			case battle:
			{
				int xOffset = (int)(player.x) - (screen.width/2);
				int yOffset = (int)(player.y) - (screen.height/2);
				map.renderTiles(screen, xOffset, yOffset);
				map.renderEntities(screen);
				player.renderHealth(screen,3,screen.height - 16);
				player.renderName(screen,1,1);
				player2.renderHealth(screen,screen.width - 17*10,screen.height - 16);
				player2.renderName(screen,screen.width - player2.name.length()*16,1);
				break;
			}
			case gameOver:
			{
				screenMessage.render(screen);
				break;
			}
			default:
				break;
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
		
		Login frame = new Login("26.176.53.192");
        frame.setTitle("Login Menu");
        frame.setVisible(true);
        frame.setBounds(400, 200, 370, 280);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(ICON).getImage());
        frame.setResizable(false);
	//	new Game().start();
	}

}
