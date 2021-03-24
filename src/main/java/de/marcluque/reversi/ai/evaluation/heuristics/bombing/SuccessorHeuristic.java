package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.search.AbstractSearch;
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
        // Determine successor
        int playerStones = map.getNumberOfStones()[MapUtil.playerToInt(player)];
        int max = Integer.MIN_VALUE;
        int successor = -1;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            if ((playerStones - map.getNumberOfStones()[i]) > max) {
                max = playerStones - map.getNumberOfStones()[i];
                successor = i;
            }
        }

        int numberOfPlayableTiles = Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles();
        return (numberOfPlayableTiles - map.getNumberOfStones()[successor]) / (double) numberOfPlayableTiles;
    }
}