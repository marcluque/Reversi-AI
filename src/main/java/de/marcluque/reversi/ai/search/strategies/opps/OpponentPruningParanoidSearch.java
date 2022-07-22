package de.marcluque.reversi.ai.search.strategies.opps;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.TerminalEvaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.MoveSorting;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;
import de.marcluque.reversi.util.SortNode;

/*
 * Created with <3 by marcluque, March 2021
 */
public class OpponentPruningParanoidSearch extends AbstractSearch {

    // TODO: MIGHT NEED SOME FINE TUNING:
    private static final int SIZE_GROUP_ONE = 1;

    private static final int BRANCHING_LIMIT_GROUP_ONE = 5;

    private static final int BRANCHING_LIMIT_GROUP_TWO = 1;

    public static MoveTriplet search(Map map, int depth, int[] totalStates) {
        MoveTriplet bestMoveTriplet = null;
        double maxValue = Double.MIN_VALUE;
        totalStates[0]++;

        var sortedMoves = MoveSorting.sortMoves(map, AbstractSearch.MAX);
        int moveCount = 0;

        for (int i = 0, sortedMovesSize = sortedMoves.size(); i < sortedMovesSize; i++) {
            SortNode move = sortedMoves.get(i);

            if (i >= BRANCHING_LIMIT_GROUP_TWO) {
                moveCount++;
            }

            double value = OPPS(move.getMap(), depth - 1, MapUtil.nextPlayer(MapUtil.playerToInt(MAX)),
                    moveCount, totalStates);

            if (value > maxValue) {
                bestMoveTriplet = move.getMove();
                maxValue = value;
            }
        }

        return bestMoveTriplet;
    }

    private static double OPPS(Map map, int depth, int turn, int moveCount, int[] totalStates) {
        totalStates[0]++;

        if (MapUtil.isTerminal(map)) {
            return TerminalEvaluation.utility(map);
        } else if (depth <= 0) {
            return HeuristicEvaluation.utility(map);
        }

        char player = MapUtil.intToPlayer(turn);
        boolean maxTurn = turn == MAX;

        var sortedMoves = MoveSorting.sortMoves(map, player);

        if (maxTurn) {
            moveCount = 0;
        } else if (moveCount == SIZE_GROUP_ONE) {
            sortedMoves = sortedMoves.subList(0, BRANCHING_LIMIT_GROUP_TWO);
        } else {
            sortedMoves = sortedMoves.subList(0, BRANCHING_LIMIT_GROUP_ONE);
        }

        double bestValue = 0;
        for (int i = 0, sortedMovesSize = sortedMoves.size(); i < sortedMovesSize; i++) {
            if (i >= BRANCHING_LIMIT_GROUP_TWO) {
                moveCount++;
            }

            double value = OPPS(sortedMoves.get(i).getMap(), depth - 1,
                    MapUtil.nextPlayer(turn), moveCount, totalStates);

            bestValue = maxTurn ? Math.max(bestValue, value) : Math.min(bestValue, value);
        }

        return bestValue;
    }
}