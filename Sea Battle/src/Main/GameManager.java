package Main;

import MapObject.SeaMap;
import MapObject.Coordinate;
import Player.AI;
import Player.Player;

import java.lang.Thread;

public class GameManager {
	private int numOfPlayers;
	private int currentPlayer;
	private int successivePlayer;
	private int previousCurrentPlayer;
	private int maxOfShips;
	private SeaMap[] maps;
	private Player[] players;
	private int[] numOfDestroyedShips;
	
	public GameManager(SeaMap[] maps, Player[] players, int maxOfShips) {
		numOfPlayers = players.length;
		numOfDestroyedShips = new int[numOfPlayers];
		this.maps = maps;
		this.players = players;
		this.maxOfShips = maxOfShips;
		currentPlayer = 0;
		previousCurrentPlayer = 0;
		successivePlayer = (currentPlayer + 1) % numOfPlayers;
	}

	public int getLastPlayerThatMadeStep() {
		return previousCurrentPlayer;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean wasTransitionToAnotherPlayer() {
		return currentPlayer != previousCurrentPlayer;
	}

	public void goToCurrentPlayer() {
		previousCurrentPlayer = currentPlayer;
	}

	public boolean isGameOver() {
		return (numOfDestroyedShips[previousCurrentPlayer] == maxOfShips);
	}

	public void step() {
		previousCurrentPlayer = currentPlayer;
		int xBound = maps[currentPlayer].getWidth();
		int yBound = maps[currentPlayer].getHeight();
		Coordinate shot = players[currentPlayer].doShot(xBound, yBound);
		if (shot != null) {
			int xShot = shot.getX();
			int yShot = shot.getY();
			if (maps[successivePlayer].isNotShotZone(xShot, yShot)) {
				if (maps[successivePlayer].isBusyZone(xShot, yShot)) {
					maps[successivePlayer].setZone(xShot, yShot, SeaMap.NOT_EMPTY_AFTER_SHOT_ZONE);
					if (checkAndMessageForDestruction(xShot, yShot, successivePlayer)) {
						players[currentPlayer].notifyDestruction();
						numOfDestroyedShips[currentPlayer]++;
					}
					else {
						players[currentPlayer].notifyHit();
					}
				}
				else {
					players[currentPlayer].notifyMiss();
					maps[successivePlayer].setZone(xShot, yShot, SeaMap.EMPTY_AFTER_SHOT_ZONE);
					currentPlayer = successivePlayer;
					successivePlayer = (successivePlayer + 1) % numOfPlayers;
				}
				if (players[previousCurrentPlayer] instanceof AI) {
					try {
					 	Thread.sleep(1000);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private boolean checkAndMessageForDestruction(int x, int y, int opponent) {
		int dx = 0;
		int dy = 0;
		if (maps[opponent].isZone(x + 1, y) && maps[opponent].isShipZone(x + 1, y) 
			|| maps[opponent].isZone(x - 1, y) && maps[opponent].isShipZone(x - 1, y)) {
			dx = 1;
		}
		else if (maps[opponent].isZone(x, y + 1) && maps[opponent].isShipZone(x, y + 1) 
			|| maps[opponent].isZone(x, y - 1) && maps[opponent].isShipZone(x, y - 1)) {
			dy = 1;
		}

		boolean isDestroyedShip = true;

		int xMin = x;
		int yMin = y;
		int xMax = x;
		int yMax = y;

		if (dx != 0 || dy != 0) {
			for (int k = 0; k <= 1; k++) {
				int i = 1;
				while (isDestroyedShip && maps[opponent].isZone(x + dx * i, y + dy * i)) {
					if (maps[opponent].isBusyZone(x + dx * i, y + dy * i)) {
						isDestroyedShip = false;
						break;
					}
					if (!maps[opponent].isNotEmptyAfterShotZone(x + dx * i, y + dy * i)) {
						break;
					}
					i++;
				}
				if (k == 0) {
					xMax = x + dx * (i - 1);
					yMax = y + dy * (i - 1);
					dx *= -1;
					dy *= -1;
				}
				else {
					xMin = x + dx * (i - 1);
					yMin = y + dy * (i - 1);
				}
			}
		}

		if (isDestroyedShip) {
			int width = maps[opponent].getWidth();
			int height = maps[opponent].getHeight();
			if (xMin > 0) {
				xMin--;
			} 
			if (xMax < width - 1) {
				xMax++;
			}
			if (yMin > 0) {
				yMin--;
			}
			if (yMax < height - 1) {
				yMax++;
			}
			
			for(int tempX = xMin; tempX <= xMax; tempX++) {
				for(int tempY = yMin; tempY <= yMax; tempY++) {
					if (maps[opponent].isFreeZone(tempX, tempY)) {
						maps[opponent].setZone(tempX, tempY, SeaMap.EMPTY_AFTER_SHOT_ZONE);
					}
				}
			}
		}
		return isDestroyedShip;
	}
}