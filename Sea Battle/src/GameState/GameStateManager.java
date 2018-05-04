package GameState;

import Main.RunnablePanel;
import GameState.Console.MainMenu;
import GameState.Console.ModeMenu;
import GameState.Console.CreationMenu;
import GameState.Console.Gameplay;
import Message.Message;

public class GameStateManager {
	private RunnablePanel gamePanel;
	private GameState[] states;
	private int currentState;
	private boolean gameIsWorked;

	public GameStateManager(RunnablePanel gamePanel) {
		this.gamePanel = gamePanel;
		states = new GameState[GameState.NUMBER_OF_GAME_STATES];
		initializeStates();
	}

	private void initializeStates() {
		loadState(GameState.MAIN_MENU);
		loadState(GameState.MODE_MENU);
		loadState(GameState.GAMEPLAY);
		loadState(GameState.CREATION_MENU);
		currentState = GameState.MAIN_MENU;
		setState(currentState);
	}

	private void loadState(int state) {
		switch (state) {
			case GameState.MAIN_MENU:
				states[state] = new MainMenu(this);
				break;
			case GameState.MODE_MENU:
				states[state] = new ModeMenu(this);
				break;
			case GameState.CREATION_MENU:
				states[state] = new CreationMenu(this);
				break;
			case GameState.GAMEPLAY:
				states[state] = new Gameplay(this);
				break;
		}
	}

	private void unloadState(int state) {
		states[state] = null;
	}

	public void setState(int state) {
		if (state == GameState.EXIT) {
			gamePanel.setClosing(true);
		}
		else {
			currentState = state;
		}
	}

	public void sendMessage(Message message, int addrState) {
		states[addrState].initialize(message);
	}

	public void update() {
		try {
			states[currentState].update();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void render() {
		try {
			states[currentState].render();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}