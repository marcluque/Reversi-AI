package de.datasecs.reversi.util;

import de.datasecs.reversi.map.Map;

import java.util.Objects;

public class Move {

    private int x;

    private int y;

    private int specialTile;

    public Move(int x, int y, int specialTile) {
        this.x = x;
        this.y = y;
        this.specialTile = specialTile;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Move move = (Move) o;
        return x == move.x && y == move.y && specialTile == move.specialTile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, specialTile);
    }

    @Override
    public String toString() {
        if (specialTile != 0) {
            if (1 <= specialTile && specialTile <= Map.getNumberOfPlayers()) {
                return "(" + x + ", " + y + ") choice: Player " + specialTile;
            } else {
                return "(" + x + ", " + y + ") choice: " + (specialTile == 20 ? "bomb" : "override");
            }
        }

        return "(" + x + ", " + y + ")";
    }

    @Override
    public Move clone() {
        return new Move(x, y, specialTile);
    }
}