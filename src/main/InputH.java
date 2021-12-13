package main;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.MouseInputListener;
/**
 * A simple key listener that checks the keyboard buttons
 */
public class InputH implements KeyListener, MouseInputListener{
	
	public InputH(Game game, boolean keyBoard, boolean Mouse) {
		if(keyBoard)
			game.addKeyListener(this);
		if(Mouse)
			game.addMouseListener(this);
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
	public class Mouse{
		public boolean press = false;
		public boolean released = false;
		public boolean pressed = false;
		public int x,y;
		public boolean isPressed(){
			return press;
		}
		public int getX(){
			return x;
		}
		public int getY() {
			return y;
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
		public Mouse leftClick = new Mouse();
	
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

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if(!leftClick.pressed) {
			leftClick.x = e.getX();
			leftClick.y = e.getY();
		}
		leftClick.pressed = true;
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
