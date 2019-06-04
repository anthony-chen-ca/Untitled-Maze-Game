package game;

public abstract class Entity {
	protected double xPos;
	protected double yPos;
	protected double xDir;
	protected double yDir;
	protected double xPlane;
	protected double yPlane;
	
	public double MOVE_SPEED = .06;
	public double ROTATION_SPEED = .040;
	
	protected Sprite sprite;
	
	public Entity(double x, double y, double xd, double yd, double xp, double yp, Sprite sprite, double MOVE_SPEED) {	
		this.xPos = x;
		this.yPos = y;
		this.xDir = xd;
		this.yDir = yd;
		this.xPlane = xp;
		this.yPlane = yp;
		this.sprite = sprite;
		this.MOVE_SPEED = MOVE_SPEED;
	}

	/**
	 * @return the xPos
	 */
	public double getxPos() {
		return xPos;
	}

	/**
	 * @param xPos the xPos to set
	 */
	public void setxPos(double xPos) {
		this.xPos = xPos;
		this.sprite.xPos = xPos;
	}

	/**
	 * @return the yPos
	 */
	public double getyPos() {
		return yPos;
	}

	/**
	 * @param yPos the yPos to set
	 */
	public void setyPos(double yPos) {
		this.yPos = yPos;
		this.sprite.yPos = yPos;
	}

	/**
	 * @return the xDir
	 */
	public double getxDir() {
		return xDir;
	}

	/**
	 * @param xDir the xDir to set
	 */
	public void setxDir(double xDir) {
		this.xDir = xDir;
	}

	/**
	 * @return the yDir
	 */
	public double getyDir() {
		return yDir;
	}

	/**
	 * @param yDir the yDir to set
	 */
	public void setyDir(double yDir) {
		this.yDir = yDir;
	}

	/**
	 * @return the xPlane
	 */
	public double getxPlane() {
		return xPlane;
	}

	/**
	 * @param xPlane the xPlane to set
	 */
	public void setxPlane(double xPlane) {
		this.xPlane = xPlane;
	}

	/**
	 * @return the yPlane
	 */
	public double getyPlane() {
		return yPlane;
	}

	/**
	 * @param yPlane the yPlane to set
	 */
	public void setyPlane(double yPlane) {
		this.yPlane = yPlane;
	}
	
	/**
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return this.sprite;
	}
	
	/**
	 * @param sprite the sprite to set
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
