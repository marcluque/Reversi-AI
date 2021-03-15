package de.marcluque.reversi.ai.evaluation.metrics;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class Metrics {

    public static List<Character> OPPONENTS_WITH_MOVES;

    public static void updatePlayersWithMoves(Map map) {
        OPPONENTS_WITH_MOVES = new ArrayList<>(Map.getNumberOfPlayers() - 1);

        for (int player = 1, numberOfPlayers = Map.getNumberOfPlayers(); player <= numberOfPlayers; player++) {
            if (player != AbstractSearch.MAX && playerHasMove(map, player)) {
                OPPONENTS_WITH_MOVES.add(MapUtil.intToPlayer(player));
            }
        }
    }

    private static boolean playerHasMove(Map map, int player) {
        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                if (AbstractMove.isMoveValid(map, x, y, MapUtil.intToPlayer(player), true)) {
                    return true;
                }
            }
        }

        return false;
    }
}