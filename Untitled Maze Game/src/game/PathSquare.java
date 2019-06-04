package game;

public class PathSquare {
	private int x;
	private int y;
	
	private double F = 0;
	private double G = 0; // movement cost from A to square
	private double H = 0; // destination cost from square to B
	
	private PathSquare parent;
	
	public PathSquare(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public double getG() {
		return this.G;
	}
	
	public void setG(double G) {
		this.G = G;
	}
	
	public double getH() {
		return this.H;
	}
	
	public void setH(double H) {
		this.H = H;
	}
	
	public double getF() {
		return this.F;
	}
	
	public void setCost(double G, double H) {
		this.G = G;
		this.H = H;
		this.F = G + H;
	}
	
	public void setParent(PathSquare square) {
		this.parent = square;
	}
	
	public PathSquare getParent() {
		return this.parent;
	}
}
