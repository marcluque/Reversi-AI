package de.marcluque.reversi.moves;

import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.Transition;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public abstract class AbstractMove {

    public static final int[][] CORNERS = {{0, -1}, {1, -1}, {1, 0},
            {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

    private AbstractMove() {}

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly,
                                      List<Coordinate> capturableTiles) {
        return isMoveValid(map, x, y, player, returnEarly, Rules.OVERRIDE_STONES, capturableTiles);
    }

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly,
                                      boolean allowOverrideStones, List<Coordinate> capturableTiles) {
        return isMoveValidImpl(map, x, y, player, returnEarly, allowOverrideStones,
                (direction) -> walkPath(map, x, y, direction, player,
                         (newX, newY) -> {
                    capturableTiles.add(new Coordinate(newX, newY));
                    return Map.getTransitions().get(new Transition(newX, newY, direction));
                }));
    }

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly) {
        return isMoveValid(map, x, y, player, returnEarly, Rules.OVERRIDE_STONES);
    }

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly,
                                      boolean allowOverrideStones) {
        return isMoveValidImpl(map, x, y, player, returnEarly, allowOverrideStones,
                (direction) -> walkPath(map, x, y, direction, player,
                         (newX, newY) -> Map.getTransitions().get(new Transition(newX, newY, direction))));
    }

    private static boolean isMoveValidImpl(Map map, int x, int y, char player, boolean returnEarly,
                                           boolean allowOverrideStones, Function<Integer, Boolean> walkPathVariant) {
        // Holes are not allowed, neither for building nor for bomb phase
        if (MapUtil.isTileHole(map.getGameField()[y][x])) {
            return false;
        }

        // Building phase
        if (Map.getPhase() == 1) {
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
                result |= walkPathVariant.apply(direction);

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
                                    BiFunction<Integer, Integer, Transition> transitionSupplier) {
        int x = startX;
        int y = startY;
        // Starts at -1 because the do while immediately adds the start tile, but the start tile doesn't count for a path
        int pathLength = -1;
        Transition transitionEnd;

        do {
            transitionEnd = transitionSupplier.apply(x, y);

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

    public static Move executeMove(Map map, int x, int y, char player, List<Coordinate> capturableStones) {
        if (Map.getPhase() == 1) {
            return BuildMove.executeBuildMove(map, x, y, player, capturableStones);
        } else {
            return BombMove.executeBombMove(map, x, y);
        }
    }
}