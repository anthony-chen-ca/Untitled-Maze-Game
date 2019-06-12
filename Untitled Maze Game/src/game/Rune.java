package game;

/**
 * Rune
 * @author Anthony
 * A class for the mysterious runes.
 */
public class Rune {
	private double xPos;
	private double yPos;

	private char symbol;
	private Sprite sprite;

	/**
	 * Rune constructor.
	 * @param xPos
	 * @param yPos
	 * @param symbol
	 */
	public Rune(double xPos, double yPos, char symbol) {
		this.sprite = new Sprite(xPos, yPos, Image.Rune);
		this.xPos = xPos;
		this.yPos = yPos;
		this.symbol = symbol;
	}

	/**
	 * getxPos
	 * @return
	 */
	public double getxPos() {
		return this.xPos;
	}

	/**
	 * setxPos
	 * This method will set the x position.
	 * @param xPos
	 */
	public void setxPos(double xPos) {
		this.xPos = xPos;
		this.sprite.xPos = xPos;
	}

	/**
	 * getyPos
	 * @return
	 */
	public double getyPos() {
		return this.yPos;
	}

	/**
	 * setyPos
	 * This method will set the y position.
	 * @param yPos
	 */
	public void setyPos(double yPos) {
		this.yPos = yPos;
		this.sprite.yPos = yPos;
	}

	/**
	 * getSprite
	 * @return
	 */
	public Sprite getSprite() {
		return this.sprite;
	}

	/**
	 * getSymbol
	 * @return
	 */
	public char getSymbol() {
		return this.symbol;
	}
}