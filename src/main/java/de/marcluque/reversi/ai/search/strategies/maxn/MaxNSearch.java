package de.marcluque.reversi.ai.search.strategies.maxn;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class MaxNSearch extends AbstractSearch {

    public static Move search(Map map, int depth, int[] totalStates) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;
        int MAX_INT = MapUtil.playerToInt(MAX);
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValid(map, x, y, MAX, false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    Move currentMove = AbstractMove.executeMove(mapClone, x, y, MAX, capturableTiles);
                    totalStates[0]++;

                    double value = maxValue(map, depth, (MAX_INT % Map.getNumberOfPlayers()) + 1, totalStates)[MAX_INT];
                    if (value > maxValue) {
                        maxValue = value;
                        bestMove = currentMove;
                    }
                }
            }
        }

        return bestMove;
    }

    private static double[] maxValue(Map map, int player, int depth, int[] totalStates) {
        return search(map, player, depth, totalStates, (value, mapClone) -> {
            double[] newValue = maxValue(mapClone, depth, (player % Map.getNumberOfPlayers()) + 1, totalStates);
            return newValue[player] > value[player] ? newValue : value;
        });
    }

    private static double[] search(Map map, int player, int depth, int[] totalStates,
                                 BiFunction<double[], Map, double[]> searchFunction) {
        // TODO: TERMINAL-TEST
        if (depth <= 0) {
            return Evaluation.utility(map);
        }

        totalStates[0]++;

        double[] value = new double[Map.getNumberOfPlayers()];
        if (player == MAX) {
            Arrays.fill(value, Double.MIN_VALUE);
        } else {
            Arrays.fill(value, Double.MAX_VALUE);
        }

        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValid(map, x, y, MapUtil.intToPlayer(player), false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    AbstractMove.executeMove(mapClone, x, y, MapUtil.intToPlayer(player), capturableTiles);
                    totalStates[0]++;
                    value = searchFunction.apply(value, mapClone);
                }
            }
        }

        return value;
    }
}