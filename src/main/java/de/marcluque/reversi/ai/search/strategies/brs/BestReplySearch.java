package de.marcluque.reversi.ai.search.strategies.brs;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.TerminalEvaluation;
import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;

import java.util.HashSet;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public class BestReplySearch extends AbstractSearch {

    public static MoveTriplet search(Map map, int depth, int[] totalStates) {
        final MoveTriplet[] bestMoveTriplet = {null};
        final double[] maxValue = {Double.MIN_VALUE};
        totalStates[0]++;

        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                Set<Coordinate> capturableStones = new HashSet<>();
                if (Move.isMoveValid(map, x, y, MAX, false, capturableStones)) {
                    Map mapClone = new Map(map);
                    MoveTriplet currentMoveTriplet = Move.executeMove(mapClone, x, y, 0, MAX, capturableStones);

                    double value = BRS(map, Double.MIN_VALUE, Double.MAX_VALUE, depth - 1, MAX, totalStates);
                    if (value > maxValue[0]) {
                        maxValue[0] = value;
                        bestMoveTriplet[0] = currentMoveTriplet;
                    }
                }
            }
        }

        return bestMoveTriplet[0];
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
        } else {
            for (char opponent : OPPONENTS) {
                maxAlpha = Math.max(maxAlpha, BRSDoMoves(map, alpha, beta, depth, opponent, totalStates));
            }
        }

        return maxAlpha;
    }

    private static double BRSDoMoves(Map map, double alpha, double beta, int depth, char turn, int[] totalStates) {
        for (int y = 0, mapHeight = Map.getMapHeight(); y < mapHeight; y++) {
            for (int x = 0, mapWidth = Map.getMapWidth(); x < mapWidth; x++) {
                Set<Coordinate> capturableStones = new HashSet<>();
                if (Move.isMoveValid(map, x, y, turn, false, capturableStones)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(mapClone, x, y, 0, turn, capturableStones);

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