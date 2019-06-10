package game;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;

public class InputListenerGame extends InputListener {
	private Robot robot;
	private boolean isRecentering = false;
	public int turnAmountX = 0;
	// public int turnAmountY = 0;
	
	public InputListenerGame(int WIDTH, int HEIGHT) {
		super(WIDTH, HEIGHT);
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
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
}
