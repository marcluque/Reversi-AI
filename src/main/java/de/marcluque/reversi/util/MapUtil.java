package de.marcluque.reversi.util;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.AbstractMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MapUtil {

    private MapUtil() {}

    public static boolean isCoordinateInMap(int x, int y) {
        return x >= 0 && x < Map.getMapWidth() && y >= 0 && y < Map.getMapHeight();
    }

    public static boolean isCapturableStone(Map map, int x, int y, int player) {
        return MapUtil.isOccupied(map.getGameField()[y][x]) && map.getGameField()[y][x] != player;
    }

    public static boolean isPlayerTile(char type) {
        return '1' <= type && type <= '8';
    }

    public static boolean isTileExpansion(char type) {
        return type == 'x';
    }

    /**
     * Considers tiles with expansion stones as occupied according to the specification.
     *
     * @return whether the current tile is occupied
     */
    public static boolean isOccupied(char type) {
        // Check whether type is expansion stone OR character from 1 to 8, using ascii values
        return isTileExpansion(type) || isPlayerTile(type);
    }

    public static boolean isTileHole(char type) {
        return type == '-';
    }

    public static boolean isTileBonus(char type) {
        return type == 'b';
    }

    public static boolean isTileSpecial(char type) {
        return type == 'c' || isTileBonus(type) || type == 'i';
    }

    public static boolean isTileFree(char type) {
        return type == '0';
    }

    public static boolean isDifferentPlayerStone(Map map, int x, int y, int player) {
        return MapUtil.isOccupied(map.getGameField()[y][x]) && map.getGameField()[y][x] != player;
    }

    public static int playerToInt(char player) {
        return player - '0';
    }

    public static char intToPlayer(int player) {
        return (char) ('0' + player);
    }

    public static boolean isTileCapturableAndInMap(int x, int y, char type) {
        return isCoordinateInMap(x, y) && !isTileHole(type);
    }

    public static Transition[] isTileCorner(Map map, int x, int y) {
        if (!isTileCapturableAndInMap(x, y, map.getGameField()[y][x])) {
            return null;
        }

        // Check every pair of neighbours around current tile (i,j)
        Transition transition;
        Coordinate neighbour;
        Coordinate oppositeNeighbour;
        // Maximum amount of neighbours is 4
        Transition[] neighbours = new Transition[4];
        int neighbourCounter = 0;

        // k in [0,3] because opposite neighbours are also checked
        for (int k = 0; k < 4; k++) {
            // Checks whether a path via a transition can be enclosed
            // Check the potential neighbour in direction k
            transition = Map.getTransitions().get(new Transition(x, y, k));
            neighbour = (transition != null)
                    ? new Coordinate(transition.getX(), transition.getY())
                    : new Coordinate(x + AbstractMove.CORNERS[k][0], y + AbstractMove.CORNERS[k][1]);

            // Check the potential opposite neighbour in direction k + 4
            transition = Map.getTransitions().get(new Transition(x, y, k + 4));
            oppositeNeighbour = (transition != null)
                    ? new Coordinate(transition.getX(), transition.getY())
                    : new Coordinate(x + AbstractMove.CORNERS[k + 4][0], y + AbstractMove.CORNERS[k + 4][1]);

            // Returns null if there is a pair of neighbours around the tile (x,y) in opposite directions
            boolean neighbourPresent = MapUtil.isTileCapturableAndInMap(
                    neighbour.getX(),
                    neighbour.getY(),
                    map.getGameField()[neighbour.getY()][neighbour.getX()]);
            boolean oppositeNeighbourPresent = MapUtil.isTileCapturableAndInMap(
                    oppositeNeighbour.getX(),
                    oppositeNeighbour.getY(),
                    map.getGameField()[oppositeNeighbour.getY()][oppositeNeighbour.getX()]);
            if (neighbourPresent && oppositeNeighbourPresent) {
                return null;
            } else if (neighbourPresent) {
                neighbours[neighbourCounter++] = new Transition(neighbour.getX(), neighbour.getY(), k);
            } else if (oppositeNeighbourPresent) {
                neighbours[neighbourCounter++] = new Transition(oppositeNeighbour.getX(), oppositeNeighbour.getY(), k + 4);
            }
        }

        // Returns the neighbours when no such pair is found
        return neighbours;
    }

    public static boolean isTerminal(Map map) {
        // When no free tiles are left and no player has any more override stones, we are in a terminal state
        return map.getNumberFreeTiles() <= 0 || Arrays.stream(map.getOverrideStones()).sum() <= 0;
    }

    public static int nextPlayer(int currentPlayer)  {
        return (currentPlayer % Map.getNumberOfPlayers()) + 1;
    }

    public static int playerWithMaxStones(Map map) {
        int playerWithMostStones = -1;
        int max = -1;
        for (int i = 1; i <= 8; i++) {
            if (map.getNumberOfStones()[i] > max) {
                max = map.getNumberOfStones()[i];
                playerWithMostStones = i;
            }
        }

        return playerWithMostStones;
    }

    public static void iterateMap(BiConsumer<Integer, Integer> action) {
        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                action.accept(x, y);
            }
        }
    }

    public static String mapToPrintableString(char[][] map) {
        StringBuilder sb = new StringBuilder();

        for (char[] tA : map) {
            for (char t : tA) {
                sb.append(t).append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}