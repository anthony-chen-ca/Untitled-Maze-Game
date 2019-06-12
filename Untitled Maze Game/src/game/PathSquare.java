package game;

/**
 * PathSquare
 * @author Anthony
 * A class for a path square. Used in Pathfinding.
 */
public class PathSquare {
	private int x;
	private int y;
	
	private double F = 0;
	private double G = 0; // movement cost from A to square
	private double H = 0; // destination cost from square to B
	
	private PathSquare parent;
	
	/**
	 * PathSquare constructor.
	 * @param x
	 * @param y
	 */
	public PathSquare(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * setCost
	 * This method will set the F cost.
	 * @param G
	 * @param H
	 */
	public void setCost(double G, double H) {
		this.G = G;
		this.H = H;
		this.F = G + H;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the f
	 */
	public double getF() {
		return F;
	}

	/**
	 * @param f the f to set
	 */
	public void setF(double f) {
		F = f;
	}

	/**
	 * @return the g
	 */
	public double getG() {
		return G;
	}

	/**
	 * @param g the g to set
	 */
	public void setG(double g) {
		G = g;
	}

	/**
	 * @return the h
	 */
	public double getH() {
		return H;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(double h) {
		H = h;
	}

	/**
	 * @return the parent
	 */
	public PathSquare getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(PathSquare parent) {
		this.parent = parent;
	}
}
