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
		public boolean released = false;
		public boolean pressed = false;
		
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
		public boolean released() {
			if(released)
			{
				released = false;
				return true;
			}
			return false;
		}
		public boolean pressed() {
			if(pressed)
			{
				pressed = false;
				return true;
			}
			return false;
		}
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key enter = new Key();
	
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(),true);
		togglePress(e.getKeyCode());
		
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(),false);
		toggleRelease(e.getKeyCode());
	}
	
	public void toggleKey(int keyCode, boolean press) {
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) up.toggle(press);
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) down.toggle(press);
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) left.toggle(press);
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) right.toggle(press);
		if(keyCode == KeyEvent.VK_ENTER) enter.toggle(press);
	}
	public void togglePress(int keyCode)
	{
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
			up.pressed = true;
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
			down.pressed = true;
	}
	public void toggleRelease(int keyCode)
	{
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
			up.released = true;
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
			down.released = true;
	}
}
