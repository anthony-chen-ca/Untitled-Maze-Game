package game;

/**
 * Sprite
 * @author Anthony
 * A class for sprites.
 */
public class Sprite {
	public double xPos;
	public double yPos;
	
	private double distanceToCamera;
	
	private Image image;
	
	/**
	 * Sprite constructor.
	 * @param xPos
	 * @param yPos
	 * @param image
	 */
	public Sprite(double xPos, double yPos, Image image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.image = image;
	}
	
	/**
	 * getDist
	 * @return the distanceToCamera
	 */
	public double getDist() {
		return this.distanceToCamera;
	}
	
	/**
	 * setDist
	 * @param dist
	 */
	public void setDist(double dist) {
		this.distanceToCamera = dist;
	}
	
	/**
	 * getImage
	 * @return the image
	 */
	public Image getImage() {
		return this.image;
	}
}
