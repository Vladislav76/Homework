package Player;

import MapObject.Coordinate;

import java.util.Random;
import java.util.Arrays;

public abstract class AI implements Player {
	public static final int HUNTING_ACTION = 1;
	public static final int AIMING_ACTION = 2;
	public static final int FIRING_IN_LINE_ACTION = 3; 

	protected static final int[][] vectors = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

	protected int action;
	protected int xFirstHit;
	protected int yFirstHit;

	protected int xLastShot;
	protected int yLastShot;

	protected boolean[] checkedAdjacentZones = new boolean[] {false, false, false, false};
	protected int[] offsetFromFirstHitAlongTheLineOnDecreasing = new int[] {0, 0};
	protected int[] offsetFromFirstHitAlongTheLineOnIncreasing = new int[] {0, 0};

	@Override
	public void notifyMiss() {
		switch (action) {
			case FIRING_IN_LINE_ACTION:
				if (xFirstHit < xLastShot || yFirstHit < yLastShot) {
					Arrays.fill(offsetFromFirstHitAlongTheLineOnIncreasing, 0);
				}
				else {
					Arrays.fill(offsetFromFirstHitAlongTheLineOnDecreasing, 0);
				}
		}
	}

	@Override
	public void notifyHit() {
		switch (action) {
			case HUNTING_ACTION:
				xFirstHit = xLastShot;
				yFirstHit = yLastShot;
				action = AIMING_ACTION;
				break;
			case AIMING_ACTION:
				if (xFirstHit < xLastShot) {
					offsetFromFirstHitAlongTheLineOnDecreasing[0] = -1;
					offsetFromFirstHitAlongTheLineOnIncreasing[0] = 2;
				}
				else if (xLastShot < xFirstHit) {
					offsetFromFirstHitAlongTheLineOnDecreasing[0] = -2;
					offsetFromFirstHitAlongTheLineOnIncreasing[0] = 1;
				}

				if (yFirstHit < yLastShot) {
					offsetFromFirstHitAlongTheLineOnDecreasing[1] = -1;
					offsetFromFirstHitAlongTheLineOnIncreasing[1] = 2;
				}
				else if (yLastShot < yFirstHit) {
					offsetFromFirstHitAlongTheLineOnDecreasing[1] = -2;
					offsetFromFirstHitAlongTheLineOnIncreasing[1] = 1;
				}
				action = FIRING_IN_LINE_ACTION;
				break;
			case FIRING_IN_LINE_ACTION:
				if (xFirstHit < xLastShot || yFirstHit < yLastShot) {
					offsetFromFirstHitAlongTheLineOnIncreasing[0] +=
						Integer.signum(offsetFromFirstHitAlongTheLineOnIncreasing[0]);
					offsetFromFirstHitAlongTheLineOnIncreasing[1] +=
						Integer.signum(offsetFromFirstHitAlongTheLineOnIncreasing[1]);		
				}
				else {
					offsetFromFirstHitAlongTheLineOnDecreasing[0] +=
						Integer.signum(offsetFromFirstHitAlongTheLineOnDecreasing[0]);
					offsetFromFirstHitAlongTheLineOnDecreasing[1] +=
						Integer.signum(offsetFromFirstHitAlongTheLineOnDecreasing[1]);
				}
				break;
		}
	}

	@Override
	public void notifyDestruction() {
		action = HUNTING_ACTION;
		Arrays.fill(offsetFromFirstHitAlongTheLineOnDecreasing, 0);
		Arrays.fill(offsetFromFirstHitAlongTheLineOnIncreasing, 0);
		Arrays.fill(checkedAdjacentZones, false);
	}

	protected Coordinate actToDestroyAttackedObject(int xBound, int yBound) {
		Random random = new Random();
		int xShot = 0;
		int yShot = 0;
		int k;
		switch (action) {
			case AIMING_ACTION:
				int n = 0;
				k = random.nextInt(4);
				while (n < 4) {
					if (!checkedAdjacentZones[k]) {
						checkedAdjacentZones[k] = true;
						if (xFirstHit + vectors[k][0] >= 0 && xFirstHit + vectors[k][0] < xBound 
							&& yFirstHit + vectors[k][1] >= 0 && yFirstHit + vectors[k][1] < yBound) {
							break;
						}
					}
					k = (k + 1) % 4;
					n++;
				}
				if (n < 4) {
					xShot = xFirstHit + vectors[k][0];
					yShot = yFirstHit + vectors[k][1];
				}
				break;
			case FIRING_IN_LINE_ACTION:
				k = random.nextInt(2);
				int dx = 0;
				int dy = 0;
				for(int i = 1; i <= 2; i++) {
					switch(k) {
						case 0:
							dx = offsetFromFirstHitAlongTheLineOnDecreasing[0];
							dy = offsetFromFirstHitAlongTheLineOnDecreasing[1];
							break;
						case 1:
							dx = offsetFromFirstHitAlongTheLineOnIncreasing[0];
							dy = offsetFromFirstHitAlongTheLineOnIncreasing[1];
					}
					if (dx == 0 && dy == 0 || dx + xFirstHit < 0 || dx + xFirstHit >= xBound
						|| dy + yFirstHit < 0 || dy + yFirstHit >= yBound) {
						k = 1 - k;
					}
					else {
						break;
					}
				}
				xShot = xFirstHit + dx;
				yShot = yFirstHit + dy;
				break;
		}
		xLastShot = xShot;
		yLastShot = yShot;
		return new Coordinate(xShot, yShot);
	}
}