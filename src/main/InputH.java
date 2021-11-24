package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * A simple key listener that checks the keyboard buttons
 */
public class InputH implements KeyListener{
	
	public InputH(Game game) {
		game.addKeyListener(this);
	}
	
	public class Key{
		private int nTimesPressed = 0;
		public boolean press = false;
		
		public int getNTimesPressed() {
			return nTimesPressed;
		}
		public boolean isPressed() {
			return press;
		}
		public void toggle(boolean press) {
			this.press = press;
			if(press) nTimesPressed++;
		}
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(),true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(),false);
	}
	
	public void toggleKey(int keyCode, boolean press) {
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) up.toggle(press);
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) down.toggle(press);
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) left.toggle(press);
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) right.toggle(press);
	}
}
