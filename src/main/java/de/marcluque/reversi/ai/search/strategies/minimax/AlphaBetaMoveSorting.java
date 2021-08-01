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
        double maxValue = Integer.MIN_VALUE;
        totalStates[0]++;

        var sortedMoves = MoveSorting.sortMoves(map, AbstractSearch.MAX);

        for (SortNode move : sortedMoves) {
            double value = minValue(move.getMap(), Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, totalStates);

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

        var sortedMoves = MoveSorting.sortMoves(map, AbstractSearch.MAX);

        double value = Integer.MIN_VALUE;

        for (SortNode move : sortedMoves) {
            value = Math.max(value, minValue(move.getMap(), alpha, beta, depth - 1, totalStates));

            if (value >= beta) {
                return value;
            }

            alpha = Math.max(alpha, value);
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

        var sortedMoves = MoveSorting.sortMoves(map, AbstractSearch.MIN);

        double value = Integer.MAX_VALUE;

        for (SortNode move : sortedMoves) {
            value = Math.min(value, maxValue(move.getMap(), alpha, beta, depth - 1, totalStates));

            if (value <= alpha) {
                return value;
            }

            beta = Math.min(beta, value);
        }

        return value == Integer.MAX_VALUE ? HeuristicEvaluation.utility(map) : value;
    }
}