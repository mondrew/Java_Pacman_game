package edu.school21.game;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import edu.school21.game.map.GameMap;

@Parameters(separators = "=")
public class Game {

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

		System.out.println(game.enemiesCount);
		System.out.println(game.wallsCount);
		System.out.println(game.gameMapWidth);
		System.out.println(game.profileName);

		if (!GameMap.checkThatUnitAmountIsFine(game.enemiesCount, game.wallsCount, game.gameMapWidth)) {
			System.err.println("Wrong amount of units!");
			System.exit(-1);
		}


		GameMap gameMap = new GameMap(game.enemiesCount, game.wallsCount, game.gameMapWidth);

		gameMap.generateGameMap();
		gameMap.printGameMap();

	}



}
