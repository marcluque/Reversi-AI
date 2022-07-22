package de.marcluque.reversi.ai.moves;

import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;
import de.marcluque.reversi.util.Transition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public abstract class Move {

    public static final int[][] CORNERS = {{0, -1}, {1, -1}, {1, 0},
            {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

    private Move() {}

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly,
                                      List<Coordinate> capturableTiles) {
        return isMoveValid(map, x, y, player, returnEarly, Rules.useOverrideStones, capturableTiles);
    }

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly,
                                      boolean allowOverrideStones, List<Coordinate> capturableTiles) {
        return isMoveValidImpl(map, x, y, player, returnEarly, allowOverrideStones, capturableTiles);
    }

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly) {
        return isMoveValid(map, x, y, player, returnEarly, Rules.useOverrideStones);
    }

    public static boolean isMoveValid(Map map, int x, int y, char player, boolean returnEarly,
                                      boolean allowOverrideStones) {
        return isMoveValidImpl(map, x, y, player, returnEarly, allowOverrideStones, new ArrayList<>());
    }

    private static boolean isMoveValidImpl(Map map, int x, int y, char player, boolean returnEarly,
                                           boolean allowOverrideStones, List<Coordinate> capturableTiles) {
        // Holes are not allowed, neither for building nor for bomb phase
        if (MapUtil.isTileHole(map.getGameField()[y][x])) {
            return false;
        }
        // Building phase
        else if (Map.getPhase() == 1) {
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
                Set<Coordinate> tempTiles = new HashSet<>();
                boolean tempResult = walkPath(map, x, y, direction, player, tempTiles);

                if (returnEarly && tempResult) {
                    return true;
                }

                result |= tempResult;
                if (tempResult) {
                    capturableTiles.addAll(tempTiles);
                }
            }

            if (result) {
                capturableTiles.add(new Coordinate(x, y));
            }

            return result;
        } else {
            // Elimination phase
            // Check basic invalidity for elimination phase
            return map.getBombs()[Character.getNumericValue(player)] > 0;
        }
    }

    private static boolean walkPath(Map map, int startX, int startY, int direction, char player, Set<Coordinate> tempTiles) {
        int x = startX;
        int y = startY;
        tempTiles.add(new Coordinate(x, y));

        // Starts at -1 because the do while immediately adds the start tile, but the start tile doesn't count for a path
        int pathLength = -1;
        Transition transitionEnd;
        Coordinate newCoord;

        do {
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

            newCoord = new Coordinate(x, y);
            if (tempTiles.contains(newCoord)) {
                break;
            }

            pathLength++;
            tempTiles.add(newCoord);
        } while (MapUtil.isCoordinateInMap(x, y) && MapUtil.isCapturableStone(map, x, y, player));

        // Check whether the last tile of the path is in the map, not the start tile and has the player stone on it
        if (!(MapUtil.isCoordinateInMap(x, y)
                && pathLength > 0
                && map.getGameField()[y][x] == player
                && (startX != x || startY != y))) {
            tempTiles = new HashSet<>();
        }

        return !tempTiles.isEmpty();
    }

    public static MoveTriplet executeMove(Map map, int x, int y, int specialField, char player, List<Coordinate> capturableStones) {
        if (Map.getPhase() == 1) {
            if (MapUtil.isTileFree(map.getGameField()[y][x])) {
                map.decrementNumberFreeTiles();
            }

            return BuildMove.executeBuildMove(map, x, y, specialField, player, capturableStones);
        } else {
            map.getBombs()[MapUtil.playerToInt(player)]--;
            return BombMove.executeBombMove(map, x, y);
        }
    }
}