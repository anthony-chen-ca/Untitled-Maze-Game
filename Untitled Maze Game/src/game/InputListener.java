package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

public class InputListener implements MouseInputListener, KeyListener {
	public final int WIDTH;
	public final int HEIGHT;
	
	// mouse
	public int x;
	public int y;
	private boolean clicked = false;
	
	// keys
	public boolean left;
	public boolean right;
	public boolean forward;
	public boolean back;
	
	public boolean interact;
	
	public InputListener(int WIDTH, int HEIGHT) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		this.clicked = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_W)) {
			forward = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_A)) {
			left = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_S)) {
			back = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_D)) {
			right = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_E)) {
			interact = true;
		}
		if ((e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_W)) {
			forward = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_A)) {
			left = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_S)) {
			back = false;
		}
		if ((e.getKeyCode() == KeyEvent.VK_D)) {
			right = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	public boolean isClicked() {
		return this.clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
}
