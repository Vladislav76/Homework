package GameState;

import Message.Message;

public abstract class GameState {
	public static final int MAIN_MENU = 0;
	public static final int MODE_MENU = 1;
	public static final int CREATION_MENU = 2;
	public static final int GAMEPLAY = 3;
	public static final int EXIT = 4;
	public static final int NUMBER_OF_GAME_STATES = 4;

	protected GameStateManager gsm;

	public abstract void update();
	public abstract void render();
	public abstract void initialize(Message message);
}