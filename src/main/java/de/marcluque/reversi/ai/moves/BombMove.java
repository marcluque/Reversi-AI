package de.marcluque.reversi.ai.moves;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.*;

import java.util.HashSet;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public abstract class BombMove {

    private static void executeBombMoveDFS(Map map, Set<Coordinate> visited, int x, int y, int depth) {
        int startX = x;
        int startY = y;
        visited.add(new Coordinate(x, y));

        if (MapUtil.isPlayerTile(map.getGameField()[y][x])) {
            map.getNumberOfStones()[MapUtil.playerToInt(map.getGameField()[y][x])] -= 1;
        }

        if (depth == Map.getBombRadius()) {
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

            if (MapUtil.isCoordinateInMap(x, y) && !MapUtil.isTileHole(map.getGameField()[y][x])) {
                executeBombMoveDFS(map, visited, x, y, depth + 1);
            }
        }
    }

    public static MoveTriplet executeBombMove(Map map, int x, int y) {
        Set<Coordinate> visited = new HashSet<>();
        executeBombMoveDFS(map, visited, x, y, 0);

        for (Coordinate c : visited) {
            map.getGameField()[c.getY()][c.getX()] = '-';
        }

        return new MoveTriplet(x, y, 0);
    }
}