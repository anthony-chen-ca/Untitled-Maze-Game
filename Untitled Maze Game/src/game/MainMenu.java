package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// screen
	public final int WIDTH;
	public final int HEIGHT;
	private MenuAreaPanel menuAreaPanel;
	
	// audio
	private AudioClip mainMenuMusic;

	public MainMenu() {
		super("Untitled Maze Game");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.WIDTH = screenSize.width;
		this.HEIGHT = screenSize.height;

		// music
		try {
			initializeMusic();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

		// initialization
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();
		
		this.menuAreaPanel = new MenuAreaPanel(WIDTH, HEIGHT);
		this.getContentPane().add(this.menuAreaPanel);
		
		this.setVisible(true);
	}
	
	public void refresh() {
		this.menuAreaPanel.repaint();
	}
	
	private class MenuAreaPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public final int WIDTH;
		public final int HEIGHT;
		private InputListener inputListener;
		
		// statuses
		private boolean mainMenuStatus = true;
		private boolean play = false;
		
		// text
		private Text title;
		
		// buttons
		private Button playButton;
		
		public MenuAreaPanel(int WIDTH, int HEIGHT) {
			this.WIDTH = WIDTH;
			this.HEIGHT = HEIGHT;
			
			// text
			title = new Text(3, "Untitled Maze Game", Color.WHITE, new Rectangle(0,0,WIDTH,HEIGHT/2));
			
			// buttons
			Text playButtonText = new Text(2, "PLAY", Color.WHITE, new Rectangle(0,HEIGHT/2,WIDTH,HEIGHT/4));
			playButton = new Button(playButtonText.getRect(), Color.DARK_GRAY, playButtonText);
			
			this.inputListener = new InputListener(WIDTH, HEIGHT);
			this.addKeyListener(inputListener);
			this.addMouseListener(inputListener);
			this.addMouseMotionListener(inputListener);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);
			
			if (mainMenuStatus == true) {
				mainMenu(g);
			}
		}
		
		public void mainMenu(Graphics g) {
			mainMenuMusic.play();
			this.setBackground(Color.BLACK);
			
			title.draw(g);
			playButton.draw(g);

			boolean clicked = inputListener.isClicked();
			if (clicked == true) {
				Point p = new Point(inputListener.x, inputListener.y);
				if (playButton.isButtonClicked(p)) {
					mainMenuStatus = false;
					play = true;
				}
				inputListener.setClicked(false);
			}
		}
	}
	
	public boolean isPlaying() {
		return this.menuAreaPanel.play;
	}
	
	public void initializeMusic()
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.mainMenuMusic = new AudioClip("MainMenu");
	}
}