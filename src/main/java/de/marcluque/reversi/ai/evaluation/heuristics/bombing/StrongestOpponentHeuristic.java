package de.datasecs.reversi.ai.evaluation.heuristics.bombing;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;

public class StrongestOpponentHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {

    }

    @Override
    public double executeHeuristic(Map map, char player) {
        // Determine strongest opponent
        int strongestOpponent = -1;
        int max = Integer.MIN_VALUE;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            if (map.getNumberOfStones()[i] > max) {
                max = map.getNumberOfStones()[i];
                strongestOpponent = i;
            }
        }

        int numberOfPlayableTiles = Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles();
        return (numberOfPlayableTiles - map.getNumberOfStones()[strongestOpponent]) / (double) numberOfPlayableTiles;
    }
}