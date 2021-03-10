package de.datasecs.reversi.ai.evaluation.heuristics.bombing;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.MapUtil;

public class PredecessorHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        // Determine predecessor
        int playerStones = map.getNumberOfStones()[MapUtil.playerToInt(player)];
        int min = Integer.MAX_VALUE;
        int predecessor = -1;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            if ((playerStones - map.getNumberOfStones()[i]) < min) {
                min = playerStones - map.getNumberOfStones()[i];
                predecessor = i;
            }
        }

        int numberOfPlayableTiles = Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles();
        return (numberOfPlayableTiles - map.getNumberOfStones()[predecessor]) / (double) numberOfPlayableTiles;
    }
}