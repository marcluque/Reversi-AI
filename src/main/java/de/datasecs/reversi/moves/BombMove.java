package de.datasecs.reversi.moves;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.MapUtil;
import de.datasecs.reversi.util.Transition;

public abstract class BombMove {

    private static boolean executeBombMoveRecursive(Map map, int x, int y, int depth, int[][] visited) {
        int startX = x;
        int startY = y;
        if (!MapUtil.isCoordinateInMap(x, y)
                || visited[y][x] == 1
                || MapUtil.isTileHole(map.getGameField()[y][x])
                || depth > Map.getBombRadius()) {
            return false;
        }

        for (int i = 0; i < 8; ++i) {
            Transition transitionEnd = Map.getTransitions().get(new Transition(x, y, i));

            if (transitionEnd != null) {
                x = transitionEnd.getX();
                y = transitionEnd.getY();
            } else {
                x += Move.CORNERS[i][0];
                y += Move.CORNERS[i][1];
            }

            if (!executeBombMoveRecursive(map, x, y, depth + 1, visited)) {
                return false;
            }

            visited[y][x] = 1;
            map.getGameField()[y][x] = '-';
            x = startX;
            y = startY;
        }

        return true;
    }

    public static void executeBombMove(Map map, int x, int y) {
        int[][] visited = new int[Map.getMapHeight()][Map.getMapWidth()];
        for (int i = 0; i < Map.getMapHeight(); i++) {
            for (int j = 0; j < Map.getMapWidth(); j++) {
                visited[i][j] = 0;
            }
        }

        executeBombMoveRecursive(map, x, y, 0, visited);
    }
}