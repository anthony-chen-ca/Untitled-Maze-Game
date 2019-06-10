package game;

public class Player extends Entity {
	private static final long serialVersionUID = 1L;
	
	private int health;
	
	public Player(double x, double y, Image image) {
		super(x, y, 1, 1, 0.55, -0.55, new Sprite(x, y, image), 0.06);
		this.health = 100;
	}
	
//	public Player(double x, double y, double xd, double yd, double xp, double yp, Image image) {
//		super(x, y, xd, yd, xp, yp, new Sprite(x, y, image), 0.06);
//		this.health = 100;
//	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void move(int[][] map, InputListener inputListener) {
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
//		if (left) { // perpendicular
//			double xValue = (xDir+1.57)*MOVE_SPEED;
//			if (map[(int)(xPos - xValue)][(int)yPos] == 0) {
//				xPos -= xValue; // setXPOS
//              sprite.xPos = xPos;
//			}
//			double yValue = (yDir+1.57)*MOVE_SPEED;
//			if (map[(int)xPos][(int)(yPos - yValue)] == 0) {
//				yPos -= yValue; // setYPOS
//              sprite.yPos = yPos;
//			}
//		}
//		if (right) {
//			double xValue = (xDir+1.57)*MOVE_SPEED;
//			if (map[(int)(xPos + xValue)][(int)yPos] == 0) {
//				xPos += xValue; // setXPOS
//              sprite.xPos = xPos;
//			}
//			double yValue = (yDir+1.57)*MOVE_SPEED;
//			if (map[(int)xPos][(int)(yPos + yValue)] == 0) {
//				yPos += yValue; // setYPOS
//              sprite.yPos = yPos;
//			}
//		}
		// if (turnAmountX < 0) { // look left
		if (left) {
			rotateLeft();
		}
		// if (turnAmountX > 0) { // look right
		if (right) {
			rotateRight();
		}
		
		// win condition
		if (xPos >= map.length - 3) {
			System.out.println("YOU WIN!");
			System.exit(0);
		}
	}
	
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
	
	public void openDoor(int[][] map) {
		
	}
}
