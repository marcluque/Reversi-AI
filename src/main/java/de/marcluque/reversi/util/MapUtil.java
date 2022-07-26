package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static boolean isTileCorner(Map map, int x, int y) {
        if (isTileHole(map.getGameField()[y][x])) {
            return false;
        }

        Transition transition;
        for (int k = 1; k < 8; k += 2) {
            int firstX = x + Move.getCORNERS()[k - 1][0];
            int firstY = y + Move.getCORNERS()[k - 1][1];
            transition = Map.getTransitions().get(new Transition(x, y, k - 1));
            if (transition != null) {
                firstX = transition.getX();
                firstY = transition.getY();
            }

            int secondX = x + Move.getCORNERS()[k][0];
            int secondY = y + Move.getCORNERS()[k][1];
            transition = Map.getTransitions().get(new Transition(x, y, k));
            if (transition != null) {
                secondX = transition.getX();
                secondY = transition.getY();
            }

            int thirdX = x + Move.getCORNERS()[(k + 1) % 8][0];
            int thirdY = y + Move.getCORNERS()[(k + 1) % 8][1];
            transition = Map.getTransitions().get(new Transition(x, y, k + 1));
            if (transition != null) {
                thirdX = transition.getX();
                thirdY = transition.getY();
            }

            if ((!isCoordinateInMap(firstX, firstY) || isTileHole(map.getGameField()[firstY][firstX]))
                    && (!isCoordinateInMap(secondX, secondY) || isTileHole(map.getGameField()[secondY][secondX]))
                    && (!isCoordinateInMap(thirdX, thirdY) || isTileHole(map.getGameField()[thirdY][thirdX]))) {
                return true;
            }
        }

        return false;
    }

    public static boolean noMovesPossibleForPlayer(Map map, char player) {
        List<Coordinate> freeTiles = new ArrayList<>();

        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                if (isTileFree(map.getGameField()[y][x])) {
                    freeTiles.add(new Coordinate(x, y));
                }
            }
        }

        for (Coordinate freeTile : freeTiles) {
            if (Move.isMoveValid(map, freeTile.getX(), freeTile.getY(), player, true)) {
                return false;
            }
        }

        return true;
    }

    public static boolean noMovesPossible(Map map) {
        List<Coordinate> freeTiles = new ArrayList<>();
        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                if (isTileFree(map.getGameField()[y][x])) {
                    freeTiles.add(new Coordinate(x, y));
                }
            }
        }

        for (Coordinate freeTile : freeTiles) {
            for (Character activePlayer : AbstractSearch.getActivePlayers()) {
                if (Move.isMoveValid(map, freeTile.getX(), freeTile.getY(), activePlayer, true)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isTerminal(Map map) {
        // When no free tiles are left and no player has any more override stones, we are in a terminal state
        return Map.getPhase() == 1
                ? Arrays.stream(map.getOverrideStones()).sum() <= 0 && noMovesPossible(map)
                : Arrays.stream(map.getBombs()).sum() <= 0;
    }

    public static int nextPlayer(int currentPlayer) {
        return (currentPlayer % Map.getNumberOfPlayers()) + 1;
    }

    public static int playerWithMaxStones(Map map) {
        int playerWithMostStones = -1;
        int max = -1;
        for (int i = 1; i <= Map.getNumberOfPlayers(); i++) {
            if (map.getNumberOfStones()[i] > max) {
                max = map.getNumberOfStones()[i];
                playerWithMostStones = i;
            }
        }

        return playerWithMostStones;
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

    @SuppressWarnings("squid:S00112")
    public static String compareMapTo(char[][] mapBefore, char[][] mapAfter) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, mapBeforeLength = mapBefore.length; i < mapBeforeLength; i++) {
            char[] tA = mapBefore[i];
            for (int j = 0, tALength = tA.length; j < tALength; j++) {
                char t = tA[j];

                if (t != mapAfter[i][j]) {
                    sb.append("\u001B[31m").append(t).append("\u001B[0m");
                } else {
                    sb.append(t);
                }
                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}