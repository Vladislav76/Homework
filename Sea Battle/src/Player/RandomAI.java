package Player;

import MapObject.Coordinate;

import java.util.Random;

public class RandomAI extends AI {

	public RandomAI() {
		action = AI.HUNTING_ACTION;
	}

	@Override
	public Coordinate doShot(int xBound, int yBound) {
		int xShot;
		int yShot;
		if (action == HUNTING_ACTION) {
			Random random = new Random();
			xShot = random.nextInt(xBound);
			yShot = random.nextInt(yBound);
			xLastShot = xShot;
			yLastShot = yShot;
			return new Coordinate(xShot, yShot);
		}
		else {
			return actToDestroyAttackedObject(xBound, yBound);
		}
	}
}