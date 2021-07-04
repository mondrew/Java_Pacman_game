package edu.school21.game.map;

import edu.school21.game.OutputData;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static com.diogonunes.jcolor.Ansi.*;
import static com.diogonunes.jcolor.Attribute.*;

public class GameMap {

	public static final int ENEMY = 0;
	public static final int PLAYER = 1;
	public static final int WALL = 2;
	public static final int PORTAL = 3;
	public static final int EMPTY = 4;

	public static final int UPWARD = 0;
	public static final int DOWNWARD = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	public static final int Y = 0;
	public static final int X = 1;

	private final GameMapData gameMapData;

	private int[][] gameMap;
	private int playerPos;
	private int portalPos;


	public GameMap(int enemiesCount, int wallsCount, int gameMapWidth) {
		gameMapData = new GameMapData(enemiesCount, wallsCount, gameMapWidth);
		gameMap = new int[gameMapWidth][gameMapWidth];
		for (int[] row: gameMap)
			Arrays.fill(row, EMPTY);
	}

	public void movePlayer(int direction) {
		switch (direction) {
			case UPWARD:
				playerPos -= gameMapData.getGameMapWidth();
				break;
			case DOWNWARD:
				playerPos += gameMapData.getGameMapWidth();
				break;
			case LEFT:
				playerPos--;
				break;
			case RIGHT:
				playerPos++;
				break;
		}
	}

	public void moveItem(int[] curPos, int[] newPos) {
		gameMap[newPos[Y]][newPos[X]] = gameMap[curPos[Y]][curPos[X]];
		gameMap[curPos[Y]][curPos[X]] = EMPTY;
	}

	public int[] getUnitPos(int unitType) {
		if (unitType == PLAYER) {
			return convertLinedCoordsToPlaneOnes(playerPos);
		} else if (unitType == PORTAL) {
			return convertLinedCoordsToPlaneOnes(portalPos);
		} else {
			return (new int[]{-1, -1});
		}
	}

	public void generateGameMap() {
		// get int[] of map
		int[] linedGameMap = new int[gameMapData.gameMapWidth * gameMapData.gameMapWidth];
		Arrays.fill(linedGameMap, EMPTY);

		// randomly put walls
		putUnits(linedGameMap, WALL);

		// randomly put enemies
		putUnits(linedGameMap, ENEMY);

		// randomly put portal
		putUnits(linedGameMap, PORTAL);

		// randomly put player
		putUnits(linedGameMap, PLAYER);

		// convert to normal map
		convertLinedMapToUsualMap(linedGameMap);
	}

	private void convertLinedMapToUsualMap(int[] linedGameMap) {
		for (int i = 0; i < linedGameMap.length; i++) {
			gameMap[i / gameMapData.gameMapWidth][i % gameMapData.gameMapWidth] = linedGameMap[i];
		}
	}

	private int[] convertLinedCoordsToPlaneOnes(int currCell) {
		return (new int[]{currCell / gameMapData.gameMapWidth, currCell % gameMapData.gameMapWidth});
	}
	private void putUnits(int[] linedGameMap, int unitType) {
		int unitAmount;

		switch (unitType) {
			case PLAYER:
			case PORTAL:
				unitAmount = 1;
				break;
			case WALL:
				unitAmount = gameMapData.getWallsCount();
				break;
			case ENEMY:
				unitAmount = gameMapData.getEnemiesCount();
				break;
			default:
				unitAmount = 0;
		}

		for (int i = 0; i < unitAmount; i++) {

			int currCell;
			do {
				currCell = ThreadLocalRandom.current().nextInt(0, linedGameMap.length);
			} while (linedGameMap[currCell] != EMPTY);

			linedGameMap[currCell] = unitType;

			if (unitType == PLAYER) {
				playerPos = currCell;
			} else if (unitType == PORTAL) {
				portalPos = currCell;
			}
		}

	}

	public void printGameMap() {
		for (int y = 0; y < gameMapData.getGameMapWidth(); y++) {
			for (int x = 0; x < gameMapData.getGameMapWidth(); x++) {
				// Get and print unit char (color taken into account)
				chooseColorAndPrintUnit(getUnitColor(gameMap[y][x]), String.valueOf(getUnitChar(gameMap[y][x])));
			}
			System.out.println("");
		}
	}

	private static void chooseColorAndPrintUnit(String color, String currUnitAsString) {
		switch (color) {
			case "RED":
				System.out.print(colorize(currUnitAsString, NONE(), RED_BACK()));
				break;
			case "GREEN":
				System.out.print(colorize(currUnitAsString, NONE(), GREEN_BACK()));
				break;
			case "YELLOW":
				System.out.print(colorize(currUnitAsString, NONE(), YELLOW_BACK()));
				break;
			case "BLUE":
				System.out.print(colorize(currUnitAsString, NONE(), BLUE_BACK()));
				break;
			case "MAGENTA":
				System.out.print(colorize(currUnitAsString, NONE(), MAGENTA_BACK()));
				break;
			case "CYAN":
				System.out.print(colorize(currUnitAsString, NONE(), CYAN_BACK()));
				break;
			case "WHITE":
				System.out.print(colorize(currUnitAsString, NONE(), WHITE_BACK()));
				break;
			default:
				System.out.print(colorize(currUnitAsString, NONE(), BLACK_BACK()));
		}
	}

	private String getUnitColor(int unitType) {
		switch (unitType) {
			case ENEMY:
				return OutputData.getEnemyColor();
			case PLAYER:
				return OutputData.getPlayerColor();
			case WALL:
				return OutputData.getWallColor();
			case PORTAL:
				return OutputData.getPortalColor();
			case EMPTY:
				return OutputData.getEmptyColor();
			default:
				return "";
		}
	}

	private char getUnitChar(int unitType) {
		switch (unitType) {
			case ENEMY:
				return OutputData.getEnemyChar();
			case PLAYER:
				return OutputData.getPlayerChar();
			case WALL:
				return OutputData.getWallChar();
			case PORTAL:
				return OutputData.getPortalChar();
			case EMPTY:
				return OutputData.getEmptyChar();
			default:
				return ' ';
		}
	}

	public static boolean checkThatUnitAmountIsFine(int enemiesCount, int wallsCount, int gameMapWidth) {
		return ((enemiesCount + wallsCount + 2) <= (gameMapWidth * gameMapWidth));
	}

	private static class GameMapData {
		private final int enemiesCount;
		private final int wallsCount;
		private final int gameMapWidth;
//		final String profileName;

		GameMapData(int enemiesCount, int wallsCount, int gameMapWidth) {
			this.enemiesCount = enemiesCount;
			this.wallsCount = wallsCount;
			this.gameMapWidth = gameMapWidth;
		}

		public int getEnemiesCount() {
			return enemiesCount;
		}

		public int getWallsCount() {
			return wallsCount;
		}

		public int getGameMapWidth() {
			return gameMapWidth;
		}
	}

}
