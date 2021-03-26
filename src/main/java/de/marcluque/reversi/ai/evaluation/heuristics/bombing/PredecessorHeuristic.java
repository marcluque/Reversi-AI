package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

/*
 * Created with <3 by marcluque, March 2021
 */
public class PredecessorHeuristic extends AbstractHeuristic implements Heuristic {

    public PredecessorHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int playerStones = map.getNumberOfStones()[MapUtil.playerToInt(player)];
        double minimalStoneDistance = Integer.MAX_VALUE;

        // Determine minimal stone distance to opponent "before" player (aka predecessor)
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            int stoneDistance = map.getNumberOfStones()[i] - playerStones;
            if (stoneDistance > 0 && stoneDistance < minimalStoneDistance) {
                minimalStoneDistance = stoneDistance;
            }
        }

        // The greater the distance between the player and its predecessor, the better
        return minimalStoneDistance / playerStones;
    }
}