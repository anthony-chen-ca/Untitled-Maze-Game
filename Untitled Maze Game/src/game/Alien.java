package game;

import java.util.ArrayList;

public class Alien extends Entity {
	private static final long serialVersionUID = 1L;
	
	private boolean active = true;
	private int damage;

	private boolean needNewPath = false;
	private int huntX = -1;
	private int huntY = -1;

	private Pathfinding algorithm;

	public Alien(double x, double y, int[][] map) {
		super(x, y, 1, 1, 0.66, -0.66, new Sprite(x, y, Image.Alien), 0.03);
		this.damage = 1;
		this.algorithm = new Pathfinding(map);
	}

//	public Alien(double x, double y, double xd, double yd, double xp, double yp, int[][] map) {
//		super(x, y, xd, yd, xp, yp, new Sprite(x, y, Image.Alien), 0.03);
//		this.damage = 1;
//		this.algorithm = new Pathfinding(map);
//	}

	public void move(int[][] map, ArrayList<Player> players) {
		// TODO Auto-generated method stub
		if (active) {
			chasePlayer(map, players);
		} else {
			rest();
		}
	}

	public void rest() {
		// TODO
	}

	public void chasePlayer(int[][] map, ArrayList<Player> players) {
		// find closest player
		// calculate best path to player
		// perform the move
		getTarget(players);
		if (needNewPath == true) {
			algorithm.calculateBestPath((int)(xPos), (int)(yPos), huntX, huntY);
			needNewPath = false;
		}
		// if already arrived
		if (caughtPlayers(players)) {
			attackPlayers(players);
		} else {
			ArrayList<PathSquare> bestPath = algorithm.getBestPath();
			PathSquare move = getNextMove(bestPath);
			if (move.getX() + 0.5 < xPos) {
				// move left
				if (map[(int)(xPos - MOVE_SPEED)][(int)yPos] == 0) {
					setxPos(xPos - MOVE_SPEED);
				}
			} else if (move.getX() + 0.5 > xPos) {
				// move right
				if (map[(int)(xPos + MOVE_SPEED)][(int)yPos] == 0) {
					setxPos(xPos + MOVE_SPEED);
				}
			}
			if (move.getY() + 0.5 < yPos) {
				// move up
				if (map[(int)xPos][(int)(yPos - MOVE_SPEED)] == 0) {
					setyPos(yPos - MOVE_SPEED);
				}
			} else if (move.getY() + 0.5 > yPos) {
				// move down
				if (map[(int)xPos][(int)(yPos + MOVE_SPEED)] == 0) {
					setyPos(yPos + MOVE_SPEED);
				}
			}
		}
	}

	public PathSquare getNextMove(ArrayList<PathSquare> bestPath) {
		if (bestPath.size() == 1) { // if arrived
			return bestPath.get(0);
		}
		// loop through best path
		for (int i = 0; i < bestPath.size(); i++) {
			PathSquare square = bestPath.get(i);
			// get the current square of alien
			if (square.getX() == (int)xPos) {
				if (square.getY() == (int)yPos) {
					// return the next move
					if (i+1 < bestPath.size()) {
						return bestPath.get(i+1);
					}
				}
			}
		}
		return bestPath.get(1);
	}

	public void attackPlayers(ArrayList<Player> players) {
		for (Player player : players) {
			if ((int)this.xPos == (int)player.xPos && (int)this.yPos == (int)player.yPos) {
				player.setHealth(player.getHealth() - this.damage);
				if (player.getHealth() == 0) {
					System.out.println("F FOR RESPECTS");
					System.exit(0);
				}
			}
		}
		// TODO
		// if player is dead
		// active = false;
	}

	private boolean caughtPlayers(ArrayList<Player> players) {
		for (Player player : players) {
			if ((int)(player.xPos) == (int)(xPos)) {
				if ((int)(player.yPos) == (int)(yPos)) {
					return true;
				}
			}
		}
		return false;
	}

	private void getTarget(ArrayList<Player> players) {
		if (players.size() == 1) {
			int newHuntX = (int)players.get(0).xPos;
			int newHuntY = (int)players.get(0).yPos;
			if (!(newHuntX == huntX && newHuntY == huntY)) {
				needNewPath = true;
				this.huntX = newHuntX;
				this.huntY = newHuntY;
			}
		} else {
			double minDist = calculateDistance(this.xPos, this.yPos, players.get(0).xPos, players.get(0).yPos);
			int index = 0;
			for (int i = 1; i < players.size(); i++) {
				double dist = calculateDistance(this.xPos, this.yPos, players.get(i).xPos, players.get(i).yPos);
				if (dist < minDist) {
					minDist = dist;
					index = i;
				}
			}
			int newHuntX = (int)players.get(index).xPos;
			int newHuntY = (int)players.get(index).yPos;
			if (!(newHuntX == huntX && newHuntY == huntY)) {
				needNewPath = true;
				this.huntX = newHuntX;
				this.huntY = newHuntY;
			}
		}
	}

	private double calculateDistance(double x1, double y1, double x2, double y2) {
		return (double)(Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)));
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
