package MapObject;

import java.util.Random;

public class SeaMap {
	public static final int HORIZONTAL_DISPOSITION_OF_SHIP = 1;
	public static final int VERTICAL_DISPOSITION_OF_SHIP = 2;

	public static final int FREE_ZONE = 1;
	public static final int BUSY_ZONE = 2;
	public static final int EMPTY_AFTER_SHOT_ZONE = 3;
	public static final int NOT_EMPTY_AFTER_SHOT_ZONE = 4;

	public static final int RANDOM_PLACEMENT_OF_SHIPS = 1;

	private int width;
	private int height;
	private int[][] map;

	public SeaMap(int width, int height) {
		this.width = width;
		this.height = height;
		map = new int[height][width];
		clear();
	}

	public void setZone(int x, int y, int state) {
		map[y][x] = state;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public boolean isFreeZone(int x, int y) {
		return map[y][x] == FREE_ZONE;
	}
	
	public boolean isBusyZone(int x, int y) {
		return map[y][x] == BUSY_ZONE;
	}
	
	public boolean isEmptyAfterShotZone(int x, int y) {
		return map[y][x] == EMPTY_AFTER_SHOT_ZONE;
	}
	
	public boolean isNotEmptyAfterShotZone(int x, int y) {
		return map[y][x] == NOT_EMPTY_AFTER_SHOT_ZONE;
	}

	public boolean isNotShotZone(int x, int y) {
		return (map[y][x] == FREE_ZONE || map[y][x] == BUSY_ZONE); 
	}

	public boolean isShipZone(int x, int y) {
		return (map[y][x] == BUSY_ZONE || map[y][x] == NOT_EMPTY_AFTER_SHOT_ZONE);
	}

	public boolean isZone(int x, int y) {
		return !(x < 0 || x >= width || y < 0 || y >= height);
	}

	public void clear() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				map[i][j] = FREE_ZONE;
			}
		}
	}

	public void addShip(int length, int xLeftTopCorner, int yLeftTopCorner, int disposition) {
		int dx = 0;
		int dy = 0;
		if (disposition == HORIZONTAL_DISPOSITION_OF_SHIP) {
			dx = 1;
		}
		else if (disposition == VERTICAL_DISPOSITION_OF_SHIP) {
			dy = 1;
		}
		for (int i = 0; i < length; i++) {
			setZone(xLeftTopCorner + dx * i, yLeftTopCorner + dy * i, BUSY_ZONE);
		}	
	}

	private boolean isInBounds(int x1, int y1, int x2, int y2) {
		return (x1 >= 0 && y1 >= 0 && x2 < width && y2 < height);
	}

	public boolean isCanAddShip(int length, int xLeftTopCorner, int yLeftTopCorner, int disposition) {
		int dx = 0;
		int dy = 0;
		if (disposition == HORIZONTAL_DISPOSITION_OF_SHIP) {
			dx = 1;
		}
		else if (disposition == VERTICAL_DISPOSITION_OF_SHIP) {
			dy = 1;
		}	

		int xRightButtomCorner = xLeftTopCorner + length * dx;
		int yRightButtomCorner = yLeftTopCorner + length * dy;

		//check bounds
		if (!isInBounds(xLeftTopCorner, yLeftTopCorner, xRightButtomCorner, yRightButtomCorner)) {
			return false;
		}

		int xLeftBound = (xLeftTopCorner == 0) ? xLeftTopCorner : xLeftTopCorner - 1;
		int xRightBound = (xRightButtomCorner == width - 1) ? xRightButtomCorner : xRightButtomCorner + 1;
		int yLeftBound = (yLeftTopCorner == 0) ? yLeftTopCorner : yLeftTopCorner - 1;
		int yRightBound = (yRightButtomCorner == height - 1) ? yRightButtomCorner : yRightButtomCorner + 1;	

		//check overlapping
		for(int x = xLeftBound; x <= xRightBound; x++) {
			for(int y = yLeftBound; y <= yRightBound; y++) {
				if (isBusyZone(x, y)) {
					return false;
				}
			}
		}
		return true;
	}

	public void placeShips(int method, int[] countOfShips) {
		switch (method) {
			case RANDOM_PLACEMENT_OF_SHIPS:
				randomPlaceShips(countOfShips);
				break;
		}
	}

	private void randomPlaceShips(int[] countOfShips) {
		Random random = new Random();
		int k = countOfShips.length - 1;
		while (k > 0) {
			int j = 0;
			while (j < countOfShips[k]) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int disposition = random.nextInt(2);
				disposition = (disposition == 0) ? HORIZONTAL_DISPOSITION_OF_SHIP : VERTICAL_DISPOSITION_OF_SHIP;
				if (isCanAddShip(k, x, y, disposition)) {
					addShip(k, x, y, disposition);
					j++;
				}
			}
			k--;
		}
	}
}