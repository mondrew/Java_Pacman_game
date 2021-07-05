package edu.school21.enemylogic;

public class ChaseLogic {

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

    public ChaseLogic() {
    }

    public static void findPriorityMovesToItem(int[][] mapArray, int pX, int pY, int item, int[] variants) {
        int[] itemPos = getItemPosition(mapArray, item);

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

    public static int[] getItemPosition(int[][] mapArray, int item) {
        int[] pos = new int[2];

        for (int y = 0; y < mapArray.length; y++) {
            for (int x = 0; x < mapArray.length; x++) {
                if (mapArray[y][x] == item) {
                    pos[X] = x;
                    pos[Y] = y;
                    return pos;
                }
            }
        }
        return null;
    }

    public static int chooseNextMoveDirection(int[][] mapArray, int pX, int pY, int item) {
        int[] variants = new int[4];

        findPriorityMovesToItem(mapArray, pX, pY, PLAYER, variants);

        for (int i = 0; i < 4; i++) {
            if (isValidMove(mapArray, pX, pY, variants[i])) {
                return variants[i];
            }
        }
        return NOWHERE;
    }

    public static boolean isValidMove(int[][] gameMap, int pX, int pY, int direction) {
        int newpX;
        int newpY;

        if (direction == FORWARD && (pY - 1 >= 0)) {
            newpX = pX;
            newpY = pY - 1;
        }
        else if (direction == BACKWARD && (pY + 1 <= gameMap.length - 1)) {
            newpX = pX;
            newpY = pY + 1;
        }
        else if (direction == LEFT && pX - 1 >= 0) {
            newpX = pX - 1;
            newpY = pY;
        }
        else if (direction == RIGHT && pX + 1 <= gameMap.length - 1) {
            newpX = pX + 1;
            newpY = pY;
        }
        else {
            return false;
        }
        return (gameMap[newpY][newpX] == EMPTY || gameMap[newpY][newpX] == PLAYER);
    }

}
