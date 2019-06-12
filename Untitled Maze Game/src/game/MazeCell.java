package game;

import java.util.Random;

/**
 * MazeCell
 * @author Anthony
 * A class for maze cells used in maze generation.
 */
public class MazeCell {
	public MazeCell north, east, south, west;
	public String openLinks = "";
	public boolean visited;

	private Random rnd = new Random();

	/**
	 * addLink
	 * This method will add a link to open links.
	 * @param link
	 */
	public void addLink(String link) {
		openLinks += link;
	}

	/**
	 * remLink
	 * This method will replace a link.
	 * @param link
	 */
	public void remLink(String link) {
		openLinks = openLinks.replace(link, "");
	}

	/**
	 * checkLinksVisited
	 * This method will check if links are visited.
	 * @return
	 */
	public boolean checkLinksVisited() {
		if (!visited) {
			return false;
		}
		if (north != null && !north.visited) {
			return false;
		}
		if (east != null && !east.visited) {
			return false;
		}
		if (south != null && !south.visited) {
			return false;
		}
		if (west != null && !west.visited) {
			return false;
		}
		return true;
	}

	/**
	 * startLinkPath
	 * This method will start a link path.
	 */
	public void startLinkPath() {
		visited = false;
		linkPath();
	}

	/**
	 * linkPath
	 * This method will link a path.
	 */
	public void linkPath() {
		if (visited || openLinks.length() < 1) {
			return;
		} else {
			visited = true;
		}

		int choiceIndex = rnd.nextInt(openLinks.length());
		String choice = "" + openLinks.charAt(choiceIndex);
		remLink(choice);

		switch (choice) {
		case "N":
			north.remLink("S");
			north.linkPath();
			break;
		case "E":
			east.remLink("W");
			east.linkPath();
			break;
		case "S":
			south.remLink("N");
			south.linkPath();
			break;
		case "W":
			west.remLink("E");
			west.linkPath();
			break;
		}
	}
}
