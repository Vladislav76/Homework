//package core.main;
//
//import core.gameState.GameStateManager;

import java.lang.Thread;

public class ConsolePanel implements RunnablePanel {
	private GameStateManager gsm;
	private Thread thread;
	private boolean isClosing;

	public ConsolePanel() {
		isClosing = false;
		gsm = new GameStateManager(this);
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void setClosing(boolean logicalValue) {
		isClosing = logicalValue;
	}

	@Override 
	public void run() {
		while (!isClosing) {
			hide();
			gsm.render();
			gsm.update();
		}
	}

	private void hide() {
	 	System.out.print("\033[H\033[2J");
	}
}
