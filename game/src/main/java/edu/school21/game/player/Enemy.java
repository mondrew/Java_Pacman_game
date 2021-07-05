package edu.school21.game.player;

import edu.school21.enemylogic.*;

import edu.school21.enemylogic.ChaseLogic;

public class Enemy implements Playable {

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

    public static final int X = 0;
    public static final int Y = 1;

    public static final int USER_MODE = 0;
    public static final int DEV_MODE = 1;

    int[][] mapArray;
    private int curPosX;
    private int curPosY;
    private int mapWidth;
    private int mapHeight;
    int[] newPosition;
    private int mode;

    public Enemy(int[][] mapArray, int curPosX, int curPosY, int mode) {
        this.mapArray = mapArray;
        this.curPosX = curPosX;
        this.curPosY = curPosY;
        this.mapWidth = mapArray[0].length;
        this.mapHeight = mapArray.length;
        newPosition = new int[2];
        this.mode = mode;
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

    public int chooseNextMoveDirection() {
        return ChaseLogic.chooseNextMoveDirection(mapArray, curPosX, curPosY, PLAYER);
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
        return (gameMap[newpY][newpX] == EMPTY || gameMap[newpY][newpX] == PLAYER);
    }

    public void move() {
        int nextMoveDirection = chooseNextMoveDirection();
        int[] curPos = new int[]{curPosX, curPosY};
        int[] newPos = getNewPosition(curPosX, curPosY, nextMoveDirection);
        if (nextMoveDirection != NOWHERE) {
            mapArray[curPosY][curPosX] = EMPTY;
            if (mapArray[newPos[Y]][newPos[X]] == PLAYER) {
                System.out.println("You lose");
                System.exit(0);
            }
            curPosX = newPos[X];
            curPosY = newPos[Y];
            mapArray[newPos[Y]][newPos[X]] = ENEMY;
        }
        else {
            say("I will skip this move");
        }
    }

    public void say(String message) {
        System.out.println("Enemy: " + message);
    }

}
