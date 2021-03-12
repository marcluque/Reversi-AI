package de.marcluque.reversi.util;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class Coordinate {

    private final int x;

    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }
}