package de.marcluque.reversi.ai.search.strategies.brs;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.Move;

import java.util.ArrayList;
import java.util.List;

public class BestReplySearch extends AbstractSearch {

    public static Move search(Map map, int depth, boolean allowOverride, int[] totalStates) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValidImpl(map, x, y, MAX, false, allowOverride, capturableTiles
                )) {
                    Map mapClone = new Map(map);
                    Move currentMove = AbstractMove.executeMove(mapClone, x, y, MAX, capturableTiles);

                    double value = BRS(map, Double.MIN_VALUE, Double.MAX_VALUE, depth, MAX, allowOverride,
                            totalStates);
                    if (value > maxValue) {
                        maxValue = value;
                        bestMove = currentMove;
                    }

                    totalStates[0]++;
                }
            }
        }

        return bestMove;
    }

    private static double BRS(Map map, double alpha, double beta, int depth, char turn, boolean allowOverride,
                              int[] totalStates) {
        if (depth <= 0) {
            return Evaluation.utility(map);
        }

        double maxAlpha = alpha;
        if (turn == MAX) {
            maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, MAX, allowOverride, totalStates));
            return maxAlpha;
        } else {
            for (char opponent : OPPONENTS) {
                maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, opponent, allowOverride,
                        totalStates));
                return maxAlpha;
            }
        }

        return maxAlpha;
    }

    private static double BRSDoMoves(Map map, double alpha, double beta, int depth, char turn, boolean allowOverride,
                                     int[] totalStates) {
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValidImpl(map, x, y, turn, false, allowOverride, capturableTiles
                )) {
                    Map mapClone = new Map(map);
                    AbstractMove.executeMove(mapClone, x, y, turn, capturableTiles);
                    double value = -BRS(mapClone, -beta, -alpha, depth - 1,
                            (turn == MAX) ? OPPONENT : MAX, allowOverride, totalStates);

                    if (value >= beta) {
                        return value;
                    }

                    alpha = Math.max(alpha, value);

                    totalStates[0]++;
                }
            }
        }

        return alpha;
    }
}