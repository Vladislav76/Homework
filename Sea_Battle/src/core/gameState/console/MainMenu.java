//package core.gameState.console;
//
//import core.gameState.GameState;
//import core.gameState.GameStateManager;
//import core.message.Message;

import java.util.Scanner;

public class MainMenu extends GameState {
	private static final String COMMAND_START = "start";
	private static final String COMMAND_EXIT = "exit";

	public MainMenu(GameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override 
	public void initialize(Message message) {}

	@Override
	public void update() {
		Scanner in = new Scanner(System.in);
		switch (in.next()) {
			case COMMAND_START: 
				gsm.setState(GameState.MODE_MENU);
				break;
			case COMMAND_EXIT:
				gsm.setState(GameState.EXIT);
				break;
		}
	}

	@Override
	public void render() {
		System.out.println("Start game " + "(\"" + COMMAND_START + "\")");
		System.out.println("Exit " + "(\"" + COMMAND_EXIT + "\")");
	}
}
