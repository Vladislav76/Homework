//package core.mapObject;

public class Ship {
	private int x;
	private int y;
	private int length;
	private int disposition;

	public Ship(int length, int x, int y, int disposition) {
		this.x = x;
		this.y = y;
		this.length = length;
		this.disposition = disposition;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getLength() {
		return length;
	}
	public int getDisposition() {
		return disposition;
	}
}
