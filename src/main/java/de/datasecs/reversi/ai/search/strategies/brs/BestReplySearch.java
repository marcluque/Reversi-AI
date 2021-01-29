package de.datasecs.reversi.ai.search.strategies.brs;

import de.datasecs.reversi.ai.search.AbstractSearch;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.moves.Move;
import de.datasecs.reversi.util.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class BestReplySearch extends AbstractSearch {

    public static void search(Map map, int depth, boolean allowOverride, int phase) {
        BRS(map, Double.MAX_VALUE, Double.MIN_VALUE, depth, MAX, allowOverride, phase);
    }

    private static double BRS(Map map, double alpha, double beta, int depth, char turn, boolean allowOverride, int phase) {
        if (depth <= 0) {
            //return evaluation();
        }

        double latestAlpha = alpha;
        if (turn == MAX) {
            return latestAlpha = BRSDoMoves(map, alpha, beta, depth, MAX, allowOverride, phase);
        } else {
            for (char opponent : OPPONENTS) {
                return latestAlpha = BRSDoMoves(map, alpha, beta, depth, opponent, allowOverride, phase);
            }
        }

        return latestAlpha;
    }

    private static double BRSDoMoves(Map map, double alpha, double beta, int depth, char turn, boolean allowOverride, int phase) {
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                List<Coordinate> capturableTiles = new ArrayList<>();
                if (Move.isMoveValidImpl(map, x, y, MAX, true, allowOverride, capturableTiles, phase)) {
                    Map mapClone = new Map(map);
                    Move.executeMove(mapClone, x, y, MAX, capturableTiles, phase);
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