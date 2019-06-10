package game;

public class Rune {
	private double xPos;
	private double yPos;
	
	private char symbol;
	
	private Sprite sprite;
	
	public Rune(double xPos, double yPos, char symbol) {
		this.sprite = new Sprite(xPos, yPos, Image.Rune);
		this.xPos = xPos;
		this.yPos = yPos;
		this.symbol = symbol;
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public char getSymbol() {
		return this.symbol;
	}
}
