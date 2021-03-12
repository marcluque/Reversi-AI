package de.marcluque.reversi.ai.search.strategies.opps;

import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.MoveSorting;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.SortNode;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class OpponentPruningParanoidSearch extends AbstractSearch {

    // TODO: MIGHT NEED SOME FINE TUNING:
    private static final int SIZE_GROUP_ONE = 1;

    private static final int BRANCHING_LIMIT_GROUP_ONE = 5;

    private static final int BRANCHING_LIMIT_GROUP_TWO = 1;

    public static Move search(Map map, int depth, int[] totalStates) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;

        var availableMoves = MapUtil.getAvailableMoves(map, MAX, Rules.OVERRIDE_STONES);

        var sortedMoves = MoveSorting.sort(map, MAX, availableMoves);
        int numberAvailableMoves = sortedMoves.size();
        int moveCount = 0;

        for (int i = 0, sortedMovesSize = sortedMoves.size(); i < sortedMovesSize; i++) {
            SortNode move = sortedMoves.get(i);
            Map mapClone = new Map(map);
            Move currentMove = AbstractMove.executeMove(mapClone, move.getX(), move.getY(), MAX,
                    availableMoves.get(move));
            totalStates[0]++;

            if (i >= BRANCHING_LIMIT_GROUP_TWO) {
                moveCount++;
            }

            double value = OPPS(mapClone, depth - 1,
                    (MapUtil.playerToInt(MAX) % Map.getNumberOfPlayers()) + 1,
                    moveCount, numberAvailableMoves, totalStates);

            maxValue = Math.max(maxValue, value);
            if (value > maxValue) {
                bestMove = currentMove;
                maxValue = value;
            }
        }

        return bestMove;
    }

    private static double OPPS(Map map, int depth, int turn, int moveCount, int numberAvailableMoves,
                               int[] totalStates) {
        if (numberAvailableMoves < 5 && MapUtil.isStateTerminal(map)) {
            return 1;
        } else if (depth == 0) {
            return 2;
        } else {
            char player = MapUtil.intToPlayer(turn);
            boolean maxTurn = turn == MAX;
            var availableMoves = MapUtil.getAvailableMoves(map, player, Rules.OVERRIDE_STONES);

            var sortedMoves = MoveSorting.sort(map, player, availableMoves);
            numberAvailableMoves = sortedMoves.size();

            if (maxTurn) {
                moveCount = 0;
            } else if (moveCount == SIZE_GROUP_ONE) {
                sortedMoves = sortedMoves.subList(0, BRANCHING_LIMIT_GROUP_TWO);
            } else {
                sortedMoves = sortedMoves.subList(0, BRANCHING_LIMIT_GROUP_ONE);
            }

            double bestValue = 0;
            for (int i = 0, sortedMovesSize = sortedMoves.size(); i < sortedMovesSize; i++) {
                SortNode move = sortedMoves.get(i);
                Map mapClone = new Map(map);
                AbstractMove.executeMove(mapClone, move.getX(), move.getY(), player, availableMoves.get(move));
                totalStates[0]++;

                if (i >= BRANCHING_LIMIT_GROUP_TWO) {
                    moveCount++;
                }

                double value = OPPS(mapClone, depth - 1, (turn % Map.getNumberOfPlayers()) + 1,
                        moveCount, numberAvailableMoves, totalStates);

                if (maxTurn) {
                    bestValue = Math.max(bestValue, value);
                } else {
                    bestValue = Math.min(bestValue, value);
                }
            }

            return bestValue;
        }
    }
}