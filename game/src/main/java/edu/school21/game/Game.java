package edu.school21.game;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import edu.school21.game.map.GameMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.school21.game.exceptions.IllegalParametersException;
import edu.school21.game.player.Enemy;
import edu.school21.game.player.Player;

@Parameters(separators = "=")
public class Game {

	public static final int FORWARD = 0;
	public static final int BACKWARD = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int NOWHERE = -1;

	public static final int ENEMY = 0;
	public static final int PLAYER = 1;
	public static final int WALL = 2;
	public static final int PORTAL = 3;
	public static final int EMPTY = 4;

	public static final int EDGE = 5;
	public static final int PATH = 6;

	public static final int USER_MODE = 0;
	public static final int DEV_MODE = 1;

	public static final int X = 0;
	public static final int Y = 1;

	public static boolean exit = false;

	@Parameter(
			names = "--enemiesCount",
			description = "Amount of enemies",
			required = true
	)
	int enemiesCount;
	@Parameter(
			names = "--wallsCount",
			description = "Amount of walls",
			required = true
	)
	int wallsCount;

	@Parameter(
			names = "--size",
			description = "Game map width",
			required = true
	)
	int gameMapWidth;

	@Parameter(
			names = "--profile",
			description = "Profile name",
			required = true
	)
	String profileName;

	public static void main(String[] args) {

		Game game = new Game();
		JCommander jCommander = JCommander.newBuilder()
				.addObject(game)
				.build();
		jCommander.parse(args);

		try {
			if (!GameMap.checkThatUnitAmountIsFine(game.enemiesCount, game.wallsCount, game.gameMapWidth)) {
				System.err.println("Wrong amount of units!");
				throw new IllegalParametersException("Wrong amount of units!");
			}
		} catch (IllegalParametersException ex) {
			System.err.println(ex.getMessage());
			System.exit(-1);
		}

		OutputData outputData = new OutputData();

		GameMap gameMap = new GameMap(game.enemiesCount, game.wallsCount, game.gameMapWidth, game.profileName);

		Player player = null;
		List<Enemy> enemiesList = null;
		while (true) {
			gameMap.generateGameMap();

			enemiesList = new ArrayList<>();

			for (int y = 0; y < gameMap.getGameMap().length; y++) {
				for (int x = 0; x < gameMap.getGameMap().length; x++) {
					if (gameMap.getGameMap()[y][x] == ENEMY) {
						enemiesList.add(new Enemy(gameMap.getGameMap(), x, y, USER_MODE));
					} else if (gameMap.getGameMap()[y][x] == PLAYER) {
						player = new Player(gameMap.getGameMap(), x, y, USER_MODE);

					}
				}
			}

			if (player.isAbleToReachPortal()) {
				break;
			}
		}

		int[][] mapArray = gameMap.getGameMap();

		Scanner scanner = new Scanner(System.in);

		while (true) {
			redrawMap(gameMap.getMode(), gameMap);

			if (!player.isValidMove(mapArray, player.getCurPosX(), player.getCurPosY(), FORWARD) &&
					!player.isValidMove(mapArray, player.getCurPosX(), player.getCurPosY(), BACKWARD) &&
					!player.isValidMove(mapArray, player.getCurPosX(), player.getCurPosY(), LEFT) &&
					!player.isValidMove(mapArray, player.getCurPosX(), player.getCurPosY(), RIGHT)) {
				System.out.println("You lose");
				System.exit(0);
			}
			String s = scanner.nextLine();
			if (s.length() != 1) {
				System.out.println("Wrong input!");
				continue;
			}
			char c = s.charAt(0);
			int dir = 0;
			if (c == 'W') {
				dir = FORWARD;
			}
			else if (c == 'S') {
				dir = BACKWARD;
			}
			else if (c == 'A') {
				dir = LEFT;
			}
			else if (c == 'D') {
				dir = RIGHT;
			} else if (c == '9') {
				System.out.println("You surrendered!");
				System.exit(0);
			} else {
				System.out.println("Wrong command!");
				continue;
			}
			if (player.move(dir) == -1) {
				continue;
			}

			if (exit) {
				redrawMap(gameMap.getMode(), gameMap);
				System.exit(0);
			}

			for (Enemy e: enemiesList) {
				int[][] newMap = Arrays.stream(gameMap.getGameMap()).map(int[]::clone).toArray(int[][]::new);

				e.move();
				if (gameMap.getMode() == DEV_MODE) {
					redrawMap(gameMap.getMode(), gameMap);
					System.out.println("Do you want to confirm enemy move?");
					String newS = scanner.nextLine();
					if (newS.length() != 1) {
						gameMap.setGameMap(newMap);
						System.out.println("Wrong input!");
						continue;
					}
					char newC = newS.charAt(0);
					if (newC != '8') {
						gameMap.setGameMap(newMap);
						System.out.println("Wrong input!");
					}
				}
			}

			redrawMap(gameMap.getMode(), gameMap);

			if (exit) {
				System.out.println("Goodbye!");
				break;
			}
		}
		scanner.close();
	}

	public static void redrawMap(int mode, GameMap gameMap) {
		if (mode == USER_MODE) {
			clearConsole();
		}
		gameMap.printGameMap();
	}

	public static void clearConsole() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
