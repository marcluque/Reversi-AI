package de.datasecs.reversi.ai.search.strategies.brs;

import de.datasecs.reversi.ai.evaluation.Evaluation;
import de.datasecs.reversi.ai.search.AbstractSearch;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.moves.AbstractMove;
import de.datasecs.reversi.util.Coordinate;
import de.datasecs.reversi.util.Move;

import java.util.ArrayList;
import java.util.List;

public class BestReplySearch extends AbstractSearch {

    public static Move search(Map map, int depth, boolean allowOverride, int phase) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValidImpl(map, x, y, MAX, false, allowOverride, capturableTiles, phase)) {
                    Map mapClone = new Map(map);
                    Move currentMove = AbstractMove.executeMove(mapClone, x, y, MAX, capturableTiles, phase);

                    double value = BRS(map, Double.MIN_VALUE, Double.MAX_VALUE, depth, MAX, allowOverride, phase);
                    if (value > maxValue) {
                        maxValue = value;
                        bestMove = currentMove;
                    }
                }
            }
        }

        return bestMove;
    }

    private static double BRS(Map map, double alpha, double beta, int depth, char turn, boolean allowOverride, int phase) {
        if (depth <= 0) {
            Evaluation.utility(map, turn);
        }

        double maxAlpha = alpha;
        if (turn == MAX) {
            maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, MAX, allowOverride, phase));
            return maxAlpha;
        } else {
            for (char opponent : OPPONENTS) {
                maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, opponent, allowOverride, phase));
                return maxAlpha;
            }
        }

        return maxAlpha;
    }

    private static double BRSDoMoves(Map map, double alpha, double beta, int depth, char turn, boolean allowOverride, int phase) {
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValidImpl(map, x, y, turn, false, allowOverride, capturableTiles, phase)) {
                    Map mapClone = new Map(map);
                    AbstractMove.executeMove(mapClone, x, y, turn, capturableTiles, phase);
                    double value = -BRS(mapClone, -beta, -alpha, depth - 1, (turn == MAX) ? OPPONENT : MAX, allowOverride, phase);

                    if (value >= beta) {
                        return value;
                    }

                    alpha = Math.max(alpha, value);
                }
            }
        }

        return alpha;
    }
}