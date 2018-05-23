//package core.mapObject;
//
//import core.scanner.ConsoleScanner;

public class User implements Player {
	private ConsoleScanner dataScanner;
	
	public User(ConsoleScanner scanner) {
		this.dataScanner = scanner;
	}

	@Override 
	public Coordinate doShot(int xBound, int yBound) {	
		return dataScanner.getCoordinates(0, 0, xBound, yBound);
	}

	@Override
	public void notifyMiss() {}

	@Override
	public void notifyHit() {}

	@Override
	public void notifyDestruction() {}
}
