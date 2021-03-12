package de.marcluque.reversi.ai.search.strategies.brs;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.Move;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class BestReplySearch extends AbstractSearch {

    public static Move search(Map map, int depth, int[] totalStates) {
        Move bestMove = null;
        double maxValue = Double.MIN_VALUE;
        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (AbstractMove.isMoveValid(map, x, y, MAX, false, capturableTiles)) {
                    Map mapClone = new Map(map);
                    Move currentMove = AbstractMove.executeMove(mapClone, x, y, MAX, capturableTiles);
                    totalStates[0]++;

                    double value = BRS(map, Double.MIN_VALUE, Double.MAX_VALUE, depth, MAX, totalStates);
                    if (value > maxValue) {
                        maxValue = value;
                        bestMove = currentMove;
                    }
                }
            }
        }

        return bestMove;
    }

    private static double BRS(Map map, double alpha, double beta, int depth, char turn, int[] totalStates) {
        if (depth <= 0) {
            return Evaluation.utility(map, MAX);
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
                    totalStates[0]++;
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