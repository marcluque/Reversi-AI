package de.datasecs.reversi.util;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.moves.Move;

import java.util.LinkedList;
import java.util.List;
import java.util.function.*;

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

    public static boolean isTileSpecial(char type) {
        return type == 'c' || type == 'b' || type == 'i';
    }

    public static void search(Map map, char player, boolean override, int phase, Predicate<Map> consumer) {
        for (int i = 0; i < map.getGameField().length; i++) {
            for (int j = 0; j < map.getGameField()[0].length; j++) {
                List<Coordinate> capturableStones = new LinkedList<>();
                if (Move.isMoveValidImpl(map, j, i, player, false, override, capturableStones, phase)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(j, i, player, mapClone, capturableStones, phase);

                    if (consumer.test(mapClone)) {
                        return;
                    }
                }
            }
        }
    }

    public static void search(Map map, char player, boolean override, int phase, BiPredicate<Map, Coordinate> consumer) {
        for (int i = 0; i < map.getGameField().length; i++) {
            for (int j = 0; j < map.getGameField()[0].length; j++) {
                List<Coordinate> capturableStones = new LinkedList<>();
                if (Move.isMoveValidImpl(map, j, i, player, false, override, capturableStones, phase)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(j, i, player, mapClone, capturableStones, phase);

                    if (consumer.test(mapClone, new Coordinate(j, i))) {
                        return;
                    }
                }
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