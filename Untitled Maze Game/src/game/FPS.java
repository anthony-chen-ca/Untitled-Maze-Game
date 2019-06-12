package game;

/**
 * FPS
 * @author Anthony
 * A class for calculating/displaying the frames per second.
 */
public class FPS {
	private int startTime;
	private int endTime;
	private int frameTimes = 0;
	private short frames = 0;
	
	private short displayFrames = 60;
	
	/**
	 * start
	 * This method will start the FPS counter.
	 */
	public void start() {
		this.startTime = (int) System.currentTimeMillis();
	}
	
	/**
	 * update
	 * This method will update the FPS counter.
	 */
	public void update() {
		this.endTime = (int)System.currentTimeMillis();
		this.frameTimes = frameTimes + endTime - startTime;
		frames++;
		if (frameTimes >= 1000) {
			displayFrames = frames;
			frames = 0;
			frameTimes = 0;
		}
	}
	
	/**
	 * getFPS
	 * This method will return the display frames.
	 * @return
	 */
	public int getFPS() {
		return displayFrames;
	}
}