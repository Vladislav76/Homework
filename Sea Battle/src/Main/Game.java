package Main;

public class Game {
	public static final int CONSOLE_MODE = 1;

	private Runnable gamePanel;

	public Game(int mode) {
		if (mode == CONSOLE_MODE) {
			initConsoleMode();
		}
	}

	private void initConsoleMode() {
		gamePanel = new ConsolePanel();
	}
}