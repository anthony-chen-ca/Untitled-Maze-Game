package game;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class AudioClip {
	private String filename;
	private Long currentFrame;
	private Clip audioClip;
	private boolean playing;
	private AudioInputStream audioStream;
	private DataLine.Info audioInfo;
	
	public AudioClip(String filename)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.filename = filename;
		this.audioStream = AudioSystem.getAudioInputStream(
				new File ("src/audio/"+this.filename+".wav"));
		this.audioInfo = new DataLine.Info(Clip.class, this.audioStream.getFormat());
		this.audioClip = (Clip) AudioSystem.getLine(this.audioInfo);
		this.audioClip.open(this.audioStream);
	}
	
	public void play() {
		this.audioClip.start();
		this.playing = true;
	}
	
	public void pause() {
		if (this.playing == false) {
			return;
		}
		this.currentFrame = this.audioClip.getMicrosecondPosition();
		this.audioClip.stop();
		this.playing = false;
	}
	
	public void resume() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (this.playing) {
			return;
		}
        this.audioClip.close(); 
        resetAudioStream(); 
        this.audioClip.setMicrosecondPosition(this.currentFrame); 
        this.play(); 
	}
	
	public void restart() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.audioClip.stop();
		this.audioClip.close();
		resetAudioStream();
		this.currentFrame = 0L;
		this.audioClip.setMicrosecondPosition(0);
		this.play();
	}
	
	public void stop() { 
        this.currentFrame = 0L; 
        this.audioClip.stop();
        this.audioClip.close();
    }
	
	public void resetAudioStream()
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.audioStream = AudioSystem.getAudioInputStream(
				new File ("src/audio/"+this.filename+".wav"));
		this.audioInfo = new DataLine.Info(Clip.class, this.audioStream.getFormat());
		this.audioClip = (Clip) AudioSystem.getLine(this.audioInfo);
		this.audioClip.open(this.audioStream);
	}
}
