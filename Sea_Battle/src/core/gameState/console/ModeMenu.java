//package core.gameState.console;
//
//import core.gameState.GameState;
//import core.gameState.ConsoleGameStateManager;
//import core.message.Message;
//import core.message.GameSettingsMessage;

import java.util.Scanner;

public class ModeMenu extends GameState {
	private static final String COMMAND_USER_VS_AI = "1";
	private static final String COMMAND_USER_VS_USER = "2";
	private static final String COMMAND_BACK = "back";

	private static final String COMMAND_SET_SHORT_SIZE = "s";
	private static final String COMMAND_SET_MEDIUM_SIZE = "m";
	private static final String COMMAND_SET_LARGE_SIZE = "l";
	private static final String[] COMMAND_SET_SIZE = {"s", "m", "l"};

	public static final int PLAYER_VS_AI_MODE = 1;
	public static final int PLAYER_VS_PLAYER_MODE = 2;

	public static final int SHORT_SIZE = 0;
	public static final int MEDIUM_SIZE = 1;
	public static final int LARGE_SIZE = 2;

	public static final int[][] COUNT_OF_SHIPS = {{0, 4, 3, 2, 1}, {0, 5, 4, 3, 2, 1}, {0, 20, 0, 0, 0, 0, 0, 0, 1}};
	public static final int[] WIDTH_OF_MAP = {10, 15, 20};
	public static final int[] HEIGHT_OF_MAP = {10, 15, 20};

	private int currentSizeOption = 0;

	public ModeMenu(ConsoleGameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override 
	public void initialize(Message message) {}

	@Override
	public void update() {
		Scanner in = new Scanner(System.in);
		switch (in.next()) {
			case COMMAND_SET_SHORT_SIZE:
				currentSizeOption = SHORT_SIZE;
				break;
			case COMMAND_SET_MEDIUM_SIZE:
				currentSizeOption = MEDIUM_SIZE;
				break;
			case COMMAND_SET_LARGE_SIZE:
				currentSizeOption = LARGE_SIZE;
				break;
			case COMMAND_USER_VS_AI: 
				gsm.setState(GameState.CREATION_MENU); 
				gsm.sendMessage(new GameSettingsMessage(this, WIDTH_OF_MAP[currentSizeOption], 
					HEIGHT_OF_MAP[currentSizeOption], PLAYER_VS_AI_MODE, 
					COUNT_OF_SHIPS[currentSizeOption]), CREATION_MENU);
				break;
			case COMMAND_USER_VS_USER:
				gsm.setState(GameState.CREATION_MENU);
				gsm.sendMessage(new GameSettingsMessage(this, WIDTH_OF_MAP[currentSizeOption],
					HEIGHT_OF_MAP[currentSizeOption], PLAYER_VS_PLAYER_MODE, 
					COUNT_OF_SHIPS[currentSizeOption]), CREATION_MENU);
				break;
			case COMMAND_BACK:
				gsm.setState(GameState.MAIN_MENU);
				break;
		}
	}

	@Override
	public void render() {
		System.out.println("Size of map:");
		for(int i = SHORT_SIZE; i <= LARGE_SIZE; i++) {
			if (currentSizeOption == i) {
				System.out.print("+ ");
			}
			else {
				System.out.print("  ");
			}
			System.out.println(WIDTH_OF_MAP[i] + " x " + HEIGHT_OF_MAP[i] + "(\"" + COMMAND_SET_SIZE[i] + "\")");
		}
		System.out.println();
		System.out.println("Against the computer " + "(\"" + COMMAND_USER_VS_AI + "\")");
		System.out.println("Against the other player " + "(\"" + COMMAND_USER_VS_USER + "\")");
		System.out.println("Main menu" + "(\"" + COMMAND_BACK + "\")");
	}
}
