package de.marcluque.reversi.ai.moves;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;
import de.marcluque.reversi.util.Transition;

/*
 * Created with <3 by marcluque, March 2021
 */
public abstract class BombMove {

    private static void executeBombMoveRecursive(Map map, int x, int y, int depth) {
        int startX = x;
        int startY = y;

        if (!MapUtil.isCoordinateInMap(x, y) || MapUtil.isTileHole(map.getGameField()[y][x])) {
            return;
        } else if (depth == Map.getBombRadius()) {
            if (MapUtil.isPlayerTile(map.getGameField()[y][x])) {
                map.getNumberOfStones()[MapUtil.playerToInt(map.getGameField()[y][x])]--;
            }

            map.getGameField()[y][x] = '$';
            return;
        }

        for (int i = 0; i < 8; ++i) {
            x = startX;
            y = startY;
            Transition transitionEnd = Map.getTransitions().get(new Transition(x, y, i));

            if (transitionEnd != null) {
                x = transitionEnd.getX();
                y = transitionEnd.getY();
            } else {
                x += Move.CORNERS[i][0];
                y += Move.CORNERS[i][1];
            }

            if (MapUtil.isCoordinateInMap(x, y)) {
                executeBombMoveRecursive(map, x, y, depth + 1);
            }
        }

        x = startX;
        y = startY;

        if (MapUtil.isPlayerTile(map.getGameField()[y][x])) {
            map.getNumberOfStones()[MapUtil.playerToInt(map.getGameField()[y][x])]--;
        }

        map.getGameField()[y][x] = '$';
    }

    public static MoveTriplet executeBombMove(Map map, int x, int y) {
        executeBombMoveRecursive(map, x, y, 0);

        for (int yMap = 0, height = Map.getMapHeight(); yMap < height; yMap++) {
            for (int xMap = 0, width = Map.getMapWidth(); xMap < width; xMap++) {
                if (map.getGameField()[yMap][xMap] == '$') {
                    map.getGameField()[yMap][xMap] = '-';
                }
            }
        }

        return new MoveTriplet(x, y, 0);
    }
}