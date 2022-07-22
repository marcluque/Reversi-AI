package de.marcluque.reversi.ai.moves;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.*;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public abstract class BombMove {

    private static void executeBombMoveWithBFS(Map map, int x, int y) {
        Set<Coordinate> visited = new HashSet<>();
        Queue<Triplet> q = new ArrayDeque<>();
        q.offer(new Triplet(x, y, 0));

        while (!q.isEmpty()) {
            Triplet t = q.remove();
            x = t.getX();
            y = t.getY();

            char tile = map.getGameField()[y][x];
            if (MapUtil.isPlayerTile(tile)) {
                map.getNumberOfStones()[MapUtil.playerToInt(tile)] -= 1;
            }

            map.getGameField()[y][x] = '-';

            // Before we can leave, we need to make sure that a player's stone is discounted
            if (t.getZ() == Map.getBombRadius()) {
                continue;
            }

            for (int i = 0; i < 8; ++i) {
                x = t.getX();
                y = t.getY();
                Transition transitionEnd = Map.getTransitions().get(new Transition(x, y, i));

                if (transitionEnd != null) {
                    x = transitionEnd.getX();
                    y = transitionEnd.getY();
                } else {
                    x += Move.CORNERS[i][0];
                    y += Move.CORNERS[i][1];
                }

                Coordinate c = new Coordinate(x, y);
                if (MapUtil.isCoordinateInMap(x, y) && !visited.contains(c)
                        && !MapUtil.isTileHole(map.getGameField()[y][x])) {
                    visited.add(c);
                    q.offer(new Triplet(x, y, t.getZ() + 1));
                }
            }
        }
    }

    public static MoveTriplet executeBombMove(Map map, int x, int y) {
        executeBombMoveWithBFS(map, x, y);
        return new MoveTriplet(x, y, 0);
    }
}