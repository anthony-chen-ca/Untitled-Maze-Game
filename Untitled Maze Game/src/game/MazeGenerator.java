package game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MazeGenerator {
	private int size;
	private int mapSize;
	private int[][] map;
	private MazeCell[][] nodeGrid;
	private ArrayList<MazeCell> unlinkedNodes;

	public MazeGenerator(int size) {
		this.size = size;
		unlinkedNodes = new ArrayList<MazeCell>();
		genNodeGrid();
		genPaths();
		genMap();
		// output();
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

	private void genMap() {
		this.mapSize = size * 2 + 1;
		map = new int[mapSize][mapSize];

		// central walls
		for (int row = 1; row < mapSize - 1; row++) {
			for (int col = 1; col < mapSize - 1; col++) {
				map[row][col] = 1;
			}
		}

		// paths
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				String links = nodeGrid[row][col].openLinks;
				int mapRow = row * 2 + 1;
				int mapCol = col * 2 + 1;
				map[mapRow][mapCol] = 0;
				if (!links.contains("N")) {
					map[mapRow - 1][mapCol] = 0;
				}
				if (!links.contains("E")) {
					map[mapRow][mapCol + 1] = 0;
				}
				if (!links.contains("S")) {
					map[mapRow + 1][mapCol] = 0;
				}
				if (!links.contains("W")) {
					map[mapRow][mapCol - 1] = 0;
				}
			}
		}

		// borders (overwrites paths)
		for (int row = 0; row < mapSize; row++) {
			map[row][0] = 1;
			map[row][mapSize - 1] = 1;
		}
		ArrayList<int[]> emptyPositions = new ArrayList<int[]>();
		for (int col = 0; col < mapSize; col++) {
			map[0][col] = 1;
			if (map[mapSize - 3][col] == 0) {
				int[] pos = {mapSize - 3, col};
				emptyPositions.add(pos);
			}
			map[mapSize - 2][col] = 1;
			map[mapSize - 1][col] = 1;
		}
		
		// carve out final path
		int randomNum = randomNum(0, emptyPositions.size()-1);
		int[] position = emptyPositions.get(randomNum);
		map[position[0]+1][position[1]] = 0;
	}

	public void output() {
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
	
	public int randomNum(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public int[][] getMap() {
		return map;
	}

	public static void main(String[] args) {
		new MazeGenerator(10);
	}
}
