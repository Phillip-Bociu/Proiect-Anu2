package main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH=300;
	public static final int HEIGHT=WIDTH*9/16;
	public static final int SCALE=4;
	public static final String NAME = "Octopus Game";
	public static final String ICON = "resources2/icon.png";
	
	private JFrame frame;
	
	public boolean running = false;
	public int tickCount = 0;
	
	private BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels =((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	public Game() {
		
		//Constants
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
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}
	public synchronized void stop() {	
	}
	
	public void run() {
		long certainTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int frames = 0;
		int ticks = 0;
		
		long certainTimer = System.currentTimeMillis();
		double delta = 0;
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - certainTime)/nsPerTick;
			certainTime = now;
			boolean toggleRender = true;
			
			while(delta >= 1)
			{
				ticks++;
				tick();
				delta -= 1;
				toggleRender = true;
			}
			
			try {
				Thread.sleep(0);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			if(toggleRender)
			{
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - certainTimer >= 1000)
			{
				certainTimer +=1000;
				System.out.println("fps = " + frames + " ticks = " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void tick() {
		tickCount++;
		for(int i=0;i<pixels.length;i++) {
			pixels[i] = i*tickCount;
		}
	}
	
	public void render() {
		BufferStrategy bffstrtgy = getBufferStrategy();
		if(bffstrtgy == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics graph = bffstrtgy.getDrawGraphics();
		
		//graph.setColor(Color.BLACK);
		//graph.fillRect(0,0,getWidth(),getHeight());
	
		graph.drawImage(img,0,0,getWidth(),getHeight(),null);
		graph.setColor(Color.BLACK);
		graph.drawRect(0,0,getWidth(),getHeight());
		
		graph.dispose();
		bffstrtgy.show();
	}
	
	public static void main(String args[]) {
		new Game().start();
	}

}
