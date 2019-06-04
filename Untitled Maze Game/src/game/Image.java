package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image {
	public static Image Wall = new Image("src/images/sci-fi-wall-1.png", "wall");
	public static Image CommanderWall = new Image("src/images/bricks.png", "commanderwall");
	public static Image EscapePodWall = new Image("src/images/minecraft.png", "escapepodwall");
	
	public static Image MacReady = new Image("src/images/MacReady.png", "macready");
	public static Image Ripley = new Image("src/images/Ripley.png", "ripley");
	public static Image Alien = new Image("src/images/Alien.png", "alien");
	
	private int[] pixels;
	private int width;
	private int height;
	private String name;
	
	public Image(String filename, String name) {
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			this.width = image.getWidth();
			this.height = image.getHeight();
			this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
			this.name = name;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int[] getPixels() {
		return this.pixels;
	}
	
	public String getName() {
		return this.name;
	}
}
