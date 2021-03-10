package de.marcluque.reversi.util;

import de.marcluque.reversi.map.Map;

import java.util.Objects;

public class Move {

    private final int x;

    private final int y;

    private final int specialTile;

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
    public int hashCode() {
        return Objects.hash(x, y, specialTile);
    }

    @Override
    public String toString() {
        if (specialTile != 0) {
            if (1 <= specialTile && specialTile <= Map.getNumberOfPlayers()) {
                return String.format("(%d, %d) choice: Player %d", x, y, specialTile);
            } else {
                return String.format("(%d, %d) choice: %s", x, y, specialTile == 20 ? "bomb" : "override");
            }
        }

        return String.format("(%d, %d)", x, y);
    }
}