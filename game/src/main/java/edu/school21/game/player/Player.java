package edu.school21.game.player;

import edu.school21.game.Game;

import java.util.Arrays;

public class Player implements Playable {

    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    public static final int ENEMY = 0;
    public static final int PLAYER = 1;
    public static final int WALL = 2;
    public static final int PORTAL = 3;
    public static final int EMPTY = 4;
    public static final int EDGE = 5;
    public static final int PATH = 6;

    public static final int X = 0;
    public static final int Y = 1;

    public static final int USER_MODE = 0;
    public static final int DEV_MODE = 1;

    int[][] mapArray;
    int[][] mapWithPathToPortal;
    private int curPosX;
    private int curPosY;
    private int mapWidth;
    private int mapHeight;
    int[] portalPosition;
    int[] newPosition;
    int mode;

    public Player(int[][] mapArray, int curPosX, int curPosY, int mode) {
        this.mapArray = mapArray;
        this.curPosX = curPosX;
        this.curPosY = curPosY;
        this.mapWidth = mapArray[0].length;
        this.mapHeight = mapArray.length;
        this.mapWithPathToPortal = Arrays.stream(mapArray).map(int[]::clone).toArray(int[][]::new);
        portalPosition = getItemPosition(PORTAL);
        newPosition = new int[2];
        this.mode = mode;
    }

    public boolean isValidMove(int[][] gameMap, int pX, int pY, int direction) {
        int newpX;
        int newpY;

        if (direction == FORWARD && (pY - 1 >= 0)) {
            newpX = pX;
            newpY = pY - 1;
        }
        else if (direction == BACKWARD && (pY + 1 <= mapHeight - 1)) {
            newpX = pX;
            newpY = pY + 1;
        }
        else if (direction == LEFT && pX - 1 >= 0) {
            newpX = pX - 1;
            newpY = pY;
        }
        else if (direction == RIGHT && pX + 1 <= mapWidth - 1) {
            newpX = pX + 1;
            newpY = pY;
        }
        else {
            return false;
        }
        return (gameMap[newpY][newpX] == EMPTY || gameMap[newpY][newpX] == PORTAL || gameMap[newpY][newpX] == ENEMY);
    }

    public int move(int direction) {
        if (!isValidMove(mapArray, curPosX, curPosY, direction)) {
            System.out.println("Try another direction to move");
            return -1;
        }
        else {
            int[] curPos = getItemPosition(PLAYER);
            if (curPos == null) {
                System.out.println("WWWHAT!?");
            }
            int[] newPos = getNewPosition(curPosX, curPosY, direction);
            mapArray[curPos[Y]][curPos[X]] = EMPTY;
            if (mapArray[newPos[Y]][newPos[X]] == PORTAL) {
                System.out.println("You win");
                System.exit(0);
            }

            if (mapArray[newPos[Y]][newPos[X]] == ENEMY) {
                System.out.println("You lose!");
                mapArray[newPos[Y]][newPos[X]] = ENEMY;
                Game.exit = true;
                return 0;
            }

            mapArray[newPos[Y]][newPos[X]] = PLAYER;
            curPosX = newPos[X];
            curPosY = newPos[Y];
        }
        return 0;
    }
    public void say(String message) {
        System.out.println("Player: " + message);
    }

    public int[] getItemPosition(int item) {
        int[] pos = new int[2];

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (mapArray[y][x] == item) {
                    pos[X] = x;
                    pos[Y] = y;
                    return pos;
                }
            }
        }
        return null;
    }

    public void findPriorityMovesToItem(int pX, int pY, int item, int[] variants) {
        int[] itemPos = getItemPosition(item);

        int diffX = pX - itemPos[X];
        int diffY = pY - itemPos[Y];

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX < 0) {
                variants[0] = RIGHT;
                variants[3] = LEFT;
            }
            else {
                variants[0] = LEFT;
                variants[3] = RIGHT;
            }
            if (diffY < 0) {
                variants[1] = BACKWARD;
                variants[2] = FORWARD;
            }
            else {
                variants[2] = BACKWARD;
                variants[1] = FORWARD;
            }
        }
        else {
            if (diffY < 0) {
                variants[0] = BACKWARD;
                variants[3] = FORWARD;
            }
            else {
                variants[0] = FORWARD;
                variants[3] = BACKWARD;
            }
            if (diffX < 0) {
                variants[1] = RIGHT;
                variants[2] = LEFT;
            }
            else {
                variants[1] = LEFT;
                variants[2] = RIGHT;
            }
        }
    }

    public int getNextMoveItem(int direction, int pX, int pY) {

        if (direction == FORWARD && pY - 1 >= 0) {
            pY -= 1;
        }
        else if (direction == BACKWARD && pY + 1 <= mapHeight - 1) {
            pY += 1;
        }
        else if (direction == LEFT && pX - 1 >= 0) {
            pX -= 1;
        }
        else if (direction == RIGHT && pX + 1 <= mapWidth - 1) {
            pX += 1;
        }
        else
            return EDGE;
        return (mapArray[pY][pX]);
    }

    public int[] getNewPosition(int pX, int pY, int direction) {
        int newpX = -1;
        int newpY = -1;

        if (direction == FORWARD) {
            newpX = pX;
            newpY = pY - 1;
        }
        else if (direction == BACKWARD) {
            newpX = pX;
            newpY = pY + 1;
        }
        else if (direction == LEFT) {
            newpX = pX - 1;
            newpY = pY;
        }
        else if (direction == RIGHT) {
            newpX = pX + 1;
            newpY = pY;
        }
        newPosition[X] = newpX;
        newPosition[Y] = newpY;
        return newPosition;
    }

    public boolean isAbleToReachPortal() {
        int[] variants = new int[4];

        return hasNextMoveToPortal(curPosX, curPosY, variants);
    }

    public boolean hasNextMoveToPortal(int pX, int pY, int[] variants) {
        findPriorityMovesToItem(pX, pY, PORTAL, variants);
        int[] newPos;

        if (mapWithPathToPortal[pY][pX] == PORTAL) {
            return true;
        }
        for (int i = 0; i < 4; i++) {
            if (isValidMove(mapWithPathToPortal, pX, pY, variants[i])) {
                newPos = getNewPosition(pX, pY, variants[i]);
                mapWithPathToPortal[pY][pX] = PATH;
                if (hasNextMoveToPortal(newPos[X], newPos[Y], variants)) {
                    mapWithPathToPortal[newPos[Y]][newPos[X]] = EMPTY;
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public void setMapArray(int[][] mapArray) {
        this.mapArray = mapArray;
    }

    public int[][] getMapWithPathToPortal() {
        return mapWithPathToPortal;
    }

    public void setMapWithPathToPortal(int[][] mapWithPathToPortal) {
        this.mapWithPathToPortal = mapWithPathToPortal;
    }

    public int getCurPosX() {
        return curPosX;
    }

    public void setCurPosX(int curPosX) {
        this.curPosX = curPosX;
    }

    public int getCurPosY() {
        return curPosY;
    }

    public void setCurPosY(int curPosY) {
        this.curPosY = curPosY;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setPortalPosition(int[] portalPosition) {
        this.portalPosition = portalPosition;
    }
}
