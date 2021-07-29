package de.marcluque.reversi.ai.search.strategies.brs;

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
public class BestReplySearch extends AbstractSearch {

    public static Move search(Map map, int depth, int[] totalStates) {
        final Move[] bestMove = {null};
        final double[] maxValue = {Double.MIN_VALUE};
        totalStates[0]++;

        MapUtil.iterateMap((x, y) -> {
            List<Coordinate> capturableTiles = new ArrayList<>();
            if (AbstractMove.isMoveValid(map, x, y, MAX, false, capturableTiles)) {
                Map mapClone = new Map(map);
                Move currentMove = AbstractMove.executeMove(mapClone, x, y, MAX, capturableTiles);

                double value = BRS(map, Double.MIN_VALUE, Double.MAX_VALUE, depth - 1, MAX, totalStates);
                if (value > maxValue[0]) {
                    maxValue[0] = value;
                    bestMove[0] = currentMove;
                }
            }
        });

        return bestMove[0];
    }

    private static double BRS(Map map, double alpha, double beta, int depth, char turn, int[] totalStates) {
        totalStates[0]++;

        if (MapUtil.isTerminal(map)) {
            return TerminalEvaluation.utility(map);
        } else if (depth <= 0) {
            return HeuristicEvaluation.utility(map);
        }

        double maxAlpha = alpha;
        if (turn == MAX) {
            maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, MAX, totalStates));
            return maxAlpha;
        } else {
            for (char opponent : OPPONENTS) {
                maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, opponent, totalStates));
                return maxAlpha;
            }
        }

        return maxAlpha;
    }

    private static double BRSDoMoves(Map map, double alpha, double beta, int depth, char turn, int[] totalStates) {
        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValid(map, x, y, turn, false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    AbstractMove.executeMove(mapClone, x, y, turn, capturableTiles);

                    double value = -BRS(mapClone, -beta, -alpha, depth - 1,
                            (turn == MAX) ? OPPONENT : MAX, totalStates);

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