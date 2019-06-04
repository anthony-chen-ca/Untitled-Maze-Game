package game;

public class Sprite {
	public double xPos;
	public double yPos;
	
	private double distanceToCamera;
	
	private Image image;
	
	public Sprite(double xPos, double yPos, Image image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.image = image;
	}
	
	public double getDist() {
		return this.distanceToCamera;
	}
	
	public void setDist(double dist) {
		this.distanceToCamera = dist;
	}
	
	public Image getImage() {
		return this.image;
	}
}
