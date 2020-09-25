package de.datasecs.reversi.moves;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.MapUtil;
import de.datasecs.reversi.util.Transition;

public abstract class BombMove {

    private static void executeBombMoveRecursive(Map map, int x, int y, int depth) {
        int startX = x;
        int startY = y;

        if (!MapUtil.isCoordinateInMap(x, y) || MapUtil.isTileHole(map.getGameField()[y][x])) {
            return;
        } else if (depth == Map.getBombRadius()) {
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
        map.getGameField()[y][x] = '$';
    }

    public static void executeBombMove(Map map, int x, int y) {
        executeBombMoveRecursive(map, x, y, 0);

        for (int i = 0; i < Map.getMapHeight(); i++) {
            for (int j = 0; j < Map.getMapWidth(); j++) {
                if (map.getGameField()[i][j] == '$') {
                    map.getGameField()[i][j] = '-';
                }
            }
        }
    }
}