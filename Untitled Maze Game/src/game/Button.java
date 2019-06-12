package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Button
 * @author Anthony
 * A class for clicking buttons.
 */
public class Button {
	private Rectangle rect;
	private Color color;
	private Text text;
	
	/**
	 * Button constructor.
	 * @param rect
	 * @param color
	 * @param text
	 */
	public Button(Rectangle rect, Color color, Text text) {
		this.rect = rect;
		this.color = color;
		this.text = text;
	}
	
	/**
	 * draw
	 * This method will draw the button.
	 * @param g
	 */
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		text.draw(g);
	}
	
	/**
	 * isButtonClicked
	 * This method will return true if a point is located in the button.
	 * @param p
	 * @return
	 */
	public boolean isButtonClicked(Point p) {
		return this.rect.contains(p);
	}
	
	/**
	 * updateColors
	 * This method will update the colors of the button.
	 * @param buttonColor
	 * @param textColor
	 */
	public void updateColors(Color buttonColor, Color textColor) {
		this.color = buttonColor;
		this.text.setColor(textColor);
	}
}