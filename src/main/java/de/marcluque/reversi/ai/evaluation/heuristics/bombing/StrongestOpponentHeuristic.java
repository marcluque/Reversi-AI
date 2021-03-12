package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class StrongestOpponentHeuristic extends AbstractHeuristic implements Heuristic {

    public StrongestOpponentHeuristic(double weight) {
        super.weight = weight;
    }

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