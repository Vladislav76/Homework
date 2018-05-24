//package core.gameState;
//
//import core.main.RunnablePanel;
//import core.gameState.console.MainMenu;
//import core.gameState.console.ModeMenu;
//import core.gameState.console.CreationMenu;
//import core.gameState.console.Gameplay;
//import core.message.Message;


public class ConsoleGameStateManager implements GameStateManager {
	private RunnablePanel gamePanel;
	private GameState[] states;
	private int currentState;
	private boolean gameIsWorked;

	public ConsoleGameStateManager() {
		states = new GameState[GameState.NUMBER_OF_GAME_STATES];
		initializeStates();
	}

	public ConsoleGameStateManager(RunnablePanel gamePanel) {
		this();
		this.gamePanel = gamePanel;
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
