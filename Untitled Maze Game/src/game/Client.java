package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private boolean running = true;
	
	private String room = "room";
	private String name = "admin";
	private int port = 6666;
	
	public void go() {
		System.out.println("CLIENT SIDE:");
		connect("127.0.0.1", port); // my computer
		play();
	}
	
	public Socket connect(String address, int port) {
		try {
			socket = new Socket(address, port);
			InputStreamReader stream = new InputStreamReader(socket.getInputStream());
			input = new BufferedReader(stream);
			output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Connection to server failed.");
		}
		return socket;
	}
	
	public void play() {
		join();
		while (running) {
			try {
				if (input.ready()) {
					String message = input.readLine();
					message = message.trim();
					System.out.println("SERVER MESSAGE:\n"+message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		close();
	}

	public void join() {
		// TODO
	}
	
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
