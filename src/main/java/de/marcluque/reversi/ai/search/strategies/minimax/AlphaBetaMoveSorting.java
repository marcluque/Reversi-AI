package de.marcluque.reversi.ai.search.strategies.minimax;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.MoveSorting;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.SortNode;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class AlphaBetaMoveSorting extends AbstractSearch {

    public static Move search(Map map, int depth, int[] totalStates) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;

        var sortedMoves = MoveSorting.sort(map, MAX, MapUtil.getAvailableMoves(map, MAX));
        int numberAvailableMoves = sortedMoves.size();

        for (SortNode move : sortedMoves) {
            totalStates[0]++;

            double value = minValue(move.getMap(), Double.MIN_VALUE, Double.MAX_VALUE, depth - 1,
                    numberAvailableMoves, totalStates);

            if (value > maxValue) {
                bestMove = move.getMove();
                maxValue = value;
            }
        }

        return bestMove;
    }

    private static double maxValue(Map map, double alpha, double beta, int depth, int numberAvailableMoves, int[] totalStates) {
        if (depth <= 0 || (numberAvailableMoves < 2 && MapUtil.isTerminal(map))) {
            return Evaluation.utility(map, MAX);
        }

        var sortedMoves = MoveSorting.sort(map, MIN, MapUtil.getAvailableMoves(map, MIN));
        numberAvailableMoves = sortedMoves.size();

        totalStates[0]++;
        double value = Double.MIN_VALUE;

        for (SortNode move : sortedMoves) {
            value = Math.max(value, minValue(move.getMap(), alpha, beta, depth - 1, numberAvailableMoves, totalStates));

            if (value >= beta) {
                return value;
            }

            alpha = Math.max(alpha, value);
        }

        return value;
    }

    private static double minValue(Map map, double alpha, double beta, int depth, int numberAvailableMoves, int[] totalStates) {
        if (depth <= 0 || (numberAvailableMoves < 2 && MapUtil.isTerminal(map))) {
            return Evaluation.utility(map, MAX);
        }

        var sortedMoves = MoveSorting.sort(map, MAX, MapUtil.getAvailableMoves(map, MAX));
        numberAvailableMoves = sortedMoves.size();

        totalStates[0]++;
        double value = Double.MAX_VALUE;

        for (SortNode move : sortedMoves) {
            value = Math.min(value, maxValue(move.getMap(), alpha, beta, depth - 1, numberAvailableMoves, totalStates));

            if (value <= alpha) {
                return value;
            }

            beta = Math.min(beta, value);
        }

        return value;
    }
}