package game;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

public class InputListener implements MouseInputListener, KeyListener {
	public final int WIDTH;
	public final int HEIGHT;
	private Robot robot;
	private boolean isRecentering = false;
	
	// mouse
	public int x;
	public int y;
	public int turnAmountX = 0;
	// public int turnAmountY = 0;
	
	// keys
	public boolean left;
	public boolean right;
	public boolean forward;
	public boolean back;
	
	public InputListener(int WIDTH, int HEIGHT) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		if (isRecentering && e.getX() == WIDTH/2 && e.getY() == HEIGHT/2) {
			this.x = e.getX();
			this.y = e.getY();
			this.turnAmountX = 0;
			isRecentering = false;
		} else {
			this.x = e.getX();
			this.y = e.getY();
			this.turnAmountX = this.x - WIDTH/2;
			// System.out.println("Turn Amount: "+turnAmountX);
			recenterMouse();
		}
	}

	private void recenterMouse() {
		isRecentering = true;
		robot.mouseMove(WIDTH/2, HEIGHT/2);
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
}
