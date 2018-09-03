//package core.main;
//
//import core.gameState.ConsoleGameStateManager;

public class ConsolePanel implements RunnablePanel {
	private GameStateManager gsm;
	private boolean isClosing;

	public ConsolePanel() {
		gsm = new ConsoleGameStateManager(this);
		isClosing = false;
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
