package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Button {
	private Rectangle rect;
	private Color color;
	private Text text;
	
	public Button(Rectangle rect, Color color, Text text) {
		this.rect = rect;
		this.color = color;
		this.text = text;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		text.draw(g);
	}
	
	public boolean isButtonClicked(Point p) {
		return this.rect.contains(p);
	}
	
	public void updateColors(Color buttonColor, Color textColor) {
		this.color = buttonColor;
		this.text.setColor(textColor);
	}
}