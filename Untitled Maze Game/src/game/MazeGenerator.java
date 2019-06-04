package game;

import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator {
	private int size;
	private char[][] map;
	private MazeCell[][] nodeGrid;
	private ArrayList<MazeCell> unlinkedNodes;
	private Random rnd = new Random();

	public MazeGenerator(int size) {
		this.size = size;
		unlinkedNodes = new ArrayList<MazeCell>();
		genNodeGrid();
		genPaths();
		genCharMap();
	}
	
	private void genNodeGrid() {
		nodeGrid = new MazeCell[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				nodeGrid[i][j] = new MazeCell();
			}
		}

		// vertical links
		for (int row = 1; row < size; row++) {
			for (int col = 0; col < size; col++) {
				nodeGrid[row][col].north = nodeGrid[row - 1][col];
				nodeGrid[row][col].addLink("N");
				nodeGrid[row - 1][col].south = nodeGrid[row][col];
				nodeGrid[row - 1][col].addLink("S");
			}
		}

		// horizontal links
		for (int col = 1; col < size; col++) {
			for (int row = 0; row < size; row++) {
				nodeGrid[row][col].west = nodeGrid[row][col - 1];
				nodeGrid[row][col].addLink("W");
				nodeGrid[row][col - 1].east = nodeGrid[row][col];
				nodeGrid[row][col - 1].addLink("E");
			}
		}
	}

	private void genPaths() {
		nodeGrid[0][0].startLinkPath();
		while (!checkVisited()) {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					if (!nodeGrid[row][col].checkLinksVisited()) {
						nodeGrid[row][col].startLinkPath();
					}
				}
			}
		}
	}

	private boolean checkVisited() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (!nodeGrid[row][col].visited) {
					return false;
				}
			}
		}
		return true;
	}

	private void genCharMap() {
		int mapSize = size * 2 + 1;
		map = new char[mapSize][mapSize];

		// central walls
		for (int row = 1; row < mapSize - 1; row++) {
			for (int col = 1; col < mapSize - 1; col++) {
				map[row][col] = '1';
			}
		}

		// paths
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				String links = nodeGrid[row][col].openLinks;
				int mapRow = row * 2 + 1;
				int mapCol = col * 2 + 1;
				map[mapRow][mapCol] = '0';
				if (!links.contains("N")) {
					map[mapRow - 1][mapCol] = '0';
				}
				if (!links.contains("E")) {
					map[mapRow][mapCol + 1] = '0';
				}
				if (!links.contains("S")) {
					map[mapRow + 1][mapCol] = '0';
				}
				if (!links.contains("W")) {
					map[mapRow][mapCol - 1] = '0';
				}
			}
		}

		// borders (overwrites paths)
		for (int row = 0; row < mapSize; row++) {
			map[row][0] = '1';
			map[row][mapSize - 1] = '1';
		}
		for (int col = 0; col < mapSize; col++) {
			map[0][col] = '1';
			map[mapSize - 1][col] = '1';
		}

		// printing
		for (int i = 0; i < mapSize; i++) {
			System.out.print("{");
			for (int j = 0; j < mapSize; j++) {
				if (j < mapSize - 1) {
					System.out.print(map[i][j] + ",");
				} else {
					System.out.print(map[i][j] + "},");
				}
			}
			System.out.println();
		}
	}

	public char[][] getCharMap() {
		return map;
	}

	public static void main(String[] args) {
		new MazeGenerator(5);
	}
}
