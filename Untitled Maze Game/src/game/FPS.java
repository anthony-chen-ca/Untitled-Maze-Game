package game;

public class FPS {
	private int startTime;
	private int endTime;
	private int frameTimes = 0;
	private short frames = 0;
	
	private short displayFrames = 60;
	
	public void start() {
		this.startTime = (int) System.currentTimeMillis();
	}
	
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
	
	public int getFPS() {
		return displayFrames;
	}
}