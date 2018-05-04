package GameState.Console;

import GameState.GameState;
import GameState.GameStateManager;
import MapObject.SeaMap;
import MapObject.Ship;
import Message.Message;
import Message.GameSettingsMessage;
import Message.GameObjectsMessage;
import Player.Player;
import Player.User;
import Player.RandomAI;
import Scanner.ConsoleScanner;

import java.util.Arrays;

public class CreationMenu extends GameState {
	private static final String COMMAND_PLAY = "play";
	private static final String COMMAND_NEXT = "next";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_RANDOM_PLACE_SHIPS = "random";
	private static final String COMMAND_BACK = "back";
	private static final String COMMAND_CLEAR_MAP = "clear";

	private SeaMap[] maps;
	private Player[] players;
	private int currentPlayer;

	private int widthMap;
	private int heightMap;
	private int gameMode;

	private int[][] countOfShips;
	private int[] numOfCreatedShips;
	private int maxOfShips;
	private int maxLengthOfShip;
	private int[] startingSetOfShips;

	private ConsoleScanner in;
	private Message lastMessage;

	public CreationMenu(GameStateManager gsm) {
		this.gsm = gsm;
		in = new ConsoleScanner();
	}

	@Override
	public void initialize(Message message) {
		if (message == null && lastMessage != null) {
			message = lastMessage;
		}
		if (message instanceof GameSettingsMessage) {
			lastMessage = message;
			GameSettingsMessage currentMessage = (GameSettingsMessage) message;
			widthMap = currentMessage.width;
			heightMap = currentMessage.height;
			gameMode = currentMessage.mode;
			startingSetOfShips = currentMessage.countOfShips.clone();
			maxOfShips = Arrays.stream(currentMessage.countOfShips).sum();
			maxLengthOfShip = currentMessage.countOfShips.length - 1;
			countOfShips = new int[][] {currentMessage.countOfShips.clone(), currentMessage.countOfShips.clone()};
			maps = new SeaMap[] {new SeaMap(widthMap, heightMap), new SeaMap(widthMap, heightMap)};
			if (gameMode == ModeMenu.PLAYER_VS_AI_MODE) {
				players = new Player[] {new User(in), new RandomAI()};
			}
			else {
				players = new Player[] {new User(in), new User(in)};
			}
			numOfCreatedShips = new int[] {0, 0};
			currentPlayer = 0;
		}
		else {
			System.out.println("Bad message!");
			System.exit(-1);
		}
	}

	@Override
	public void update() {
		switch (in.getNewString()) {
			case COMMAND_BACK:
				gsm.setState(GameState.MAIN_MENU);
				break;

			case COMMAND_CLEAR_MAP:
				maps[currentPlayer].clear();
				numOfCreatedShips[currentPlayer] = 0;
				countOfShips[currentPlayer] = Arrays.copyOf(startingSetOfShips, startingSetOfShips.length);
				break;

			case COMMAND_RANDOM_PLACE_SHIPS:
				maps[currentPlayer].clear();
				maps[currentPlayer].placeShips(SeaMap.RANDOM_PLACEMENT_OF_SHIPS, startingSetOfShips);
				numOfCreatedShips[currentPlayer] = maxOfShips;
				Arrays.fill(countOfShips[currentPlayer], 0);
				break;

			case COMMAND_NEXT:
				if (gameMode == ModeMenu.PLAYER_VS_PLAYER_MODE && currentPlayer == 0 
					&& numOfCreatedShips[currentPlayer] == maxOfShips) {
					currentPlayer++;
				}
				break;

			case COMMAND_PLAY:
				boolean doLaunchTheGame = false;
				if (gameMode == ModeMenu.PLAYER_VS_PLAYER_MODE) {
					if (currentPlayer == 1 && numOfCreatedShips[currentPlayer] == maxOfShips) {
						doLaunchTheGame = true;
					}
				}
				else if (gameMode == ModeMenu.PLAYER_VS_AI_MODE) {
					if (numOfCreatedShips[currentPlayer] == maxOfShips) {
						currentPlayer++;
						maps[currentPlayer].placeShips(SeaMap.RANDOM_PLACEMENT_OF_SHIPS, countOfShips[currentPlayer]);
						doLaunchTheGame = true;
					}
				}
				if (doLaunchTheGame) {
					gsm.setState(GameState.GAMEPLAY);
					gsm.sendMessage(new GameObjectsMessage(this, maps, players, gameMode, maxOfShips, in), GAMEPLAY);
				}
				break;

			case COMMAND_ADD:
				Ship ship = in.getShip(0, 0, widthMap, heightMap, maxLengthOfShip);
				int length = ship.getLength();
				int x = ship.getX();
				int y = ship.getY();
				int disposition = ship.getDisposition();

				if (ship != null) {
					if (countOfShips[currentPlayer][length] > 0 && maps[currentPlayer].isCanAddShip(length, x, y, disposition)) {
						maps[currentPlayer].addShip(length, x, y, disposition);
						countOfShips[currentPlayer][length]--;
						numOfCreatedShips[currentPlayer]++;
					}
					else {
						System.out.println("You don't add the ship");
					}
				}
				else {
					System.out.println("Input data are incorrect");	
				}	
				break;
		}
	}

	@Override
	public void render() {
		if (gameMode == ModeMenu.PLAYER_VS_PLAYER_MODE && currentPlayer == 0) {
			System.out.println("Next " + "(\"" + COMMAND_NEXT + "\")");
		}
		else {
			System.out.println("Play " + "(\"" + COMMAND_PLAY + "\")");
		}
		System.out.println("Main menu" + "(\"" + COMMAND_BACK + "\")\n");
		System.out.println("Random placement" + "(\"" + COMMAND_RANDOM_PLACE_SHIPS + "\")");
		System.out.println("Clear map" + "(\"" + COMMAND_CLEAR_MAP + "\")\n");
		System.out.println("Add a ship" + "(\"" + COMMAND_ADD + "\")");
		System.out.println("Input format: length(1:" + maxLengthOfShip + 
			") xLeftTopCorner yLeftTopCorner vector(\"h\" or \"v\")\n");
		showCurrentCreatedMap();
		System.out.println("\nThere is left to add ships:");
		for(int i = 1; i <= maxLengthOfShip; i++) {
			if (startingSetOfShips[i] > 0) {
				System.out.print(i + "-deck " + countOfShips[currentPlayer][i]);
				if (i < maxLengthOfShip) {
					System.out.print(" || ");
				}
			}
		}
		System.out.println();
	}

	private void showCurrentCreatedMap() {
		System.out.print("  ");
		for(int i = 0; i < widthMap; i++) {
			if (i <= 9) {
				System.out.printf("%2d", i);
			}
			else {
				int c = (int) 'A' + (i - 10);
				System.out.print(" " + (char) c);
			}
		}		
		System.out.println();

		for(int i = 0; i < heightMap; i++) {
			if (i <= 9) {
				System.out.printf("%2d", i);
			}
			else {
				int c = (int) 'A' + (i - 10);
				System.out.print(" " + (char) c);
			}
			for(int j = 0; j < widthMap; j++) {
				if (maps[currentPlayer].isBusyZone(j, i)) {
					System.out.print(" *");
				}
				else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}	
}