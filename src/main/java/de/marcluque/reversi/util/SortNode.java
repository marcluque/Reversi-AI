package de.marcluque.reversi.util;

import java.util.Objects;

public class SortNode {

    private final int x;

    private final int y;

    private double heuristicValue;

    public SortNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
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