package de.datasecs.reversi.ai.evaluation.heuristics.bombing;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.MapUtil;

public class SuccessorHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player, boolean allowOverrideStones) {
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