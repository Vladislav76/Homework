package Player;

import MapObject.Coordinate;

public interface Player {
	Coordinate doShot(int xBound, int yBound);
	abstract void notifyMiss();
	abstract void notifyHit();
	abstract void notifyDestruction();
}