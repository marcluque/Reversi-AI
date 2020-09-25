package de.datasecs.reversi.moves;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.Coordinate;
import de.datasecs.reversi.util.MapUtil;
import de.datasecs.reversi.util.Transition;

import java.util.List;

public abstract class Move {

    // x, y, r; means first is the x-coord, then y-coord and then the direction
    protected static final int[][] CORNERS = {{0, -1}, {1, -1}, {1, 0},
            {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

    private Move() {}

    /**
     * Checks whether or not the specified move is valid
     *
     * @param x coordinate of placed stone
     * @param y coordinate of placed stone
     * @param player the character that represents the player doing the move
     * @return returns whether the move at the given coordinates in the given phase by the given player, was valid
     */
    public static boolean isMoveValid(Map map, int x, int y, char player, List<Coordinate> capturableTiles, int phase) {
        return isMoveValidImpl(map, x, y, player, false, true, capturableTiles, phase);
    }

    public static boolean isMoveValidImpl(Map map, int x, int y, char player, boolean returnEarly,
                                          boolean allowOverrideStones, List<Coordinate> capturableTiles, int phase) {
        // Holes are not allowed, neither for building nor for bomb phase
        if (MapUtil.isTileHole(map.getGameField()[y][x])) {
            return false;
        }

        // Building phase
        if (phase == 1) {
            // Tile may be occupied by player or expansion stone -> override stones must be allowed and available for player
            if (MapUtil.isOccupied(map.getGameField()[y][x])
                    && (!allowOverrideStones || map.getOverrideStones()[Character.getNumericValue(player)] == 0)) {
                return false;
            }

            // Used for allowing an override action without actually enclosing a path on an expansion stone
            boolean result = MapUtil.isTileExpansion(map.getGameField()[y][x]);

            // Iterate over all directions from start stone
            for (int direction = 0; direction < 8; direction++) {
                // Walk along direction starting from (x,y)
                result |= walkPath(map, x, y, direction, player, capturableTiles);

                if (returnEarly & result) {
                    return true;
                }
            }

            return result;
        } else {
            // Elimination phase
            // Check basic invalidity for elimination phase
            return map.getBombs()[Character.getNumericValue(player)] > 0;
        }
    }

    private static boolean walkPath(Map map, int startX, int startY, int direction, char player,
                                    List<Coordinate> capturableTiles) {
        int x = startX;
        int y = startY;
        // Starts at -1 because the do while immediately adds the start tile, but the start tile doesn't count for a path
        int pathLength = -1;
        Transition transitionEnd;

        do {
            capturableTiles.add(new Coordinate(x, y));
            transitionEnd = Map.getTransitions().get(new Transition(x, y, direction));

            // Follow the transition, if there is one and adapt its direction
            if (transitionEnd != null) {
                // Jump to stone the transitions ends on
                x = transitionEnd.getX();
                y = transitionEnd.getY();
                direction = (transitionEnd.getDirection() + 4) % 8;
            } else {
                // Move in the specified direction, while the next stone still is another player's stone
                x += CORNERS[direction][0];
                y += CORNERS[direction][1];
            }

            pathLength++;
        } while ((MapUtil.isCoordinateInMap(x, y) && MapUtil.isCapturableStone(map, x, y, player)));

        // Check whether the last tile of the path is in the map, not the start tile and has the player stone on it
        return MapUtil.isCoordinateInMap(x, y)
                && pathLength > 0
                && map.getGameField()[y][x] == player
                && (startX != x || startY != y);
    }

    public static void executeMove(Map map, int x, int y, char player, List<Coordinate> capturableStones, int phase) {
        if (phase == 1) {
            BuildMove.executeBuildMove(map, x, y, player, capturableStones);
        } else {
            BombMove.executeBombMove(map, x, y);
        }
    }
}