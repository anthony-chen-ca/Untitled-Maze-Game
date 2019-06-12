package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Game
 * @author Anthony
 * A class for running the game. Operates as a client.
 */
public class Game extends JFrame {
	private static final long serialVersionUID = 1L;

	// screen
	public final int WIDTH;
	public final int HEIGHT;
	private int[][] map;
	private ArrayList<Image> textures;
	private BufferedImage screenImage;
	private int[] pixels;
	private Screen screen;
	private FPS fps = new FPS();
	private InputListenerGame inputListener;
	
	// client
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private boolean running = true;
	private boolean gameRunning = false;
	
	private int clientNum;
	private String ip = "127.0.0.1";
	private int port = 6666;

	// font
	private Font font = new Font("Calibri", Font.BOLD, 40);

	// audio
	private AudioClip ambience;
	private AudioClip breathing;
	
	// game objects
	private ArrayList<Player> players;
	private Alien alien;
	private ArrayList<Rune> runes;
	
	// password
	private String password = "123";
	
	/**
	 * Game constructor.
	 */
	public Game() {
		// screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.WIDTH = screenSize.width;
		this.HEIGHT = screenSize.height;
		this.textures = new ArrayList<Image>();
		this.textures.add(Image.Wall);
		this.textures.add(Image.Door);
		this.screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt)screenImage.getRaster().getDataBuffer()).getData();
		
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setLocationRelativeTo(null);
		
		// music
		try {
			initializeMusic();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

		// invisible cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.getContentPane().setCursor(blankCursor);

		// listeners
		this.inputListener = new InputListenerGame(WIDTH, HEIGHT);
		this.addKeyListener(inputListener);
		this.addMouseListener(inputListener);
		this.addMouseMotionListener(inputListener);
	}

	/**
	 * start
	 * This method will start the game and connect to a server.
	 */
	public synchronized void start() {
		running = true;

		System.out.println("CLIENT SIDE:");
		connect(ip, port); // my computer
		play();
	}

	/**
	 * connect
	 * This method will connect to an IP address and port.
	 * @param address
	 * @param port
	 * @return
	 */
	public Socket connect(String address, int port) {
		try {
			this.socket = new Socket(address, port);
			InputStreamReader stream = new InputStreamReader(socket.getInputStream());
			this.input = new BufferedReader(stream);
			this.output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Connection to server failed.");
		}
		return socket;
	}

	/**
	 * play
	 * This method will play the game.
	 */
	public void play() {
		// join messages
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Enter your avatar:");
//		String avatar = scanner.nextLine();
//		if (avatar.equals("macready")) {
//			sendMessage("JOIN macready");
//		} else {
//			sendMessage("JOIN ripley");
//		}
//		System.out.println("Enter to continue.");
//		String debug = scanner.nextLine();
//		sendMessage("READY");
//		scanner.close();
		
		sendMessage("JOIN macready");
		sendMessage("READY");
		
		requestFocus();
		setVisible(true);
		
		while (running) {
			// send and get messages from server
			try {
				if (input.ready()) {
					String message = input.readLine();
					message = message.trim();
					System.out.println("SERVER MESSAGE:\n"+message);
					getData(message);
				}
				if (gameRunning == true) {
					gameLoop();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		close();
	}

	/**
	 * gameLoop
	 * This method contains the main game loop.
	 */
	public void gameLoop() {
		this.ambience.play();
		if (players.get(clientNum).getHealth() <= 20) {
			this.breathing.play();
		}
		fps.start();
		// sendMessage("PLAYER"); // move networking
		ArrayList<Sprite> sprites = getSprites(clientNum);
		screen.update(players.get(clientNum), pixels, sprites); // update screen
		players.get(clientNum).move(map, inputListener);
		if (inputListener.interact == true) {
			interact(players.get(clientNum));
		}
		alien.move(map, players);
		drawFrame(); // displays to the screen in unrestricted time
		fps.update();
	}
	
	/**
	 * interact
	 * This method will have a player interact with the game world.
	 * @param player
	 */
	private void interact(Player player) {		
		// look around
    	double xPos = player.getxPos();
		double yPos = player.getyPos();
		int[] doorPos = new int[2];
		if (map[((int)xPos)-1][((int)yPos)] == 2) { // left
			doorPos[0] = ((int)xPos)-1;
			doorPos[1] = ((int)yPos);
		} else if (map[((int)xPos)][((int)yPos)-1] == 2) { // up 
			doorPos[0] = ((int)xPos);
			doorPos[1] = ((int)yPos)-1;
		} else if (map[((int)xPos)+1][((int)yPos)] == 2) { // right
			doorPos[0] = ((int)xPos)+1;
			doorPos[1] = ((int)yPos);
		} else if (map[((int)xPos)][((int)yPos)+1] == 2) { // down 
			doorPos[0] = ((int)xPos);
			doorPos[1] = ((int)yPos)+1;
		} else { // does not exist
			doorPos[0] = -1;
			doorPos[1] = -1;
		}

		if (doorPos[0] != -1) { // if door is nearby
			String input = JOptionPane.showInputDialog("Enter the password:");
			if (input != null) {
				if (input.equals(password)) {
					map[doorPos[0]][doorPos[1]] = 0;
				}
			}
		} else {
			for (Rune rune : runes) {
				if ((int)rune.getxPos() == (int)xPos) {
					if ((int)rune.getyPos() == (int)yPos) {
						char c = rune.getSymbol();
						JOptionPane.showMessageDialog(null, c, "Rune", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}

	    inputListener.interact = false;
	}
	
	/**
	 * drawFrame
	 * This method will draw a frame of the screen.
	 */
	public void drawFrame() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(screenImage, 0, 0, screenImage.getWidth(), screenImage.getHeight(), null);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("FPS: "+fps.getFPS(), 100, 100);
		
		g.setColor(Color.RED);
		g.drawString("HEALTH: "+players.get(0).getHealth(), 100, 200);

		bs.show();
		g.dispose();
	}

	/**
	 * initializeMusic
	 * This method will initialize audio clips.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void initializeMusic()
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.ambience = new AudioClip("Ambience");
		this.breathing = new AudioClip("Breathing");
//		this.gameOverMusic = new AudioClip("GameOver");
//		this.winMusic = new AudioClip("Win");
	}

	/**
	 * getSprites
	 * This method will return the sprites a player needs to draw to their screen.
	 * @param numPlayer
	 * @return
	 */
	public ArrayList<Sprite> getSprites(int numPlayer) {
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();

		// add alien
		sprites.add(alien.getSprite());

		// add other players
		for (int i = 0; i < players.size(); i++) {
			if (i != numPlayer) {
				sprites.add(players.get(i).getSprite());
			}
		}
		
		// add runes
		for (int i = 0; i < runes.size(); i++) {
			sprites.add(runes.get(i).getSprite());
		}
		
		return sprites;
	}
	
	/**
	 * sendMessage
	 * This method will send a message to the server.
	 * @param type
	 */
	public void sendMessage(String type) {
		String message = "ERROR";
		if (type.substring(0,4).equals("JOIN")) {
			// JOIN - Join a server.
			message = type;
		} else if (type.equals("READY")) {
			// READY - Ready to start the game.
			message = "READY";
		} else if (type.equals("PLAYER")) {
			// PLAYER - Send input listener controls.
			message = "PLAYER ";
			message += Boolean.toString(inputListener.forward)+" ";
			message += Boolean.toString(inputListener.back)+" ";
			message += Boolean.toString(inputListener.left)+" ";
			message += Boolean.toString(inputListener.right)+" ";
			message += Boolean.toString(inputListener.interact);
		}
		output.println(message);
		output.flush();
	}
	
	/**
	 * getData
	 * This method will parse data from a server message.
	 * @param message
	 */
	public void getData(String message) {
		if (message.substring(0,5).equals("ERROR")) {
			// ERROR - Something went wrong!

			System.exit(0);
		}  else if (message.substring(0,5).equals("CLIENT")) {
			// CLIENT - Receive client number identifier.
			
			message = message.substring(6);
			this.clientNum = Integer.parseInt(message);
		} else if (message.substring(0,5).equals("START")) {
			// START - The server is ready to start up the game.

			this.gameRunning = true;
		} else if (message.substring(0,3).equals("MAP")) {
			// MAP - Receive the map information.

			parseMap(message.substring(4));
		} else if (message.substring(0,5).equals("SETUP")) {
			// SETUP - Set up the game object positions.

			parseSetup(message.substring(6));
		} else if (message.substring(0,4).equals("GAME")) {
			// GAME - Receive game state.
			
			parseGame(message.substring(5));
		}
	}
	
	/**
	 * parseMap
	 * This method will parse a map message.
	 * @param message
	 */
	private void parseMap(String message) {
		int width = Integer.parseInt(message.substring(0, message.indexOf(' ')));
		message = message.substring(message.indexOf(' ')+1);
		int height = Integer.parseInt(message.substring(0, message.indexOf(' ')));
		message = message.substring(message.indexOf(' ')+1);
		this.map = new int[width][height];
		
		int currentIndex = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				int endIndex = message.indexOf(' ', currentIndex);
				String data;
				if (endIndex != -1) {
					data = message.substring(currentIndex, endIndex);
				} else { // if reach the end
					data = message.substring(currentIndex, message.length());
				}
				map[i][j] = Integer.parseInt(data);
				currentIndex = endIndex + 1;
			}
		}
		
		// FOR DEBUG PURPOSES
		System.out.println("MAP");
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j]+" ");
			}
			System.out.println();
		}
	}

	/**
	 * parseSetup
	 * This method will parse a setup message.
	 * @param message
	 */
	private void parseSetup(String message) {
		// parse number of players
		int numPlayers = Integer.parseInt(message.substring(0, message.indexOf(' ')));
		message = message.substring(message.indexOf(' ')+1);

		// parse data for players
		this.players = new ArrayList<Player>();
		int currentIndex = 0;
		int i = 0;
		while (i < numPlayers) {
			// player info
			double xPos = -1;
			double yPos = -1;
			Image image = null;

			int j = 0;
			while (j < 3) { // get information
				int endIndex = message.indexOf(' ', currentIndex);
				String data = message.substring(currentIndex, endIndex);
				if (j == 0) { // x
					xPos = Double.parseDouble(data);
				} else if (j == 1) { // y
					yPos = Double.parseDouble(data);
				} else { // image
					if (data.equals("macready")) {
						image = Image.MacReady;
					} else if (data.equals("ripley")) {
						image = Image.Ripley;
					} else {
						System.out.println("ERROR WITH PLAYER IMAGE NAME");
						System.out.println("DATA: "+data);
						System.exit(0);
					}
				}
				currentIndex = endIndex + 1;
				j++;
			}
			Player player = new Player(xPos, yPos, image);
			players.add(player);
			i++;
		}

		// parse data for alien
		double xPos = -1;
		double yPos = -1;

		int j = 0;
		while (j < 2) {
			int endIndex = message.indexOf(' ', currentIndex);
			String data = message.substring(currentIndex, endIndex);
			if (j == 0) { // x
				xPos = Double.parseDouble(data);
			} else { // y
				yPos = Double.parseDouble(data);
			}
			currentIndex = endIndex + 1;
			j++;
		}
		this.alien = new Alien(xPos, yPos, map);
		
		// parse number of runes
		int endIndex = message.indexOf(' ', currentIndex);
		int numRunes = Integer.parseInt(message.substring(currentIndex, endIndex));
		message = message.substring(message.indexOf(' ')+1);
		
		// parse data for runes
		this.runes = new ArrayList<Rune>();
		i = 0;
		while (i < numRunes) {
			// rune info
			xPos = -1;
			yPos = -1;
			char symbol = 'x';
			
			j = 0;
			while (j < 3) { // get information
				endIndex = message.indexOf(' ', currentIndex);
				String data = message.substring(currentIndex, endIndex);
				if (j == 0) { // x
					xPos = Double.parseDouble(data);
				} else if (j == 1) { // y
					yPos = Double.parseDouble(data);
				} else { // char
					symbol = data.charAt(0);
				}
				currentIndex = endIndex + 1;
				j++;
			}
			Rune rune = new Rune(xPos, yPos, symbol);
			runes.add(rune);
			i++;
		}
		
		// parse password
		this.password = message.substring(currentIndex, message.length());

		// initialize screen
		this.screen = new Screen(map, textures, WIDTH, HEIGHT);
	}
	
	/**
	 * parseGame
	 * This method will parse a game message.
	 * @param message
	 */
	private void parseGame(String message) {
		// parse players
		int currentIndex = 0;
		int i = 0;
		while (i < players.size()) {
			int j = 0;
			while (j < 7) {
				int endIndex = message.indexOf(' ', currentIndex);
				String data = message.substring(currentIndex, endIndex);

				switch (j) {
				case 0:
					players.get(i).setxPos(Double.parseDouble(data));
					break;
				case 1:
					players.get(i).setyPos(Double.parseDouble(data));
					break;
				case 2:
					players.get(i).setxDir(Double.parseDouble(data));
					break;
				case 3:
					players.get(i).setyDir(Double.parseDouble(data));
					break;
				case 4:
					players.get(i).setxPlane(Double.parseDouble(data));
					break;
				case 5:
					players.get(i).setyPlane(Double.parseDouble(data));
					break;
				case 6:
					players.get(i).setHealth(Integer.parseInt(data));
					break;
				}
				j++;
				currentIndex = endIndex + 1;
			}
			i++;
		}
		
		// parse data for alien
		i = 0;
		while (i < 3) {
			int endIndex = message.indexOf(' ', currentIndex);
			String data;
			if (endIndex != -1) {
				data = message.substring(currentIndex, endIndex);
			} else { // if reach the end
				data = message.substring(currentIndex, message.length());
			}
			switch (i) {
			case 0:
				alien.xPos = Double.parseDouble(data);
			case 1:
				alien.yPos = Double.parseDouble(data);
			case 2:
				alien.setActive(Boolean.parseBoolean(data));
			}
			i++;
			currentIndex = endIndex + 1;
		}

	}

	/**
	 * close
	 * This method will close sockets.
	 */
	public void close() {
		try {
			socket.close();
			input.close();
			output.close();
		} catch (IOException e) {
			System.out.println("Failed to close socket.");
		}
	}
}
