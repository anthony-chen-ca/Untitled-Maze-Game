package game;

public class Main {
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