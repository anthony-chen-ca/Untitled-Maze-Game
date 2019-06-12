package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Image
 * @author Anthony
 * A class for loading images.
 */
public class Image {
	public static Image Wall = new Image("src/images/sci-fi-wall.png", "wall");
	public static Image Door = new Image("src/images/sci-fi-door.png", "door");
	public static Image Rune = new Image("src/images/rune.png", "rune");
	
	public static Image MacReady = new Image("src/images/MacReady.png", "macready");
	public static Image Ripley = new Image("src/images/Ripley.png", "ripley");
	public static Image Alien = new Image("src/images/Alien.png", "alien");
	
	private int[] pixels;
	private int width;
	private int height;
	private String name;
	
	/**
	 * Image constructor.
	 * @param filename
	 * @param name
	 */
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
	
	/**
	 * getWidth
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * getHeight
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * getPixels
	 * @return the pixels
	 */
	public int[] getPixels() {
		return this.pixels;
	}
	
	/**
	 * getName
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}
