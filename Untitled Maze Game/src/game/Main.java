package game;

/**
 * Main
 * @author Anthony
 * The main class. Run Server first, and then run Main.
 */
public class Main {
	/**
	 * main
	 * The main method.
	 * @param args
	 */
	public static void main(String[] args) {
		MainMenu mainMenu = new MainMenu();

		while (mainMenu.isPlaying() == false) {
			mainMenu.refresh();
		}
		mainMenu.dispose();
		Game game = new Game();
		game.start();
	}
}