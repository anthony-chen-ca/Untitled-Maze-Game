package game;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * AudioClip
 * @author Anthony
 * A class for playing audio.
 */
public class AudioClip {
	private String filename;
	private Long currentFrame;
	private Clip audioClip;
	private boolean playing;
	private AudioInputStream audioStream;
	private DataLine.Info audioInfo;

	/**
	 * AudioClip constructor.
	 * @param filename
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public AudioClip(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.filename = filename;
		this.audioStream = AudioSystem.getAudioInputStream(new File("src/audio/" + this.filename + ".wav"));
		this.audioInfo = new DataLine.Info(Clip.class, this.audioStream.getFormat());
		this.audioClip = (Clip) AudioSystem.getLine(this.audioInfo);
		this.audioClip.open(this.audioStream);
	}

	/**
	 * play
	 * This method will play the audio clip.
	 */
	public void play() {
		this.audioClip.start();
		this.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		this.playing = true;
	}

	/**
	 * pause
	 * This method will pause the audio clip.
	 */
	public void pause() {
		if (this.playing == false) {
			return;
		}
		this.currentFrame = this.audioClip.getMicrosecondPosition();
		this.audioClip.stop();
		this.playing = false;
	}

	/**
	 * resume
	 * This method will resume the audio clip.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void resume() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (this.playing) {
			return;
		}
		this.audioClip.close();
		resetAudioStream();
		this.audioClip.setMicrosecondPosition(this.currentFrame);
		this.play();
	}

	/**
	 * restart
	 * This method will restart the audio clip.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void restart() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.audioClip.stop();
		this.audioClip.close();
		resetAudioStream();
		this.currentFrame = 0L;
		this.audioClip.setMicrosecondPosition(0);
		this.play();
	}

	/**
	 * stop
	 * This method will stop the audio clip.
	 */
	public void stop() {
		this.currentFrame = 0L;
		this.audioClip.stop();
		this.audioClip.close();
	}

	/**
	 * resetAudioStream
	 * This method will reset the audio stream.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.audioStream = AudioSystem.getAudioInputStream(new File("src/audio/" + this.filename + ".wav"));
		this.audioInfo = new DataLine.Info(Clip.class, this.audioStream.getFormat());
		this.audioClip = (Clip) AudioSystem.getLine(this.audioInfo);
		this.audioClip.open(this.audioStream);
		this.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * setVolume
	 * This method will set the volume.
	 * @param level
	 */
	public void setVolume(float level) {
		FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
	}
}
