package de.marcluque.reversi.ai.search.strategies.minimax;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.TerminalEvaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class AlphaBeta extends AbstractSearch {

    public static Move search(Map map, int depth, int[] totalStates) {
        final Move[] bestMove = {null};
        final double[] maxValue = {Integer.MIN_VALUE};
        totalStates[0]++;

        MapUtil.iterateMap((x, y) -> {
            List<Coordinate> capturableTiles = new ArrayList<>();
            if (AbstractMove.isMoveValid(map, x, y, MAX, false, capturableTiles)) {
                Map mapClone = new Map(map);
                Move currentMove = AbstractMove.executeMove(mapClone, x, y, 0, MAX, capturableTiles);

                double value = minValue(mapClone, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, totalStates);
                if (value > maxValue[0]) {
                    maxValue[0] = value;
                    bestMove[0] = currentMove;
                }
            }
        });

        return bestMove[0];
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
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValid(map, x, y, MAX, false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    AbstractMove.executeMove(mapClone, x, y, 0, MAX, capturableTiles);

                    value = Math.max(value, minValue(mapClone, alpha, beta, depth - 1, totalStates));

                    if (value >= beta) {
                        return value;
                    }

                    alpha = Math.max(alpha, value);
                }
            }
        }

        if (value == Integer.MIN_VALUE) {
            return HeuristicEvaluation.utility(map);
        }

        return value;
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
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValid(map, x, y, MIN, false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    AbstractMove.executeMove(mapClone, x, y, 0, MIN, capturableTiles);

                    value = Math.min(value, maxValue(mapClone, alpha, beta, depth - 1, totalStates));

                    if (value <= alpha) {
                        return value;
                    }

                    beta = Math.min(beta, value);
                }
            }
        }

        if (value == Integer.MAX_VALUE) {
            return HeuristicEvaluation.utility(map);
        }

        return value;
    }
}