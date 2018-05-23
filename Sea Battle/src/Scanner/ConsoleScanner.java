package Scanner;

import MapObject.Coordinate;
import MapObject.SeaMap;
import MapObject.Ship;

import java.util.Scanner;

public class ConsoleScanner {
	private Scanner in;
	private String lastString;

	public ConsoleScanner() {
		in = new Scanner(System.in);
	}

	public String getNewString() {
		return in.nextLine();
	}

	public void rememberString(String string) {
		lastString = string;
	}

	public Coordinate getCoordinates(int leftBoundX, int leftBoundY, int rightBoundX, int rightBoundY) {
			if (lastString == null) {
				lastString = getNewString();
			}
			String[] data = lastString.split(" ");
			lastString = null;
			if (data.length != 2) {
				return null;
			}
			int x = 0;
			int y = 0;
			int d = (int) 'a' - 10;
			if (data[0].matches("[a-z]+")) {
				x = (int) data[0].charAt(0) - d;
			}
			else {
				x = Integer.parseInt(data[0]);
			}

			if (data[1].matches("[a-z]+")) {
				y = (int) data[1].charAt(0) - d;
			}
			else {
				y = Integer.parseInt(data[1]);
			}
			
			if (x < leftBoundX || x >= rightBoundX || y < leftBoundY || y >= rightBoundY) {
				System.out.println("Coordinates are outside the permissible range. Repeat again.");
				return null;
			}
			else {
				return new Coordinate(x, y);
			}
	}

	public Ship getShip(int leftBoundX, int leftBoundY, int rightBoundX, int rightBoundY, int maxLengthOfShip) {
		String string = getNewString();
		String[] data = string.split(" ");
		if (data.length != 4) {
			return null;
		}
		int length = Integer.parseInt(data[0]);
		if (length <= 0 || length > maxLengthOfShip) {
			return null;
		}
	 	int x = 0;
		int y = 0;
		int d = (int) 'a' - 10;
		if (data[1].matches("[a-z]+")) {
			x = (int) data[1].charAt(0) - d;
		}
		else {
			x = Integer.parseInt(data[1]);
		}

		if (data[2].matches("[a-z]+")) {
			y = (int) data[2].charAt(0) - d;
		}
		else {
			y = Integer.parseInt(data[2]);
		}

	 	if (x < leftBoundX || x >= rightBoundX) {
	 		return null;
	 	}
		if (y < leftBoundY || y >= rightBoundY) {
			return null;
		}
		int disposition = 0;
		switch (data[3]) {
			case "h":
				disposition = SeaMap.HORIZONTAL_DISPOSITION_OF_SHIP;
				break;
			case "v":
				disposition = SeaMap.VERTICAL_DISPOSITION_OF_SHIP;
				break;
			default:
				return null;	
		}
		return new Ship(length, x, y, disposition);
	}
}