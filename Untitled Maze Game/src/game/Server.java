package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Server {
	// server
	private ServerSocket serverSocket;
	private int port = 6666;
	private boolean running = true;
	private boolean gameRunning = false;
	
	private int readyNum = 0;

	// clients
	private ArrayList<ClientHandler> clients;

	// screen
	private int[][] map;

	// game objects
	private ArrayList<Player> players = new ArrayList<Player>();
	private Alien alien;

	public static void main(String[] args) {
		new Server().go();
	}

	public void go() {
		System.out.println("SERVER SIDE:");
		Socket client = null;
		this.clients = new ArrayList<ClientHandler>();

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// accept clients
		while (running) {
			try {
				client = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(client);
				clients.add(clientHandler);
				Thread t = new Thread(clientHandler);
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ClientHandler implements Runnable {
		private Socket client;
		private BufferedReader input;
		private PrintWriter output;
		private boolean running;
		private int clientNum;

		public ClientHandler(Socket client) {
			this.client = client;
			try {
				this.output = new PrintWriter(client.getOutputStream());
				InputStreamReader stream = new InputStreamReader(client.getInputStream());
				this.input = new BufferedReader(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.running = true;
		}

		@Override
		public void run() {
			// send and get messages from client
			while (running) {
				try {
					if (input.ready()) {
						String message = input.readLine();
						message = message.trim();
						System.out.println("CLIENT MESSAGE: "+message);
						getData(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			close();
		}

		public void sendMessage(String type, boolean broadcast) {
			String message = "ERROR";
			if (type.equals("CLIENT")) {
				// CLIENT - Send the client number identifier.
				
				message = "CLIENT ";
				message += Integer.toString(clientNum);
			} else if (type.equals("START")) {
				// START - The server is ready to start the game.
				
				message = "START";
				gameRunning = true;
			} else if (type.equals("MAP")) {
				// MAP - Send the initial map.
				
				map = getMap(1);
				message = "MAP "+(map.length)+" "+(map[0].length)+" ";
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						message += map[i][j]+" ";
					}
				}
			} else if (type.equals("SETUP")) {
				// SETUP - Set up the initial game object positions.
				
				generatePositions();
				message = "SETUP "+players.size()+" ";
				for (Player player : players) {
					message += player.xPos+" "+player.yPos+" "+player.sprite.getImage().getName()+" ";
				}
				message += alien.xPos+" "+alien.yPos+" ";
			} else if (type.equals("GAME")) {
				// GAME - Send the game state.
				
				message = "GAME ";

				for (Player player : players) {
					message += player.getxPos()+" "+player.getyPos()+" "
							+player.getxDir()+" "+player.getyDir()+" "
							+player.getxPlane()+" "+player.getyPlane()+" "
							+player.getHealth()+" ";
				}

				message += alien.getxPos()+" "+alien.getyPos()+" "
						+Boolean.toString(alien.isActive());
			}

			// broadcast to all clients
			if (broadcast == true) {
				for (int i = 0; i < clients.size(); i++) {
					clients.get(i).output.println(message);
					clients.get(i).output.flush();
				}
			} else {
				output.println(message);
				output.flush();
			}
		}

		public void getData(String message) {
			if (message.substring(0,4).equals("JOIN")) {
				// JOIN - A client wants to join the server.

				message = message.substring(5);
				Player player;
				if (message.equals("macready")) {
					player = new Player(-1, -1, Image.MacReady);
				} else if (message.equals("ripley")) {
					player = new Player(-1, -1, Image.Ripley);
				} else {
					player = new Player(-1, -1, Image.MacReady);
				}
				players.add(player);
				this.clientNum = players.size() - 1;
				sendMessage("CLIENT", false);
			} else if (message.equals("READY")) {
				// READY - A client is ready to start the game.

				readyNum++;
				if (readyNum == players.size()) {
					sendMessage("MAP", true);
					sendMessage("SETUP", true);
					sendMessage("START", true);
				}
			} else if (message.substring(0,6).equals("PLAYER")) {
				// PLAYER - Send the input listener statuses.
				
				message = message.substring(7);
				
				boolean forward = Boolean.parseBoolean(message.substring(0, message.indexOf(' ')));
				message = message.substring(message.indexOf(' ')+1);
				boolean back = Boolean.parseBoolean(message.substring(0, message.indexOf(' ')));
				message = message.substring(message.indexOf(' ')+1);
				boolean left = Boolean.parseBoolean(message.substring(0, message.indexOf(' ')));
				message = message.substring(message.indexOf(' ')+1);
				boolean right = Boolean.parseBoolean(message.substring(0, message.length()));
				
				players.get(clientNum).move(map, forward, back, left, right);
			}
		}

		public void generatePositions() {	
			// get arraylist of empty positions for alien
			ArrayList<int[]> alienEmptyPositions = new ArrayList<int[]>();
			for (int i = 4; i < map.length - 4; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] == 0) {
						int[] pos = {i, j};
						alienEmptyPositions.add(pos);
					}
				}
			}
			
			// set up alien position
			alien = new Alien(-1, -1, map);
			int randomNum = randomNum(0, alienEmptyPositions.size()-1);
			int[] position = alienEmptyPositions.get(randomNum);
			alien.setxPos(position[0] + 0.5);
			alien.setyPos(position[1] + 0.5);

			// get arraylist of empty positions for player
			ArrayList<int[]> playerEmptyPositions = new ArrayList<int[]>();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] == 0) {
						int[] pos = {i, j};
						playerEmptyPositions.add(pos);
					}
				}
			}
			
			// set up player positions
			for (Player player : players) {
				randomNum = randomNum(0, playerEmptyPositions.size()-1);
				position = playerEmptyPositions.get(randomNum);
				player.setxPos(position[0] + 0.5);
				player.setyPos(position[1] + 0.5);
				playerEmptyPositions.remove(randomNum);
			}
		}

		public int randomNum(int min, int max) {
			return ThreadLocalRandom.current().nextInt(min, max + 1);
		}

		public void close() {
			try {
				client.close();
				input.close();
				output.close();
			} catch (Exception e) {
				System.out.println("Failed to close socket.");
			}
		}
	}

	public int[][] getMap(int num) {
		// size * 2 + 1 = actual size
		// actual size is 41
		MazeGenerator mazeGenerator = new MazeGenerator(20);
		int[][] maze = mazeGenerator.getMap();
		
		int[][] map = new int[][] {
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},

			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
		};
		
		System.arraycopy(maze, 1, map, 4, 39);
		
		return map;
	}
}
