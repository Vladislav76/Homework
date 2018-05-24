//package core.gameState.console;
//
//import core.gameState.GameState;
//import core.gameState.ConsoleGameStateManager;
//import core.main.GameManager;
//import core.mapObject.SeaMap;
//import core.message.Message;
//import core.message.GameObjectsMessage;
//import core.scanner.ConsoleScanner;

public class Gameplay extends GameState {
	private static final String COMMAND_BACK = "back";

	private GameManager gameManager;
	private SeaMap[] maps;
	private int gameMode;
	private ConsoleScanner in;

	public Gameplay(ConsoleGameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override 
	public void initialize(Message message) {
		if (message instanceof GameObjectsMessage) {
			GameObjectsMessage currentMessage = (GameObjectsMessage) message;
			gameManager = new GameManager(currentMessage.maps, currentMessage.players, currentMessage.maxOfShips);
			maps = currentMessage.maps;
			gameMode = currentMessage.gameMode;
			in = currentMessage.in;
		}
		else {
			System.out.println("Bad Message");
			System.exit(-1);
		}
	}

	@Override
	public void update() {
		if (!gameManager.isGameOver()) {
			if (gameManager.getCurrentPlayer() != 1 || gameMode != ModeMenu.PLAYER_VS_AI_MODE) {
				String string = in.getNewString();
				if (string.equals(COMMAND_BACK)) {
					gsm.setState(GameState.MAIN_MENU);
				}
				else if (gameMode == ModeMenu.PLAYER_VS_PLAYER_MODE && gameManager.wasTransitionToAnotherPlayer()) {
					gameManager.goToCurrentPlayer();
				}
				else {
					in.rememberString(string);
					gameManager.step();	
				}
			}
			else {
				gameManager.step();
			}
		}
		else {
			in.getNewString();
			gsm.setState(GameState.CREATION_MENU);
			gsm.sendMessage(null, GameState.CREATION_MENU);
		}
	}

	@Override
	public void render() {
		int currentPlayer = gameManager.getLastPlayerThatMadeStep();
		int width = maps[currentPlayer].getWidth();
		int height = maps[currentPlayer].getHeight();
		for(int k = 0; k < 2; k++) {
			System.out.print("  ");
			for(int i = 0; i < width; i++) {
				System.out.print(Integer.toString(i, i + 1).toUpperCase() + " ");
			}
			System.out.print("  ");
		}
		System.out.println();

		for(int i = 0; i < height; i++) {
			for(int k = 0; k <= 1; k++) {
				System.out.print(Integer.toString(i, i + 1).toUpperCase());
				for(int j = 0; j < width; j++) {
					if (maps[k].isNotEmptyAfterShotZone(j, i)) {
						System.out.print(" x");
					}
					else if (maps[k].isEmptyAfterShotZone(j, i)) {
						System.out.print(" .");
					}
					else if ((k == currentPlayer && gameMode == ModeMenu.PLAYER_VS_PLAYER_MODE
						|| k != 1 && gameMode == ModeMenu.PLAYER_VS_AI_MODE) && maps[k].isBusyZone(j, i)) {
						System.out.print(" *");
					}
					else {
						System.out.print("  ");
					}
				}
				System.out.print("   ");
			}	
			System.out.println();
		}

		if (!gameManager.isGameOver()) {
			if (gameMode == ModeMenu.PLAYER_VS_PLAYER_MODE) {
				System.out.println("Press any key to move to another player");
			}
		}
		else {
			System.out.println("G A M E   O V E R");
		}
		
	}
}
