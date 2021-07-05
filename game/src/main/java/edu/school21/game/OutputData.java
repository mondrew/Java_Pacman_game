package edu.school21.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OutputData {
	private static final int CHAR_PROPERTY = 0;
	private static final int COLOR_PROPERTY = 1;

	private static final int ENEMY_PROPERTY = 0;
	private static final int PLAYER_PROPERTY = 1;
	private static final int WALL_PROPERTY = 2;
	private static final int PORTAL_PROPERTY = 3;
	private static final int EMPTY_PROPERTY = 4;

	private static char ENEMY_CHAR;
	private static char PLAYER_CHAR;
	private static char WALL_CHAR;
	private static char PORTAL_CHAR;
	private static char EMPTY_CHAR;

	private static String ENEMY_COLOR;
	private static String PLAYER_COLOR;
	private static String WALL_COLOR;
	private static String PORTAL_COLOR;
	private static String EMPTY_COLOR;

	public static char getEnemyChar() {
		return ENEMY_CHAR;
	}

	public static char getPlayerChar() {
		return PLAYER_CHAR;
	}

	public static char getWallChar() {
		return WALL_CHAR;
	}

	public static char getPortalChar() {
		return PORTAL_CHAR;
	}

	public static char getEmptyChar() {
		return EMPTY_CHAR;
	}

	public static String getEnemyColor() {
		return ENEMY_COLOR;
	}

	public static String getPlayerColor() {
		return PLAYER_COLOR;
	}

	public static String getWallColor() {
		return WALL_COLOR;
	}

	public static String getPortalColor() {
		return PORTAL_COLOR;
	}

	public static String getEmptyColor() {
		return EMPTY_COLOR;
	}

	public OutputData() {
		// Retrieving properties
		try (InputStream input = Game.class.getClassLoader().getResourceAsStream("application-production.properties")) {

			Properties propertiesData = new Properties();

//			if (input == null) {
//				System.out.println("Sorry, unable to find application-production.properties");
//				return;
//			}

			//load a properties file from class path, inside static method
			propertiesData.load(input);

			for (int propType = 0; propType < 2; propType++) {
				for (int prop = 0; prop < 5; prop++) {
					setProperty(propType, prop, propertiesData);
				}
			}
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void setProperty(int propertyType, int property, Properties propData) {

		if (propertyType == CHAR_PROPERTY) {
			switch (property) {
				case ENEMY_PROPERTY:
					ENEMY_CHAR = validateAndReturnProperty(propData.getProperty("enemy.char"), propertyType).charAt(0);
				case PLAYER_PROPERTY:
					PLAYER_CHAR = validateAndReturnProperty(propData.getProperty("player.char"), propertyType).charAt(0);
				case WALL_PROPERTY:
					WALL_CHAR = validateAndReturnProperty(propData.getProperty("wall.char"), propertyType).charAt(0);
				case PORTAL_PROPERTY:
					PORTAL_CHAR = validateAndReturnProperty(propData.getProperty("goal.char"), propertyType).charAt(0);
				case EMPTY_PROPERTY:
					EMPTY_CHAR = validateAndReturnProperty(propData.getProperty("empty.char"), propertyType).charAt(0);
			}
		} else {
			switch (property) {
				case ENEMY_PROPERTY:
					ENEMY_COLOR = validateAndReturnProperty(propData.getProperty("enemy.color"), propertyType);
				case PLAYER_PROPERTY:
					PLAYER_COLOR = validateAndReturnProperty(propData.getProperty("player.color"), propertyType);
				case WALL_PROPERTY:
					WALL_COLOR = validateAndReturnProperty(propData.getProperty("wall.color"), propertyType);
				case PORTAL_PROPERTY:
					PORTAL_COLOR = validateAndReturnProperty(propData.getProperty("goal.color"), propertyType);
				case EMPTY_PROPERTY:
					EMPTY_COLOR = validateAndReturnProperty(propData.getProperty("empty.color"), propertyType);
			}
		}
	}

	private String validateAndReturnProperty(String property, int propertyType) {
		if (property.equals("")) {
			if (propertyType == COLOR_PROPERTY) {
				return "BLACK";
			} else {
				return " ";
			}
		}
		else {
			return property;
		}
	}

}