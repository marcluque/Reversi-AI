package de.marcluque.reversi.util;

import de.marcluque.reversi.map.Map;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MoveTriplet extends Triplet {

    public MoveTriplet(int x, int y, int specialTile) {
        super(x, y, specialTile);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpecialTile() {
        return z;
    }

    @Override
    public String toString() {
        int specialTile = z;
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