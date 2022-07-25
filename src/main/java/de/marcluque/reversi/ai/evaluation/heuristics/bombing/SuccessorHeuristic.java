package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

/*
 * Created with <3 by marcluque, March 2021
 */
public class SuccessorHeuristic extends AbstractHeuristic implements Heuristic {

    public SuccessorHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int playerStones = map.getNumberOfStones()[MapUtil.playerToInt(player)];
        double minimalStoneDistance = Integer.MAX_VALUE;

        // Determine minimal stone distance to opponent "after" player (aka successor)
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            int stoneDistance = playerStones - map.getNumberOfStones()[i];
            if (stoneDistance > 0 && stoneDistance < minimalStoneDistance) {
                minimalStoneDistance = stoneDistance;
            }
        }

        // The greater the distance between the player and its successor, the better
        return minimalStoneDistance / playerStones;
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