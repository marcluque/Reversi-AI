package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Metrics {

    public static int maximalBombEffect;

    public static int maximalBombPower;

    public static int numberPlayableTiles;

    public static List<Character> opponentsWithMoves;

    public static void initNumberMetrics() {
        Map map = GameInstance.getMap();
        int numberFreeTiles = 0;
        int numberOfHoles = 0;

        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                if (MapUtil.isTileFree(map.getGameField()[y][x])) {
                    numberFreeTiles++;
                } else if (MapUtil.isTileHole(map.getGameField()[y][x])) {
                    numberOfHoles++;
                }
            }
        }

        map.setNumberFreeTiles(numberFreeTiles);
        Map.setNumberOfHoles(numberOfHoles);
    }

    public static void initBombEffect() {
        int diameter = Map.getBombRadius() + Map.getBombRadius() + 1;
        maximalBombEffect = diameter * diameter;
    }

    public static void initNumberPlayableTiles() {
        numberPlayableTiles = Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles();
    }

    public static void updateBombPower() {
        maximalBombPower = GameInstance.getMap().getBombs()[AbstractSearch.MAX_NUMBER] * maximalBombEffect;
    }

    public static void updateOpponentsWithMoves() {
        opponentsWithMoves = new ArrayList<>(Map.getNumberOfPlayers() - 1);

        for (int player = 1, numberOfPlayers = Map.getNumberOfPlayers(); player <= numberOfPlayers; player++) {
            if (player != AbstractSearch.MAX && playerHasMove(player)) {
                opponentsWithMoves.add(MapUtil.intToPlayer(player));
            }
        }
    }

    private static boolean playerHasMove(int player) {
        Map map = GameInstance.getMap();

        // Shortcut: If the player has override stones, he has available moves
        if (map.getOverrideStones()[player] > 0) {
            return true;
        }

        // Otherwise do a regular check for available moves
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