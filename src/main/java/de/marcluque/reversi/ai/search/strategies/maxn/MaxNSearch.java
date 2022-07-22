package de.marcluque.reversi.ai.search.strategies.maxn;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.TerminalEvaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MaxNSearch extends AbstractSearch {

    public static MoveTriplet search(Map map, int depth, int[] totalStates) {
        final MoveTriplet[] bestMoveTriplet = {null};
        final double[] maxValue = {Double.MIN_VALUE};
        int MAX_INT = MapUtil.playerToInt(MAX);
        totalStates[0]++;

        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (Move.isMoveValid(map, x, y, MAX, false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    MoveTriplet currentMoveTriplet = Move.executeMove(mapClone, x, y, 0, MAX, capturableTiles);

                    double value = maxValue(map, depth - 1, MapUtil.nextPlayer(MAX_INT), totalStates)[MAX_INT];
                    if (value > maxValue[0]) {
                        maxValue[0] = value;
                        bestMoveTriplet[0] = currentMoveTriplet;
                    }
                }
            }
        }

        return bestMoveTriplet[0];
    }

    private static double[] maxValue(Map map, int player, int depth, int[] totalStates) {
        totalStates[0]++;

        if (MapUtil.isTerminal(map)) {
            return TerminalEvaluation.nPlayerUtility(map);
        } else if (depth <= 0) {
            return HeuristicEvaluation.nPlayerUtility(map);
        }

        double[] maxValue = new double[Map.getNumberOfPlayers() + 1];
        Arrays.fill(maxValue, player == MAX ? Double.MIN_VALUE : Double.MAX_VALUE);

        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (Move.isMoveValid(map, x, y, MapUtil.intToPlayer(player), false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(mapClone, x, y, 0, MapUtil.intToPlayer(player), capturableTiles);
                    totalStates[0]++;

                    double[] value = maxValue(mapClone, depth - 1, MapUtil.nextPlayer(player), totalStates);
                    if (value[player] > maxValue[player]) {
                        System.arraycopy(value, 0, maxValue, 0, maxValue.length);
                    }
                }
            }
        }

        return maxValue;
    }
}