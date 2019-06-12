package game;

/**
 * Player
 * @author Anthony
 * A class for the player.
 */
public class Player extends Entity {
	private int health;
	
	/**
	 * Player constructor.
	 * @param x
	 * @param y
	 * @param image
	 */
	public Player(double x, double y, Image image) {
		super(x, y, 1, 1, 0.55, -0.55, new Sprite(x, y, image), 0.06);
		this.health = 100;
	}
	
	/**
	 * move
	 * This method will move the player according to the input listener.
	 * @param map
	 * @param inputListener
	 */
	public void move(int[][] map, InputListenerGame inputListener) {
		boolean forward = inputListener.forward;
		boolean back = inputListener.back;
		boolean left = inputListener.left;
		boolean right = inputListener.right;
		
		// int turnAmountX = inputListener.turnAmountX;
		
		if (forward) {
			moveForward(map);
		}
		if (back) {
			moveBackward(map);
		}
		if (left) { // look left
			rotateLeft();
		}
		if (right) { // look right
			rotateRight();
		}
		
		// win condition
		if (xPos >= map.length - 3) {
			System.out.println("YOU WIN!");
			System.exit(0);
		}
	}
	
	/**
	 * moveForward
	 * This method will move forward.
	 * @param map
	 */
	private void moveForward(int[][] map) {
		double xValue = xDir*MOVE_SPEED;
		if (map[(int)(xPos + xValue)][(int)yPos] == 0) {
			setxPos(xPos + xValue);
		}
		
		double yValue = yDir*MOVE_SPEED;
		if (map[(int)xPos][(int)(yPos + yValue)] == 0) {
			setyPos(yPos + yValue);
		}
	}
	
	/**
	 * moveBackward
	 * This method will move backwards.
	 * @param map
	 */
	private void moveBackward(int[][] map) {
		double xValue = xDir*MOVE_SPEED;
		if (map[(int)(xPos - xValue)][(int)yPos] == 0) {
			setxPos(xPos - xValue);
		}
		
		double yValue = yDir*MOVE_SPEED;
		if (map[(int)xPos][(int)(yPos - yValue)] == 0) {
			setyPos(yPos - yValue);
		}
	}

//	private void moveLeft(int[][] map) {
////		float xrotrad;
////		xrotrad = (float)(xDir / 180 * Math.PI);
////		
////		double xValue = (float)(Math.cos(xrotrad)) * MOVE_SPEED;
//		double xValue = xPlane*MOVE_SPEED;
//		if (map[(int)(xPos + xValue)][(int)yPos] == 0) {
//			setxPos(xPos + xValue);
//		}
//		
////		float yrotrad;
////		yrotrad = (float)(yDir / 180 * Math.PI);
////		
////		double yValue = (float)(Math.sin(yrotrad)) * MOVE_SPEED;
//		double yValue = yPlane*MOVE_SPEED;
//		if (map[(int)yPos][(int)(yPos + yValue)] == 0) {
//			setyPos(yPos + yValue);
//		}
//	}
	
//	private void moveRight(int[][] map) {
//		float xrotrad;
//		xrotrad = (float)(xDir / 180 * Math.PI);
//		
//		double xValue = (float)(Math.cos(xrotrad)) * MOVE_SPEED;
//		if (map[(int)(xPos - xValue)][(int)yPos] == 0) {
//			setxPos(xPos - xValue);
//		}
//		
//		float yrotrad;
//		yrotrad = (float)(yDir / 180 * Math.PI);
//		
//		double yValue = (float)(Math.sin(yrotrad)) * MOVE_SPEED;
//		if (map[(int)yPos][(int)(yPos - yValue)] == 0) {
//			setyPos(yPos - yValue);
//		}
//	}
	
	/**
	 * rotateLeft
	 * This method will rotate to the left.
	 */
	private void rotateLeft() {
		double rotateSpeed = ROTATION_SPEED;
		// rotateSpeed = rotateSpeed * (turnAmountX / 10000);
		
		double oldxDir = xDir;
		xDir = xDir*Math.cos(rotateSpeed) - yDir*Math.sin(rotateSpeed);
		yDir = oldxDir*Math.sin(rotateSpeed) + yDir*Math.cos(rotateSpeed);
		double oldxPlane = xPlane;
		xPlane = xPlane*Math.cos(rotateSpeed) - yPlane*Math.sin(rotateSpeed);
		yPlane = oldxPlane*Math.sin(rotateSpeed) + yPlane*Math.cos(rotateSpeed);
		
		// inputListener.turnAmountX = 0;
	}
	
	/**
	 * rotateRight
	 * This method will rotate to the right.
	 */
	private void rotateRight() {
		double rotateSpeed = -ROTATION_SPEED;
		// rotateSpeed = rotateSpeed * (turnAmountX / 10000);
		
		double oldxDir = xDir;
		xDir = xDir*Math.cos(rotateSpeed) - yDir*Math.sin(rotateSpeed);
		yDir = oldxDir*Math.sin(rotateSpeed) + yDir*Math.cos(rotateSpeed);
		double oldxPlane = xPlane;
		xPlane = xPlane*Math.cos(rotateSpeed) - yPlane*Math.sin(rotateSpeed);
		yPlane = oldxPlane*Math.sin(rotateSpeed) + yPlane*Math.cos(rotateSpeed);
		
		// inputListener.turnAmountX = 0;
	}
	
	/**
	 * getHealth
	 * @return the health
	 */
	public int getHealth() {
		return this.health;
	}
	
	/**
	 * setHealth
	 * @param health
	 */
	public void setHealth(int health) {
		this.health = health;
	}
}
