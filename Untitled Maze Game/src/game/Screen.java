package game;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;

public class Screen {
	private int[][] map;
	private int width;
	private int height;
	private ArrayList<Image> textures;

	private double[] ZBuffer;

	private static Color ceiling = new Color(0,0,0);
	private static Color floor = new Color(0,0,0);
	private static Color blood = new Color(166,16,30);
	
	private static final Color GREEN_SCREEN = new Color(0,177,65);

	public Screen(int[][] map, ArrayList<Image> textures, int width, int height) {
		this.map = map;
		this.textures = textures;
		this.width = width;
		this.height = height;
		this.ZBuffer = new double[width];
	}

	/**
	 * update
	 * Recalculates the screen based on the player's position on the map.
	 * @param player
	 * @param pixels
	 * @return
	 */
	public int[] update(Player player, int[] pixels, ArrayList<Sprite> sprites) {
		/* DRAW CEILING AND FLOOR */
		int ceilingColor = ceiling.getRGB();
		for (int n = 0; n < pixels.length/2; n++) { // ceiling
			pixels[n] = ceilingColor;
		}
		int floorColor = floor.getRGB();
		for (int i = pixels.length/2; i < pixels.length; i++) { // floor
			pixels[i] = floorColor;
		}

		/* LOOP THROUGH EACH VERTICAL BAR ON THE SCREEN */
		for (int x = 0; x < width; x = x + 1) {
			double cameraX = 2 * x / (double)(width) -1;
			double rayDirX = player.xDir + player.xPlane * cameraX;
			double rayDirY = player.yDir + player.yPlane * cameraX;

			// map position
			int mapX = (int)(player.xPos);
			int mapY = (int)(player.yPos);

			// length of ray from current position to next x-side or y-side
			double sideDistX;
			double sideDistY;

			// length of ray from one side to next in map
			double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
			double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
			double perpWallDist;

			// direction to go in x and y
			int stepX;
			int stepY;
			boolean hit = false; // was a wall hit
			int side = 0; // was the wall horizontal or vertical

			// figure out the step direction and initial distance to a side
			if (rayDirX < 0) {
				stepX = -1;
				sideDistX = (player.xPos - mapX) * deltaDistX;
			} else {
				stepX = 1;
				sideDistX = (mapX + 1.0 - player.xPos) * deltaDistX;
			}
			if (rayDirY < 0) {
				stepY = -1;
				sideDistY = (player.yPos - mapY) * deltaDistY;
			} else {
				stepY = 1;
				sideDistY = (mapY + 1.0 - player.yPos) * deltaDistY;
			}
			
			/* CHECK IF RAY COLLIDES WITH WALL */
			
			while (!hit) {
				// jump to next square
				if (sideDistX < sideDistY) {
					sideDistX += deltaDistX;
					mapX += stepX;
					side = 0;
				} else {
					sideDistY += deltaDistY;
					mapY += stepY;
					side = 1;
				}
				// check if ray has hit a wall
				if (map[mapX][mapY] > 0) {
					hit = true;
				}
			}

			/* DISTANCE CALCULATION */
			
			// calculate distance to the point of impact
			if (side == 0) {
				perpWallDist = Math.abs((mapX - player.xPos + (1 - stepX) / 2) / rayDirX);
			} else {
				perpWallDist = Math.abs((mapY - player.yPos + (1 - stepY) / 2) / rayDirY);   
			}
			
			// z-buffer for sprite calculation
			ZBuffer[x] = perpWallDist;
			
			// calculate the height of the wall based on the distance from the camera
			int lineHeight;
			if (perpWallDist > 0) {
				lineHeight = Math.abs((int)(height / perpWallDist));
			} else {
				lineHeight = height;
			}
			
			// calculate lowest and highest pixel to fill in current stripe
			int drawStart = -lineHeight/2 + height/2;
			if (drawStart < 0) {
				drawStart = 0;
			}
			int drawEnd = lineHeight/2 + height/2;
			if (drawEnd >= height) {
				drawEnd = height - 1;
			}

			/* ADD TEXTURES */
			
			int texNum = map[mapX][mapY] - 1;
			double wallX; // exact position of where wall was hit
			if (side == 0) { // if it is a x-axis wall
				wallX = (player.yPos + ((mapX - player.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
			} else { // if it is a y-axis wall
				wallX = (player.xPos + ((mapY - player.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
			}
			wallX -= Math.floor(wallX);

			// calculate x coordinate on the texture
			int texX = (int)(wallX * (textures.get(texNum).getWidth()));
			if (side == 0 && rayDirX > 0) {
				texX = textures.get(texNum).getWidth() - texX - 1;
			}
			if (side == 1 && rayDirY < 0) {
				texX = textures.get(texNum).getHeight() - texX - 1;
			}

			// calculate y coordinate on texture
			for (int y = drawStart; y < drawEnd; y++) {
				int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
				int color;
				if (side == 0) {
					color = textures.get(texNum).getPixels()[Math.abs(texX + (texY * textures.get(texNum).getWidth()))];
//					color = fadeToBlack(color, perpWallDist); // TODO
				} else {
					color = (textures.get(texNum).getPixels()[Math.abs(texX + (texY * textures.get(texNum).getHeight()))]>>1) & 8355711; // shadow on y sides
//					color = fadeToBlack(color, perpWallDist); // TODO
				}
				pixels[x + y*(width)] = color;
			}
		}
		
		/* ADD SPRITES */

		// sort sprites
		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = sprites.get(i);
			double distance = calcDist(sprite.xPos, sprite.yPos, player.xPos, player.yPos);
			sprite.setDist(distance);
		}
		sprites = sortSprites(sprites);

		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = sprites.get(i);

			// distance to camera
			double spriteX = sprite.xPos - player.xPos;
			double spriteY = sprite.yPos - player.yPos;

			// multiply by inversion of 2x2 camera matrix
			// ____________1___________    [yDir      -xDir]
			// (xPlane*yDir-xDir*yPlane) * [-yPlane  xPlane]
			double inversion = 1.0 / (player.xPlane * player.yDir - player.xDir * player.yPlane);
			double xTransform = inversion * (player.yDir * spriteX - player.xDir * spriteY);
			double yTransform = inversion * (-player.yPlane * spriteX + player.xPlane * spriteY);

			// center point of the sprite
			int spriteScreenX = (int)((width / 2) * (1 + xTransform / yTransform));
			// int spriteScreenY = height / 2;

			double uDiv = 1.5;
			double vDiv = 1.5;
			double vMove = 89;
			int vMoveScreen = (int)(vMove / yTransform);
			
			// calculate sprite dimensions
			int spriteWidth = (int)Math.round((Math.abs((int)(height / (yTransform)))) / uDiv);
			int spriteHeight = (int)Math.round((Math.abs((int)(height / (yTransform)))) / vDiv);

			// calculate pixels
			int drawStartX = -spriteWidth / 2 + spriteScreenX;
			if (drawStartX < 0) {
				drawStartX = 0;
			}
			int drawEndX = spriteWidth / 2 + spriteScreenX;
			if (drawEndX >= width) {
				drawEndX = width - 1;
			}

			int drawStartY = -spriteHeight / 2 + height / 2 + vMoveScreen;
			if (drawStartY < 0) {
				drawStartY = 0;
			}
			int drawEndY = spriteHeight / 2 + height / 2 + vMoveScreen;
			if (drawEndY >= height) {
				drawEndY = height - 1;
			}

			Image image = sprite.getImage();

			// draw the sprite stripe by stripe
			// long startTime = System.currentTimeMillis();
			for (int stripe = drawStartX; stripe < drawEndX; stripe++) {
				int imageX = (int)(256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * image.getWidth() / spriteWidth) / 256;
				// if is in front of the camera (yTransformed > 0) but before the wall
				if (yTransform > 0 && stripe > 0 && stripe < width && yTransform < ZBuffer[stripe]) {
					for (int y = drawStartY; y < drawEndY; y++) {
						int d = (y-vMoveScreen) * 256 - height * 128 + spriteHeight * 128;
						int imageY = ((d * image.getHeight()) / spriteHeight) / 256;
						int color = image.getPixels()[Math.abs(imageX + (imageY * image.getWidth()))];
						if (color != GREEN_SCREEN.getRGB()) {
//							color = fadeToBlack(color, sprite.getDist()); // TODO
							pixels[stripe + y*(width)] = color;
						}
					}
				}
			}
			// long endTime = System.currentTimeMillis();
			// long duration = (endTime - startTime);
			// System.out.println(duration+" milliseconds");
		}
		
		/* BLOOD */
//		if (player.getHealth() < 20) {
//			for (int n = 0; n < pixels.length; n++) { // top
//				int color = pixels[n];
//				color = fadeToRed(color, 2);
//				pixels[n] = color;
//			}
//		}
		return pixels;
	}
	
	public static int fadeToBlack(int color, double distance) {
		if (distance <= 1) {
			return color;
		} else {
			Color c = new Color(color);
			double intensity;
			if (distance < 10) {
				intensity = Math.round((1.0/distance) * 100.0) / 100.0;
			} else {
				intensity = 0;
			}
			int r = (int) ((c.getRed() * intensity));
			int g = (int) ((c.getGreen() * intensity));
			int b = (int) ((c.getBlue() * intensity));
			return new Color(r, g, b).getRGB();
		}
	}
	
	public static int fadeToRed(int color, double distance) {
		if (distance <= 1) {
			return color;
		} else {
			Color c = new Color(color);
			double intensity = 1.0/distance;
			double redIntensity = 1.0 - intensity;
			int r = (int) ((c.getRed() * intensity) + (blood.getRed() * redIntensity));
			int g = (int) ((c.getGreen() * intensity) + (blood.getGreen() * redIntensity));
			int b = (int) ((c.getBlue() * intensity) + (blood.getBlue() * redIntensity));
			return new Color(r, g, b).getRGB();
		}
	}

	public static double calcDist(double x1, double y1, double x2, double y2) {
		return (Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
	}

	public static ArrayList<Sprite> sortSprites(ArrayList<Sprite> sprites) {
		// insertion sort
		// extract an element
		// go backwards to insert it
		// furthest away --> closest
		for (int i = 1; i < sprites.size(); i++) {
			for (int j = i; j > 0; j--) {
				if (sprites.get(j).getDist() > sprites.get(j-1).getDist()) {
					Collections.swap(sprites, j, j-1);
				}
			}
		}
		return sprites;
	}
}
