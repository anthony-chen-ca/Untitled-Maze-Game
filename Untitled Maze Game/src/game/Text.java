package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

public class Text {
	private Font textFont; // 1
	private Font headingFont; // 2
	private Font titleFont; // 3
	
	private int type;
	private String text;
	private Color color;
	private Rectangle rect;
	
	public Text(int type, String text, Color color, Rectangle rect) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		try {
//			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/MetalMania-Regular.ttf")));
//		} catch (FontFormatException | IOException e) {
//			e.printStackTrace();
//		}
		
		//setting different font styles 
		this.textFont = new Font("Arial", Font.PLAIN, 30);
		this.headingFont = new Font("Arial", Font.PLAIN, 50);
		this.titleFont = new Font("Arial", Font.PLAIN, 80);
		
		this.type = type;
		this.text = text;
		this.color = color;
		this.rect = rect;
	}
	
	public void draw(Graphics g) {
		FontMetrics metrics;
		if (type == 1) {
			metrics = g.getFontMetrics(textFont);
			g.setFont(textFont);
		} else if (type == 2) {
			metrics = g.getFontMetrics(headingFont);
			g.setFont(headingFont);
		} else {
			metrics = g.getFontMetrics(titleFont);
			g.setFont(titleFont);
		}
		
		int centeredX = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int centeredY = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setColor(color);
		g.drawString(text, centeredX, centeredY);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Rectangle getRect() {
		return this.rect;
	}
}
