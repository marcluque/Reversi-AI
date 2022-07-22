package de.marcluque.reversi.ai.evaluation.heuristics.building;

import de.marcluque.reversi.ai.evaluation.Metrics;
import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.Move;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MobilityHeuristic extends AbstractHeuristic implements Heuristic {

    public MobilityHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        final double[] numberOfMoves = {0};

        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                if (Move.isMoveValid(map, x, y, player, true, Rules.useOverrideStones)) {
                    numberOfMoves[0]++;
                }
            }
        }

        return numberOfMoves[0] / Metrics.numberPlayableTiles;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void updateWeight(double weight) {
        super.weight = weight;
    }
}