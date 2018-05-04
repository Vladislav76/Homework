package Main;

import GameState.GameStateManager;

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
		hide();
		gsm.render();
		while (!isClosing) {
			gsm.update();
			hide();
			gsm.render();
		}
	}

	private void hide() {
	 	System.out.print("\033[H\033[2J");  
	}
}