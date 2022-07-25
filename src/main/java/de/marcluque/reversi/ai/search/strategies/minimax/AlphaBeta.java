package de.marcluque.reversi.ai.search.strategies.minimax;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.TerminalEvaluation;
import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;

import java.util.HashSet;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public class AlphaBeta extends AbstractSearch {

    public static MoveTriplet search(Map map, int depth, int[] totalStates) {
        final MoveTriplet[] bestMoveTriplet = {null};
        final double[] maxValue = {Integer.MIN_VALUE};
        totalStates[0]++;

        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                Set<Coordinate> capturableStones = new HashSet<>();
                if (Move.isMoveValid(map, x, y, MAX, false, capturableStones)) {
                    Map mapClone = new Map(map);
                    MoveTriplet currentMoveTriplet = Move.executeMove(mapClone, x, y, 0, MAX, capturableStones);

                    double value = minValue(mapClone, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, totalStates);
                    if (value > maxValue[0]) {
                        maxValue[0] = value;
                        bestMoveTriplet[0] = currentMoveTriplet;
                    }
                }
            }
        }

        return bestMoveTriplet[0];
    }

    private static double maxValue(Map map, double alpha, double beta, int depth, int[] totalStates) {
        totalStates[0]++;

        if (MapUtil.isTerminal(map)) {
            return TerminalEvaluation.utility(map);
        } else if (depth <= 0) {
            return HeuristicEvaluation.utility(map);
        }

        double value = Integer.MIN_VALUE;

        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                Set<Coordinate> capturableStones = new HashSet<>();
                if (Move.isMoveValid(map, x, y, MAX, false, capturableStones)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(mapClone, x, y, 0, MAX, capturableStones);

                    value = Math.max(value, minValue(mapClone, alpha, beta, depth - 1, totalStates));

                    if (value >= beta) {
                        return value;
                    }

                    alpha = Math.max(alpha, value);
                }
            }
        }

        return value == Integer.MIN_VALUE ? HeuristicEvaluation.utility(map) : value;
    }

    private static double minValue(Map map, double alpha, double beta, int depth, int[] totalStates) {
        totalStates[0]++;

        if (MapUtil.isTerminal(map)) {
            return TerminalEvaluation.utility(map);
        } else if (depth <= 0) {
            return HeuristicEvaluation.utility(map);
        }

        double value = Integer.MAX_VALUE;

        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                Set<Coordinate> capturableStones = new HashSet<>();
                if (Move.isMoveValid(map, x, y, MIN, false, capturableStones)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(mapClone, x, y, 0, MIN, capturableStones);

                    value = Math.min(value, maxValue(mapClone, alpha, beta, depth - 1, totalStates));

                    if (value <= alpha) {
                        return value;
                    }

                    beta = Math.min(beta, value);
                }
            }
        }

        return value == Integer.MAX_VALUE ? HeuristicEvaluation.utility(map) : value;
    }
}