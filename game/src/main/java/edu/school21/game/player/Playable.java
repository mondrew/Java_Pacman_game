package edu.school21.game.player;

public interface Playable {

    public void say(String message);
    public void findPriorityMovesToItem(int pX, int pY, int item, int[] variants);
}
