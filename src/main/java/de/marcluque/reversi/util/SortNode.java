package de.marcluque.reversi.util;

import de.marcluque.reversi.map.Map;

import java.util.Objects;

/*
 * Created with <3 by marcluque, March 2021
 */
public class SortNode {

    private final int x;

    private final int y;

    private int specialTile;

    private double heuristicValue;

    private Map map;

    public SortNode(Move move) {
        x = move.getX();
        y = move.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpecialTile() {
        return specialTile;
    }

    public Move getMove() {
        return new Move(x, y, specialTile);
    }

    public void setSpecialTile(int specialTile) {
        this.specialTile = specialTile;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }
}