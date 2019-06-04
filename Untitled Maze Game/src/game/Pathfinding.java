package game;

import java.util.ArrayList;
import java.util.Collections;

public class Pathfinding {
	private int[][] map;

	private ArrayList<PathSquare> openList;
	private ArrayList<PathSquare> closedList;

	private PathSquare[][] squares;

	private ArrayList<PathSquare> bestPath; // from start square to destination square

	public static void main(String[] args) {
		int[][] map = new int[][] {
			{1,1,1,1,1,1,1,1,1,1},
			{1,0,1,0,0,0,0,0,0,1},
			{1,0,1,1,1,1,1,1,0,1},
			{1,0,1,0,0,0,0,1,0,1},
			{1,0,0,0,1,1,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1}
		};
		Pathfinding pathfinding = new Pathfinding(map);
		for (int i = 0; i < 999; i++) {
			pathfinding.calculateBestPath(1,1,1,8);
			// pathfinding.output(1, 7);
		}
	}

	public Pathfinding(int[][] map) {
		this.map = map;
		this.openList = new ArrayList<PathSquare>();
		this.closedList = new ArrayList<PathSquare>();
	}
	
	public ArrayList<PathSquare> getBestPath() {
		return this.bestPath;
	}

	public void calculateBestPath(int startX, int startY, int endX, int endY) {
		if (map[endX][endY] != 0) {
			System.out.println("WHAT THE FUCK IT'S IN A WALL");
			System.exit(0);
		}
		
		// reset everything
		bestPath = new ArrayList<PathSquare>();
		openList = new ArrayList<PathSquare>();
		closedList = new ArrayList<PathSquare>();
		this.squares = new PathSquare[map.length][map[0].length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				squares[x][y] = new PathSquare(x, y);
			}
		}

		// if already arrived
		if (startX == endX && startY == endY) {
			bestPath.add(squares[startX][startY]);
			return;
		}

		// add the starting square
		openList.add(squares[startX][startY]);
		PathSquare currentSquare;
		while (openList.size() != 0) {
			currentSquare = getLowestScoreSquare();

			// switch to closed list
			closedList.add(currentSquare);
			openList.remove(currentSquare);

			// if closedList contains goal
			if (closedList.contains(squares[endX][endY])) {
				currentSquare = squares[endX][endY];
				bestPath.add(currentSquare);
				while (currentSquare.getParent() != null) {
					currentSquare = currentSquare.getParent();
					bestPath.add(currentSquare);
				}
				Collections.reverse(bestPath);
				return;
			}

			// loop through adjacent squares
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (!(x == 0 && y == 0)) {
						int childX = currentSquare.getX() + x;
						int childY = currentSquare.getY() + y;
						// if valid move and closed list doesn't contain it
						if (isValid(childX, childY) == true) {
							if (closedList.contains(squares[childX][childY]) == false) {
								double g = squares[currentSquare.getX()][currentSquare.getY()].getG() + 1.0;
								double h = calculateHValue(childX, childY, endX, endY);

								// if open list already contains it
								if (openList.contains(squares[childX][childY])) {
									// if it is a better move
									if (g <= squares[childX][childY].getG()) {
										// add child to openlist
										squares[childX][childY].setCost(g, h);
										squares[childX][childY].setParent(squares[currentSquare.getX()][currentSquare.getY()]);
										openList.add(squares[childX][childY]);
									}
								} else {
									// add child to openlist
									squares[childX][childY].setCost(g, h);
									squares[childX][childY].setParent(squares[currentSquare.getX()][currentSquare.getY()]);
									openList.add(squares[childX][childY]);
								}
							}
						}
					}
				}
			}
		}
		System.out.println("No path found.");
	}

//	public void output(int endX, int endY) {
//		int[][] newMap = new int[map.length][map[0].length];
//		copyArray(map, newMap);
//		System.out.println("Best Path:");
//		for (PathSquare square : bestPath) {
//			newMap[square.getX()][square.getY()] = 2;
//		}
//
//		for (int i = 0; i < newMap.length; i++) {
//			for (int j = 0; j < newMap[0].length; j++) {
//				if (i == endX && j == endY) {
//					System.out.print("X ");
//				} else {
//					System.out.print(newMap[i][j]+" ");
//				}
//			}
//			System.out.println();
//		}
//	}

//	private void copyArray(int[][] map, int[][] newMap) {
//		for (int i = 0; i < map.length; i++) {
//			for (int j = 0; j < map[0].length; j++) {
//				newMap[i][j] = map[i][j];
//			}
//		}
//	}

	private boolean isValid(int x, int y) {
		if (x >= 0 && x < map.length) {
			if (y >= 0 && y < map[0].length) {
				if (map[x][y] == 0) {
					return true;
				}
			}
		}
		return false;
	}

	private PathSquare getLowestScoreSquare() {
		if (openList.size() == 1) {
			return openList.get(0);
		} else {
			int minIndex = 0;
			for (int i = 1; i < openList.size(); i++) {
				if (openList.get(i).getF() < openList.get(minIndex).getF()) {
					minIndex = i;
				}
			}
			return openList.get(minIndex);
		}
	}

	private double calculateHValue(int x1, int y1, int x2, int y2) {
		return (double)(Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2))); 
	}
}
