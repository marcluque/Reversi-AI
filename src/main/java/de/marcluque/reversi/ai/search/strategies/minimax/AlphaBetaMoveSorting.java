package de.marcluque.reversi.ai.search.strategies.minimax;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.TerminalEvaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.MoveSorting;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.SortNode;

/*
 * Created with <3 by marcluque, March 2021
 */
public class AlphaBetaMoveSorting extends AbstractSearch {

    public static Move search(Map map, int depth, int[] totalStates) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;
        totalStates[0]++;

        var sortedMoves = MoveSorting.sortForMax(map);

        for (SortNode move : sortedMoves) {
            double value = minValue(move.getMap(), Double.MIN_VALUE, Double.MAX_VALUE, depth - 1, totalStates);

            if (value > maxValue) {
                bestMove = move.getMove();
                maxValue = value;
            }
        }

        return bestMove;
    }

    private static double maxValue(Map map, double alpha, double beta, int depth, int[] totalStates) {
        totalStates[0]++;

        if (MapUtil.isTerminal(map)) {
            return TerminalEvaluation.utility(map);
        } else if (depth <= 0) {
            return HeuristicEvaluation.utility(map);
        }

        var sortedMoves = MoveSorting.sortForMax(map);

        double value = Double.MIN_VALUE;

        for (SortNode move : sortedMoves) {
            value = Math.max(value, minValue(move.getMap(), alpha, beta, depth - 1, totalStates));

            if (value >= beta) {
                return value;
            }

            alpha = Math.max(alpha, value);
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

        var sortedMoves = MoveSorting.sortForMin(map);

        double value = Double.MAX_VALUE;

        for (SortNode move : sortedMoves) {
            value = Math.min(value, maxValue(move.getMap(), alpha, beta, depth - 1, totalStates));

            if (value <= alpha) {
                return value;
            }

            beta = Math.min(beta, value);
        }

        return value;
    }
}