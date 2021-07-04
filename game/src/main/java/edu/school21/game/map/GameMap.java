package edu.school21.game.map;

import java.util.concurrent.ThreadLocalRandom;

public class GameMap {
	public static final int EMPTY = 0;
	public static final int PLAYER = 1;
	public static final int WALL = 2;
	public static final int ENEMY = 3;
	public static final int PORTAL = 4;

	private final GameMapData gameMapData;
	private final int[][] gameMap;

	public GameMap(int enemiesCount, int wallsCount, int gameMapWidth) {
		gameMapData = new GameMapData(enemiesCount, wallsCount, gameMapWidth);
		gameMap = new int[gameMapWidth][gameMapWidth];
	}


	public void generateGameMap() {
		// get int[] of map
		int[] linedGameMap = new int[gameMapData.gameMapWidth * gameMapData.gameMapWidth];

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
			} while (linedGameMap[currCell] != 0);

			linedGameMap[currCell] = unitType;
		}

	}

	public void printGameMap() {

		for (int x = 0; x < gameMapData.getGameMapWidth(); x++) {

			for (int y = 0; y < gameMapData.getGameMapWidth(); y++) {
				System.out.print(gameMap[y][x]);
			}
			System.out.println("");
		}

	}

	public static boolean checkThatUnitAmountIsFine(int enemiesCount, int wallsCount, int gameMapWidth) {
		return ((enemiesCount + wallsCount + 2) <= (gameMapWidth * gameMapWidth));
	}



	public static class GameMapData {
		final int enemiesCount;
		final int wallsCount;
		final int gameMapWidth;
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
